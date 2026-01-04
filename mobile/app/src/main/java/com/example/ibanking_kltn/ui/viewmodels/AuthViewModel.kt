package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.di.BiometricManager
import com.example.ibanking_kltn.data.di.TokenManager
import com.example.ibanking_kltn.data.dtos.requests.LoginRequest
import com.example.ibanking_kltn.data.dtos.requests.LoginViaBiometricRequest
import com.example.ibanking_kltn.data.repositories.AuthRepository
import com.example.ibanking_kltn.ui.security.BiometricAuthenticator
import com.example.ibanking_kltn.ui.uistates.AuthUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val biometricManager: BiometricManager,
    @ApplicationContext private val context: Context,
    private val biometricAuthenticator: BiometricAuthenticator,
) : ViewModel(), IViewModel {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    override fun error(message: String) {
        _uiState.update {
            it.copy(
                loginState = StateType.FAILED(message = message)
            )
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    override fun clearState() {
        _uiState.value = AuthUiState()
    }

    fun loadLastLoginUser() {
        val lastLoginUser = tokenManager.getLastLoginUser()
        if (lastLoginUser != null) {
            _uiState.update {
                it.copy(
                    username = lastLoginUser.username,
                    fullName = lastLoginUser.fullName
                )
            }
        }

    }

    fun onLogout() {
        tokenManager.clearToken()
    }

    fun onDeleteLastLoginUser() {
        tokenManager.clearLastLoginUser()
        biometricManager.clear()
        _uiState.update {
            it.copy(
                username = "",
                fullName = null
            )
        }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(username = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update {
            it.copy(password = newPassword)
        }
    }

    fun checkEnableLogin(): Boolean {
        return uiState.value.username != "" && uiState.value.password != ""
    }

    fun onChangeVisiblePassword() {
        _uiState.update {
            it.copy(isPasswordShow = !it.isPasswordShow)
        }
    }

    fun onLoginClick(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(loginState = StateType.LOADING)
        }
        viewModelScope.launch {
            val loginRequest = LoginRequest(uiState.value.username, uiState.value.password)
            val apiResult = authRepository.login(loginRequest)
            when (apiResult) {
                is ApiResult.Success -> {
                    val loginResponse = apiResult.data
                    _uiState.update {
                        it.copy(loginState = StateType.SUCCESS)
                    }

                    tokenManager.updateToken(
                        access = loginResponse.access_token,
                        refresh = loginResponse.refresh_token
                    )

                    onSuccess()
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(loginState = StateType.FAILED(apiResult.message))

                    }
                    onError(apiResult.message)
                }
            }
        }
    }

    fun onBiometricClick(
        fragmentActivity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val isAllowBiometricAuthenticator = biometricManager.getBiometricKey() != null
        if (!isAllowBiometricAuthenticator) {
            onError("Vui lòng đăng nhập bằng tài khoản và mật khẩu trước")
            return
        }
        biometricAuthenticator.promptBiometricAuth(
            title = "Đăng nhập bằng sinh trắc học",
            negativeButtonText = "Cancel",
            fragmentActivity = fragmentActivity,
            onSucess = {
                loginViaBiometric(
                    onSuccess = onSuccess,
                    onError = onError
                )
            },
            onFailed = {
                onError("Xác thực không thành công")
            },
            onError = { errorCode, errorString ->
                onError(errorString)
            }
        )
    }

    private fun loginViaBiometric(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(loginState = StateType.LOADING)
        }
        viewModelScope.launch {
            val biometricPayload = biometricManager.getBiometricKey()
            if (biometricPayload == null) {
                _uiState.update {
                    it.copy(loginState = StateType.FAILED("Chưa đăng ký đăng nhập bằng sinh trắc học"))
                }
                onError("Chưa đăng ký đăng nhập bằng sinh trắc học")
                return@launch
            }
            val request = LoginViaBiometricRequest(
                biometricKey = biometricPayload.biometricKey,
                deviceId = biometricPayload.deviceId,
                username = biometricPayload.username,
            )
            val apiResult = authRepository.loginViaBiometric(request)
            when (apiResult) {
                is ApiResult.Success -> {
                    val loginResponse = apiResult.data
                    _uiState.update {
                        it.copy(loginState = StateType.SUCCESS)
                    }

                    tokenManager.updateToken(
                        access = loginResponse.access_token,
                        refresh = loginResponse.refresh_token
                    )
                    onSuccess()
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(loginState = StateType.FAILED(apiResult.message))

                    }
                    onError(apiResult.message)
                }
            }
        }
    }


}