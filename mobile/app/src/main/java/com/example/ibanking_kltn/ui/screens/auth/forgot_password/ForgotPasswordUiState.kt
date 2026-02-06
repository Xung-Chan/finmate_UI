package com.example.ibanking_kltn.ui.screens.auth.forgot_password

import com.example.ibanking_kltn.dtos.definitions.RequestOtpPurpose
import com.example.ibanking_kltn.ui.uistates.StateType

enum class ForgotPasswordStep {
    ENTER_USERNAME,
    ENTER_EMAIL,
    CONFIRM_OTP,
    RESET_PASSWORD
}


data class ForgotPasswordUiState(
    val screenState: StateType = StateType.NONE,
    val currentStep: ForgotPasswordStep = ForgotPasswordStep.ENTER_USERNAME,

    val purpose: RequestOtpPurpose=RequestOtpPurpose.PASSWORD_RESET,
    val maskedEmail:String="",
    val verifyKey:String="",
    val token: String ="",

    val username: String="",
    val email: String="",
    val otp: String="",
    val newPassword: String="",

    val isEnableResendOtp: Boolean=true,
)

