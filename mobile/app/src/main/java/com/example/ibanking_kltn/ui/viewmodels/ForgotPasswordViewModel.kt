package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.RequestOtpPurpose
import com.example.ibanking_kltn.data.dtos.requests.RequestOtpRequest
import com.example.ibanking_kltn.data.dtos.requests.ResetPasswordRequest
import com.example.ibanking_kltn.data.dtos.requests.SendOtpRequest
import com.example.ibanking_kltn.data.dtos.requests.VerifyOtpRequest
import com.example.ibanking_kltn.data.repositories.AuthRepository
import com.example.ibanking_kltn.ui.event.ForgotPasswordEffect
import com.example.ibanking_kltn.ui.event.ForgotPasswordEvent
import com.example.ibanking_kltn.ui.uistates.ForgotPasswordStep
import com.example.ibanking_kltn.ui.uistates.ForgotPasswordUiState
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val savedStateHandle: SavedStateHandle

) : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ForgotPasswordEffect>()
    val uiEffect = _uiEffect.asSharedFlow()


    init {
        val purpose = savedStateHandle.get<String>("purpose")?.let {
            RequestOtpPurpose.valueOf(it)
        }
        if (purpose == null) {
            viewModelScope.launch {
                _uiEffect.emit(
                    ForgotPasswordEffect.WrongPurpose
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    purpose = purpose
                )
            }
        }
    }

    private suspend fun onError(message: String) {
        _uiEffect.emit(
            ForgotPasswordEffect.ShowSnackBar(
                snackBar = SnackBarUiState(
                    message,
                    SnackBarType.ERROR
                )
            )
        )
    }

    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            ForgotPasswordEvent.BackToEnterEmailClick -> onBackToEnterEmailClick()
            ForgotPasswordEvent.BackToEnterUsernameClick -> onBackToEnterUsernameClick()
            is ForgotPasswordEvent.ChangeEmail -> onEmailChange(event.email)
            is ForgotPasswordEvent.ChangeNewPassword -> onNewPasswordChange(event.newPassword)
            is ForgotPasswordEvent.ChangeOtp -> onOtpChange(event.otp)
            is ForgotPasswordEvent.ChangeUsername -> onUsernameChange(event.username)
            ForgotPasswordEvent.ConfirmOtp -> onConfirmOtpClick()
            ForgotPasswordEvent.FindByUsername-> onFindUsernameClick()
            ForgotPasswordEvent.ResetPassword -> onResetPasswordClick()
            ForgotPasswordEvent.SendOtp -> onSendOtpClick()
        }
    }

    fun clearState() {
        _uiState.value = ForgotPasswordUiState()
    }


    private fun onFindUsernameClick(
    ) {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            val request = RequestOtpRequest(
                purpose = uiState.value.purpose.name,
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

    private fun onUsernameChange(username: String) {
        _uiState.update {
            it.copy(
                username = username
            )
        }
    }

    private fun onSendOtpClick() {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            val request = SendOtpRequest(
                verifyKey = uiState.value.verifyKey,
                email = uiState.value.email,
                purpose = uiState.value.purpose.name
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


    private fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email
            )
        }
    }

    private fun onConfirmOtpClick(
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
                purpose = uiState.value.purpose.name,
            )
            val apiResult = authRepository.verifyOtp(
                request = request
            )
            when (apiResult) {
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.FAILED(apiResult.message)
                        )
                    }
                    onError(apiResult.message)
                }

                is ApiResult.Success -> {
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

    private fun onOtpChange(otp: String) {
        _uiState.update {
            it.copy(
                otp = otp
            )
        }
    }

    private fun onResetPasswordClick(
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
            when (apiResult) {
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.FAILED(apiResult.message)
                        )
                    }
                    onError(apiResult.message)
                }

                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS
                        )
                    }
                    _uiEffect.emit(
                        ForgotPasswordEffect.ResetPasswordSuccess
                    )
                }
            }
        }
    }

    private fun onNewPasswordChange(newPassword: String) {
        _uiState.update {
            it.copy(
                newPassword = newPassword
            )
        }
    }

    private fun onBackToEnterEmailClick() {
        _uiState.update {
            it.copy(
                currentStep = ForgotPasswordStep.ENTER_EMAIL
            )
        }
    }

    private fun onBackToEnterUsernameClick() {
        _uiState.update {
            it.copy(
                currentStep = ForgotPasswordStep.ENTER_USERNAME
            )
        }
    }


}