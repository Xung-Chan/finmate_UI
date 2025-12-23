package com.example.ibanking_kltn.data.dtos.responses


data class LoginResponse(
    val access_token: String,
    val refresh_token: String,
    val id_token: String,
    val expires_in: Int
)

data class RequestOtpResponse(
    val maskedMail: String,
    val verifyKeyDurationMinutes: Long,
    val verifyKey: String
)

data class VerifyOtpResponse(

    val email: String,
    val resetPasswordToken: String
)

data class RegisterBiometricResponse(
    val biometricKey: String
)