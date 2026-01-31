package com.example.ibanking_kltn.dtos.requests

data class RegisterFcmRequest(
    val deviceId: String,
    val fcmToken: String,
    val deviceType: String,
)