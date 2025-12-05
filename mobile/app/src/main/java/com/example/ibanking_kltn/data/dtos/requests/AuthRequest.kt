package com.example.ibanking_kltn.data.dtos.requests

data class LoginRequest(
    val username : String,
    val password : String
)

data class  RefreshTokenRequest(
    val refreshToken : String
)

data class ResetPasswordRequest(
    val email: String,
    val logoutAllDevices: Boolean,
    val newPassword: String,
    val token: String
)

data class SendOtpRequest(
    val email: String,
    val purpose: String,
    val verifyKey: String
)

data class RequestOtpRequest(
    val purpose: String,
    val username: String
)
data class ChangePasswordRequest(
    val logoutAllDevices: Boolean=false,
    val newPassword: String,
    val oldPassword: String
)
data class VerifyOtpRequest(
    val email: String,
    val otp: String,
    val purpose: String,
    val verifyKey: String
)