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
    }


}