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
    val username: String? = null,
    val accountType:String?=null,
    val status: String? = null,
    val type: String? = null,
    val fromDate: String? = null,
    val toDate: String? = null,
    val page: Int? = 0,
    val size: Int? = 10,
    val sortBy:String?=null, //created_at_desc
//    val accountType;
//ActionBy actionBy;
)