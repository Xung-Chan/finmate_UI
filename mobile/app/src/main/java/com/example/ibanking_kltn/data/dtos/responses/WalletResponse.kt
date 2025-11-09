package com.example.ibanking_kltn.data.dtos.responses

data class WalletResponse(
    val id: String,
    val mail: Any,
    val balance: Double?,
    val merchantName: String,
    val status: String,
    val username: String,
    val walletNumber: String
)
