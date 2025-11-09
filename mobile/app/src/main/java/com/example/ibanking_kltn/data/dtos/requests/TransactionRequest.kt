package com.example.ibanking_kltn.data.dtos.requests

data class PrepareTransferRequest(
    val accountType: String,
    val amount: Long,
    val description: String,
    val toWalletNumber: String
)
data class ConfirmTransferRequest(
    val otp: String,
    val transactionId: String
)