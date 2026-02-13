package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.EkycApi
import com.example.ibanking_kltn.data.exception.safeApiCall
import com.example.ibanking_kltn.dtos.requests.RegisterEkycRequest
import com.example.ibanking_kltn.dtos.requests.VerifyEkycRequest
import com.example.ibanking_kltn.dtos.responses.RegisterEkycResponse
import com.example.ibanking_kltn.dtos.responses.VerifyEkycResponse
import com.example.ibanking_soa.data.utils.ApiResult
import jakarta.inject.Inject

class EkycRepository @Inject constructor(
    private val ekycApi: EkycApi,
) {
    suspend fun registerEkyc(request: RegisterEkycRequest): ApiResult<RegisterEkycResponse> {
        return safeApiCall(
            apiCall = { ekycApi.registerEkyc(request = request) }
        )
    }

    suspend fun transactionVerify(request: VerifyEkycRequest): ApiResult<VerifyEkycResponse> {
        return safeApiCall(
            apiCall = { ekycApi.transactionVerify(request = request) }
        )
    }

}