package com.example.ibanking_kltn.data.dtos.responses

data class PrepareTransactionResponse(
    val retryTokenOtp: String,
    val transactionId: String
)

class AllExpenseTypeResponse : ArrayList<ExpenseType>()

data class ExpenseType(
    val id: String,
    val name: String,
    val tag: String
)