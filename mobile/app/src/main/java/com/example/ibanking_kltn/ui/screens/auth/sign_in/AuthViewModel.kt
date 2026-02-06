package com.example.ibanking_kltn.ui.screens.auth.sign_in

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.di.BiometricManager
import com.example.ibanking_kltn.data.di.TokenManager
import com.example.ibanking_kltn.data.repositories.AuthRepository
import com.example.ibanking_kltn.dtos.definitions.RequestOtpPurpose
import com.example.ibanking_kltn.dtos.requests.LoginRequest
import com.example.ibanking_kltn.dtos.requests.LoginViaBiometricRequest
import com.example.ibanking_kltn.ui.security.BiometricAuthenticator
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val biometricManager: BiometricManager,
    private val biometricAuthenticator: BiometricAuthenticator,
) : ViewModel() {


    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<LoginEffect>()
    val uiEffect: SharedFlow<LoginEffect> = _uiEffect.asSharedFlow()

    init {
        loadLastLoginUser()
    }
    fun onEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.DeleteLastestLogin -> onDeleteLastLoginUser()
            LoginEvent.Login -> onLoginClick()
            is LoginEvent.LoginBiometric -> onBiometricClick(event.fragmentActivity)
            is LoginEvent.ChangePassword -> onPasswordChange(event.password)
            is LoginEvent.ChangeUsername -> onEmailChange(event.username)
            is LoginEvent.RequestOtp -> onRequestOtp(event.purpose)
        }
    }

    fun clearState() {
        _uiState.value = AuthUiState()
    }

    private suspend fun onError(message: String) {
        _uiEffect.emit(
            LoginEffect.ShowSnackBar(
                SnackBarUiState(
                    message = message,
                    type = SnackBarType.ERROR
                )
            )
        )
    }

    private fun loadLastLoginUser() {
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

    private fun onRequestOtp(purpose: RequestOtpPurpose) {
        viewModelScope.launch {
            _uiEffect.emit(
                LoginEffect.RequestOtp(
                    purpose = purpose,
                )
            )
        }
    }

    private fun onDeleteLastLoginUser() {
        tokenManager.clearLastLoginUser()
        biometricManager.clear()
        _uiState.update {
            it.copy(
                username = "",
                fullName = null
            )
        }
    }

    private fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(username = newEmail) }
    }

    private fun onPasswordChange(newPassword: String) {
        _uiState.update {
            it.copy(password = newPassword)
        }
    }


    private fun onLoginClick(
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
                    _uiEffect.emit(
                        LoginEffect.LoginSuccess
                    )
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

    private fun onBiometricClick(
        fragmentActivity: FragmentActivity,
    ) {
        val isAllowBiometricAuthenticator = biometricManager.getBiometricKey() != null
        if (!isAllowBiometricAuthenticator) {
            viewModelScope.launch {
                onError("Chưa kích hoạt đăng nhập bằng sinh trắc học")
            }
            return
        }
        biometricAuthenticator.promptBiometricAuth(
            title = "Đăng nhập bằng sinh trắc học",
            negativeButtonText = "Cancel",
            fragmentActivity = fragmentActivity,
            onSucess = {
                loginViaBiometric()
            },
            onFailed = {
                viewModelScope.launch {

                    onError("Xác thực không thành công")
                }
            },
            onError = { errorCode, errorString ->
                viewModelScope.launch {
                    onError(errorString)
                }
            }
        )
    }

    private fun loginViaBiometric(
    ) {
        _uiState.update {
            it.copy(loginState = StateType.LOADING)
        }
        viewModelScope.launch {
            val biometricPayload = biometricManager.getBiometricKey()
            if (biometricPayload == null) {
                _uiState.update {
                    it.copy(loginState = StateType.FAILED("Chưa kích hoạt đăng nhập bằng sinh trắc học"))
                }
                onError("Chưa kích hoạt đăng nhập bằng sinh trắc học")
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
                    _uiEffect.emit(
                        LoginEffect.LoginSuccess
                    )
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