package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.di.TokenManager
import com.example.ibanking_kltn.data.dtos.requests.ChangePasswordRequest
import com.example.ibanking_kltn.data.repositories.AuthRepository
import com.example.ibanking_kltn.ui.uistates.ChangePasswordUiState
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
class ChangPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()


    fun clearState() {
        _uiState.value = ChangePasswordUiState()
    }

    fun onChangeOldPassword(newOldPassword: String) {
        _uiState.update { it.copy(oldPassword = newOldPassword) }
    }

    fun onChangeNewPassword(newNewPassword: String) {
        _uiState.update {
            it.copy(newPassword = newNewPassword)
        }

        if (Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,16}\$").matches(
                uiState.value.newPassword
            )
        ) {
            _uiState.update {
                it.copy(
                    isValidNewPassword = true
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isValidNewPassword = false
                )
            }
        }
    }


    fun isEnableChangePassword(): Boolean {
        return uiState.value.oldPassword.isNotEmpty() &&
                uiState.value.newPassword.isNotEmpty() &&
                uiState.value.isValidNewPassword
    }

    fun onConfirmChangePassword(
        onChangeSuccess: () -> Unit, //log out
        onError: (String) -> Unit
    ) {

        _uiState.update {
            it.copy(screenState = StateType.LOADING)
        }
        viewModelScope.launch {
            val request = ChangePasswordRequest(
                oldPassword = uiState.value.oldPassword,
                newPassword = uiState.value.newPassword
            )
            val apiResult = authRepository.changePassword(request = request)

            when (apiResult) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS
                        )
                    }
                    tokenManager.clearToken()
                    onChangeSuccess()
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.FAILED(apiResult.message)
                        )
                    }
                    onError(apiResult.message)
                }
            }
        }
    }

    fun onChangeVisibilityOldPassword() {
        _uiState.update {
            it.copy(isShowOldPassword = !it.isShowOldPassword)
        }
    }

    fun onChangeVisibilityNewPassword() {
        _uiState.update {
            it.copy(isShowNewPassword = !it.isShowNewPassword)
        }
    }
}