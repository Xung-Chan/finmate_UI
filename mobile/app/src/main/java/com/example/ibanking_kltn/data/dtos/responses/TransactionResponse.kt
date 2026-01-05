package com.example.ibanking_kltn.data.dtos.responses

import com.example.ibanking_kltn.data.dtos.ServiceType

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

data class HandleVNPayReturnResponse(
    val transactionId: String
)


data class  TransactionHistoryResponse(
    val id: String,
    val amount: Double,
    val description: String,
    val processedAt: String,
    val sourceAccountNumber: String,
    val sourceBalanceUpdated: Double,
    val status: String,
    val toWalletNumber: String?,
    val transactionType: ServiceType
)
class TrendStatisticResponse : ArrayList<TrendStatisticResponseItem>()

data class TrendStatisticResponseItem(
    val date: String,
    val totalTransactions: Int,
    val totalValue: Long
)

data class DistributionStatisticResponse(
    val analyticId: String,
    val distributions: List<Distribution>
)

data class Distribution(
    val label: String,
    val expenseName: String,
    val expenseTag: String,
    val totalTransactions: Int,
    val totalValue: Long
)


