package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.ui.uistates.ForgotPasswordStep
import com.example.ibanking_kltn.ui.uistates.ForgotPasswordUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel(), IViewModel {
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()
    override fun error(message: String) {
        _uiState.update {
            it.copy(
                screenState = StateType.FAILED(message = message)
            )
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun clearState() {
        _uiState.value = ForgotPasswordUiState()
    }

    fun onFindUsernameClick() {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            delay(2000)
            _uiState.update {
                it.copy(
                    screenState = StateType.SUCCESS,
                    currentStep = ForgotPasswordStep.ENTER_EMAIL,
                    email = ""
                )
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

    fun onSendOtpClick() {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            delay(2000)
            _uiState.update {
                it.copy(
                    screenState = StateType.SUCCESS,
                    currentStep = ForgotPasswordStep.CONFIRM_OTP,
                    otp = ""
                )
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

    fun onConfirmOtpClick() {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            delay(2000)
            _uiState.update {
                it.copy(
                    screenState = StateType.SUCCESS,
                    currentStep = ForgotPasswordStep.RESET_PASSWORD,
                    newPassword = ""
                )
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
        onSuccess: () -> Unit
    ) {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            delay(2000)
            Toast.makeText(context, "Password reset successfully", Toast.LENGTH_SHORT).show()
            onSuccess()
        }
    }

    fun onNewPasswordChange(newPassword: String) {
        _uiState.update {
            it.copy(
                newPassword = newPassword
            )
        }
    }

    fun onBackToEnterEmailClick(){
        _uiState.update {
            it.copy(
                currentStep = ForgotPasswordStep.ENTER_EMAIL
            )
        }
    }

    fun onBackToEnterUsernameClick(){
        _uiState.update {
            it.copy(
                currentStep = ForgotPasswordStep.ENTER_USERNAME
            )
        }
    }


}