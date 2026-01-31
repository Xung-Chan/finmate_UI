package com.example.ibanking_kltn.dtos.responses

import com.example.ibanking_kltn.dtos.definitions.BillingCycleStatus
import com.example.ibanking_kltn.dtos.definitions.PayLaterAccountStatus
import com.example.ibanking_kltn.dtos.definitions.PayLaterApplicationStatus
import com.example.ibanking_kltn.dtos.definitions.PayLaterApplicationType

data class PayLaterResponse(
    val approvedAt: String,
    val approvedBy: String,
    val availableCredit: Double,
    val creditLimit: Double,
    val id: String,
    val interestRate: Double,
    val nextBillingDate: String,
    val nextDueDate: String,
    val payLaterAccountNumber: String,
    val status: PayLaterAccountStatus,
    val usedCredit: Double,
    val username: String,
    val walletNumber: String?=null
)


data class PayLaterApplicationResponse(
    val id: String,
    val username: String,
    val type: PayLaterApplicationType,

    val requestedCreditLimit: Double,
    val approvedLimit: Double?,

    val reason: String?,
    val rejectionReason: String?,
    val status: PayLaterApplicationStatus,

    val approvedBy: String?,
    val appliedAt: String,
    val processedAt: String?
)

data class BillingCycleResonse(
    val code: String,

    val startDate: String,
    val endDate: String,
    val dueDate: String,

    val totalSpent: Double,        // đã sử dụng
    val paidPrincipal: Double,     // đã trả gốc
    val minimumPayment: Double,    // số tiền tối thiểu phải trả
    val totalInterest: Double,     // tổng lãi phải trả
    val paidInterest: Double,      // đã trả lãi
    val lateInterestRate: Double,  // lãi suất trễ hạn

    val penaltyFee: Double?,       // phí trễ hạn (có thể chưa áp dụng)
    val penaltyApplied: Boolean,       // đã áp dụng phí trễ hạn hay chưa

    val status: BillingCycleStatus
)
