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

data class DepositTransactionResponse(
    val url: String
)

data class  TransactionHistoryResponse(
    val id: String,
    val amount: Double,
    val description: String,
    val destinationBalanceUpdated: Any?,
    val metadata: Any?,
    val processedAt: String,
    val sourceAccountNumber: String,
    val sourceBalanceUpdated: Any?,
    val status: String,
    val toWalletNumber: Any?
)