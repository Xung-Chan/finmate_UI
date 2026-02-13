package com.example.ibanking_kltn.dtos.responses

data class RegisterEkycResponse(
    val message: String
)

data class VerifyEkycResponse(
    val ekycTransactionId: String,
    val ekycKey: String
)
