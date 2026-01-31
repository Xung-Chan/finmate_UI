package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.FCMApi
import com.example.ibanking_kltn.data.exception.safeApiCall
import com.example.ibanking_kltn.dtos.requests.RegisterFcmRequest
import com.example.ibanking_kltn.dtos.responses.RegisterFcmResponse
import com.example.ibanking_soa.data.utils.ApiResult
import jakarta.inject.Inject

class FCMRepository @Inject constructor(
    private val fcmApi: FCMApi
) {
    suspend fun registerFcm(request: RegisterFcmRequest): ApiResult<RegisterFcmResponse> {
        return safeApiCall(
            apiCall = {
                fcmApi.registerFcm(
                    request = request
                )
            }
        )
    }


}