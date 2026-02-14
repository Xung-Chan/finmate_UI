package com.example.ibanking_kltn.dtos.responses

import android.os.Parcelable
import com.example.ibanking_kltn.dtos.definitions.SpendingRecordType
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

data class SpendingSnapshotResponse(
    val id: String,
    val monthlySpending: String,
    val snapshotName: String,
    val description: String,
    val usedAmount: BigDecimal,
    val budgetAmount: BigDecimal
)

data class SpendingSnapshotDetailResponse(
    val id: String,
    val monthlySpending: String,
    val snapshotName: String,
    val usedAmount: BigDecimal,
    val budgetAmount: BigDecimal,
    val spendingCategories: List<SpendingCategoryDetailResponse>
)


@Parcelize
data class SpendingRecordResponse(
    val id: String?,
    val snapshotId: String?,
    val transactionId: String?,
    val amount: BigDecimal,
    val description: String,
    val destinationAccountName: String,
    val destinationAccountNumber: String,
    val recordType: SpendingRecordType?,
    val categoryCode: String?,
    val categoryName: String?,
    val categoryIcon: String?,
    val categoryTextColor: String?,
    val categoryBackgroundColor: String?,
    val occurredAt: String
) : Parcelable

data class DefinedSpendingCategoryResponse(
    val id: String,
    val name: String,
    val code: String,
    val icon: String,
    val textColor: String,
    val backgroundColor: String,
    val systemCategoryCode: String,
    val systemCategoryName: String
)

data class SpendingCategoryDetailResponse(
    val categoryId:String,
    val categoryName: String,
    val categoryCode: String,
    val categoryIcon: String,
    val textColor: String,
    val backgroundColor: String,
    val budgetAmount: BigDecimal,
    val usedAmount: BigDecimal
)