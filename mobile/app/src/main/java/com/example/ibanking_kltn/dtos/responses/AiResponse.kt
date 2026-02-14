package com.example.ibanking_kltn.dtos.responses

data class AnalyzeResponse(
    val canh_bao: String,
    val ty_trong: String,
    val xu_huong: String
)
data class  SpendingAnalyzeResponse(
    val alert: String,
    val categoryRatio: String,
    val trend: String
)
data class ExtractTransactionResponse(
    val amount: Long,
    val destinationAccountName: String,
    val destinationAccountNumber: String,
    val externalBankName: String,
    val transactionDateTime: String,
    val transactionDescription: String
)