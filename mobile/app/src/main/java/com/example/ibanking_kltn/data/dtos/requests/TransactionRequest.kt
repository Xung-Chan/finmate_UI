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

data class DepositTransactionRequest(
    val amount: Long
)

data class FilterTransactionRequest(
    val fromDate: String,
    val toDate: String,
    val accountType: String? = null,
    val status: String? = null,
    val type: String? = null,
    val sortBy: String = "processed_at_desc",
    val page: Int? = 0,
    val size: Int? = 10,
)

data class FilterTransactionPara(
    val fromDate: String,
    val toDate: String,
    val accountType: String? = null,
    val status: String? = null,
    val type: String? = null,
    val sortBy: String = "processed_at_desc",
)