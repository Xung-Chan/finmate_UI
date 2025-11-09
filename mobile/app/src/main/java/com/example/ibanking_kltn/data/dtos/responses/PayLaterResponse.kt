package com.example.ibanking_kltn.data.dtos.responses

data class PayLaterResponse(
    val approvedAt: Any,
    val approvedBy: Any,
    val availableCredit: Double?,
    val creditLimit: Double,
    val id: String,
    val interestRate: Double,
    val nextBillingDate: Any,
    val nextDueDate: Any,
    val payLaterAccountNumber: String,
    val status: String,
    val usedCredit: Double,
    val username: String,
    val walletNumber: Any
)