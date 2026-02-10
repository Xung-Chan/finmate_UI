package com.example.ibanking_kltn.dtos.requests

import com.example.ibanking_kltn.dtos.definitions.SpendingRecordType
import java.math.BigDecimal
import java.util.UUID

data class SpendingSnapshotRequest(
    val snapshotName: String,
    val monthlySpending: String,
    val budgetAmount: BigDecimal
)

data class SpendingCategorySnapshotRequest(
    val categoryId: String,
    val budgetAmount: BigDecimal,
    val budgetName: String
)

data class SpendingRecordRequest(
    val username: String? = null,
    val transactionId: UUID,
    val spendingRecordType: SpendingRecordType,
    val categoryCode: String
)

data class DefinedSpendingCategoryRequest(
    val id: String? = null,
    val code: String,
    val systemCategoryId: String? = null,
    val name: String,
    val icon: String,
    val textColor: String,
    val backgroundColor: String
)
