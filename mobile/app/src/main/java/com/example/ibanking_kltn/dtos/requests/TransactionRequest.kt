package com.example.ibanking_kltn.dtos.requests

import com.example.ibanking_kltn.dtos.definitions.AccountType
import com.example.ibanking_kltn.dtos.definitions.ServiceType
import com.example.ibanking_kltn.dtos.definitions.SortOption
import com.example.ibanking_kltn.dtos.definitions.TransactionStatus
import java.time.LocalDate

data class PrepareTransferRequest(
    val accountType: String,
    val amount: Long,
    val description: String,
    val toWalletNumber: String,
    val expenseTypeId: String? = null
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
    val accountType: String,
    val status: String? = null,
    val type: String? = null,
    val sortBy: String = "processed_at_desc",
    val page: Int? = 0,
    val size: Int? = 10,
)

data class FilterTransactionPara(
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val accountType: AccountType? = AccountType.WALLET,
    val status: TransactionStatus? = null,
    val type: ServiceType? = null,
    val sortBy: SortOption = SortOption.NEWEST,
)

data class TrendStatisticRequest(
    val moneyFlowType: String,
    val year: Int
)

data class DistributionStatisticRequest(
    val referenceDate: String
)