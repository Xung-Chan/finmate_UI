package com.example.ibanking_kltn.data.dtos.requests

data class CreateBillRequest(
    val amount: Long,
    val description: String,
    val dueDate: String,
    val expenseTypeId: String
)
data class PreparePayBillRequest(
    val accountType: String,
    val billerCode: String
)
data class PreparePrePaymentRequest(
    val amount: Int,
    val billerCode: String
)
data class CancelBillRequest(
    val qrIdentifier: String
)