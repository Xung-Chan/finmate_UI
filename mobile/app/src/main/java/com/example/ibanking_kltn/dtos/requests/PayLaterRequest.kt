package com.example.ibanking_kltn.dtos.requests

import com.example.ibanking_kltn.dtos.definitions.PayLaterApplicationStatus
import com.example.ibanking_kltn.dtos.definitions.PayLaterApplicationType
import java.time.LocalDate

data class PayLaterApplicationRequest(
    val requestedCreditLimit: Long =5000000L,
    val type: String,
    val reason: String? = null,
)

data class FilterPayLaterApplicationRequest(
    val status: String? = null,
    val type: String? = null,
    val fromDate: String,
    val toDate: String,
    val page: Int? = 0,
    val size: Int? = 10,
)

data class FilterBillingCyclesRequest(
    val sortBy: String
)


data class FilterPayLaterApplicationPara(
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val status: PayLaterApplicationStatus? = null,
    val type: PayLaterApplicationType? = null,
)

