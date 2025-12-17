package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.requests.RequestOtpRequest
import com.example.ibanking_kltn.data.dtos.requests.ResetPasswordRequest
import com.example.ibanking_kltn.data.dtos.requests.SendOtpRequest
import com.example.ibanking_kltn.data.dtos.requests.VerifyOtpRequest
import com.example.ibanking_kltn.data.repositories.AuthRepository
import com.example.ibanking_kltn.ui.uistates.ForgotPasswordStep
import com.example.ibanking_kltn.ui.uistates.ForgotPasswordUiState
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
class ForgotPasswordViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()


    fun clearState() {
        _uiState.value = ForgotPasswordUiState()
    }

    fun  onChangeVisiblePassword(){
        _uiState.update {
            it.copy(
                isShowPassword = !uiState.value.isShowPassword
            )
        }
    }

    fun onFindUsernameClick(
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            val request = RequestOtpRequest(
                purpose = "PASSWORD_RESET",
                username = uiState.value.username
            )
            val apiResult = authRepository.requestOtp(request)

            when (apiResult) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS,
                            currentStep = ForgotPasswordStep.ENTER_EMAIL,
                            maskedEmail = apiResult.data.maskedMail,
                            verifyKey = apiResult.data.verifyKey
                        )
                    }
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

    fun onUsernameChange(username: String) {
        _uiState.update {
            it.copy(
                username = username
            )
        }
    }

    fun onSendOtpClick(onError: (String) -> Unit) {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            val request = SendOtpRequest(
                verifyKey = uiState.value.verifyKey,
                email = uiState.value.email,
                purpose = "PASSWORD_RESET"
            )
            val apiResult = authRepository.sendOtp(
                request = request
            )

            when (apiResult) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS,
                            currentStep = ForgotPasswordStep.CONFIRM_OTP,
                        )
                    }
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


    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email
            )
        }
    }

    fun onConfirmOtpClick(
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            val request = VerifyOtpRequest(
                verifyKey = uiState.value.verifyKey,
                email = uiState.value.email,
                otp = uiState.value.otp,
                purpose = "PASSWORD_RESET",
            )
            val apiResult = authRepository.verifyOtp(
                request = request
            )
            when(apiResult){
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.FAILED(apiResult.message)
                        )
                    }
                    onError(apiResult.message)
                }
                is ApiResult.Success->{
                    val response = apiResult.data
                    _uiState.update {
                        it.copy(
                            token = response.resetPasswordToken,
                            screenState = StateType.SUCCESS,
                            currentStep = ForgotPasswordStep.RESET_PASSWORD
                        )
                    }
                }
            }
        }
    }

    fun onOtpChange(otp: String) {
        _uiState.update {
            it.copy(
                otp = otp
            )
        }
    }

    fun onResetPasswordClick(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            val request = ResetPasswordRequest(
                email = uiState.value.email,
                logoutAllDevices = false,
                newPassword = uiState.value.newPassword,
                token = uiState.value.token
            )
            
            val apiResult = authRepository.resetPassword(
                request = request
            )
            when(apiResult){
                is ApiResult.Error -> {
                    onError(apiResult.message)
                }
                is ApiResult.Success->{
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS
                        )
                    }
                    onSuccess()
                }
            }
        }
    }

    fun onNewPasswordChange(newPassword: String) {
        _uiState.update {
            it.copy(
                newPassword = newPassword
            )
        }
    }

    fun onBackToEnterEmailClick() {
        _uiState.update {
            it.copy(
                currentStep = ForgotPasswordStep.ENTER_EMAIL
            )
        }
    }

    fun onBackToEnterUsernameClick() {
        _uiState.update {
            it.copy(
                currentStep = ForgotPasswordStep.ENTER_USERNAME
            )
        }
    }


}