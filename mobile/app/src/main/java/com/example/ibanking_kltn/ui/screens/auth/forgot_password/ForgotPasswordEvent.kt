package com.example.ibanking_kltn.ui.screens.auth.forgot_password

import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class ForgotPasswordEvent {
    data class ChangeUsername(val username: String) : ForgotPasswordEvent()
    data class ChangeEmail(val email: String) : ForgotPasswordEvent()
    data class ChangeOtp(val otp: String) : ForgotPasswordEvent()
    data class ChangeNewPassword(val newPassword: String) : ForgotPasswordEvent()

    object ConfirmOtp : ForgotPasswordEvent()
    object FindByUsername : ForgotPasswordEvent()
    object SendOtp : ForgotPasswordEvent()
    object ResetPassword : ForgotPasswordEvent()
    object BackToEnterEmailClick : ForgotPasswordEvent()
    object BackToEnterUsernameClick : ForgotPasswordEvent()
}

sealed class ForgotPasswordEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : ForgotPasswordEffect()
    object WrongPurpose : ForgotPasswordEffect()
    object ResetPasswordSuccess : ForgotPasswordEffect()
}