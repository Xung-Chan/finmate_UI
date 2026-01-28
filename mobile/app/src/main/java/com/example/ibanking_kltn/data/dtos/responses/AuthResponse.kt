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

enum class UserStatusEnum {
    ACTIVE,
    UNACTIVE,
    LOCKED
}

data class UpdateAvatarResponse(

    val avatarUrl: String
)

data class UserInfoResponse(
    val id: String?,
    val username: String,
    val fullName: String,
    val mail: String,
    val status: UserStatusEnum,
    val phoneNumber: String,
    val avatarUrl: Any?,
    val dateOfBirth: String,
    val gender: String,
    val address: String,
    val cardId: String
)
