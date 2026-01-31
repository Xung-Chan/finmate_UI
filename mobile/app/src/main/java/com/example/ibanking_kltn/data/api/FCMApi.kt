package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.dtos.requests.RegisterFcmRequest
import com.example.ibanking_kltn.dtos.responses.RegisterFcmResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


//for auth activity
interface FCMApi {

    @POST("/api/fcm/register")
    suspend fun registerFcm(
        @Body request: RegisterFcmRequest
    ): Response<RegisterFcmResponse>

}