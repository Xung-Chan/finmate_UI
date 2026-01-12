package com.example.ibanking_kltn.data.dtos.requests

import com.example.ibanking_kltn.data.dtos.BillStatus

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
    val amount: Long,
    val billerCode: String
)

data class CancelBillRequest(
    val qrIdentifier: String
)

data class FilterBillRequest(
    val status: String? = null,
    val sortBy: String = "date_desc",
    val page: Int? = 0,
    val size: Int? = 10,
)


data class FilterBillParam(
    val status: BillStatus? =null,
    val sortBy: String = "date_desc"
)