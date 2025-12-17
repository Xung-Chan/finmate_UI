package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.PayLaterApi
import com.example.ibanking_kltn.data.dtos.responses.PayLaterResponse
import com.example.ibanking_kltn.ui.exception.safeApiCall
import com.example.ibanking_soa.data.utils.ApiResult
import jakarta.inject.Inject

class PayLaterRepository @Inject constructor(
    private val payLaterApi: PayLaterApi
) {
    suspend fun getMyPayLater(): ApiResult<PayLaterResponse> {
        return safeApiCall(
            apiCall = { payLaterApi.getMyPaylater() }
        )
//        return ApiResult.Success(
//            data = PayLaterResponse(
//                approvedAt = "2025-01-12T10:35:00Z",
//                approvedBy = "AdminSystem",
//                availableCredit = 1500000.0,
//                creditLimit = 5000000.0,
//                id = "PL-ACC-123456",
//                interestRate = 2.5,
//                nextBillingDate = "2025-02-01",
//                nextDueDate = "2025-02-10",
//                payLaterAccountNumber = "9990011223344",
//                status = "ACTIVE",
//                usedCredit = 3500000.0,
//                username = "xungchan",
//                walletNumber = "WLT-556677"
//            )
//        )
    }


}