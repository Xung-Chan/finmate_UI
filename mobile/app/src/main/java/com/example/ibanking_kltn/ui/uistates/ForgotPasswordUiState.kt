package com.example.ibanking_kltn.ui.uistates

enum class ForgotPasswordStep {
    ENTER_USERNAME,
    ENTER_EMAIL,
    CONFIRM_OTP,
    RESET_PASSWORD
}


data class ForgotPasswordUiState(
    val screenState: StateType = StateType.NONE,
    val currentStep: ForgotPasswordStep = ForgotPasswordStep.ENTER_USERNAME,

    val maskedEmail:String="",
    val verifyKey:String="",
    val token: String ="",

    val username: String="",
    val email: String="",
    val otp: String="",
    val newPassword: String="",

    val isShowPassword:Boolean=false,
    val isEnableResendOtp: Boolean=true,
)

