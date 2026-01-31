package com.example.ibanking_kltn.dtos.responses

import com.example.ibanking_kltn.dtos.definitions.BillStatus

data class BillResponse(
    val amount: Long,
    val billStatus: BillStatus,
    val description: String,
    val dueDate: String,
    val merchantName: String,
    val metadata: Any,
    val qrIdentifier: String,
    val walletNumber: String
)



data class BillingCycleResponse(
    val code: String,
    val dueDate: String,
    val endDate: String,
    val lateInterestRate: Double,
    val minimumPayment: Double,
    val paidInterest: Double,
    val paidPrincipal: Double,
    val penaltyApplied: Boolean,
    val penaltyFee: Double,
    val startDate: String,
    val totalInterest: Double,
    val totalSpent: Double
)

//data class FilterBillResponse(
//    val contents: List<BillResponse>,
//    val currentPage: Int,
//    val pageSize: Int,
//    val totalElements: Int,
//    val totalPages: Int
//)
