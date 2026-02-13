package com.example.ibanking_kltn.dtos.requests

data class RegisterEkycRequest(
    val cardId: String,
    val fullName: String,
    val dateOfBirth: String,
    val address: String,
)

data class VerifyEkycRequest(
    val transactionId: String,
    val faceData: String
)