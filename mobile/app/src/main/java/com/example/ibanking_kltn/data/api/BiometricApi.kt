package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.dtos.requests.RegisterBiometricRequest
import com.example.ibanking_kltn.dtos.responses.RegisterBiometricResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


//for auth activity
interface BiometricApi {

    @POST("/api/biometric/register")
    suspend fun registerBiometric(
        @Body request: RegisterBiometricRequest
    ): Response<RegisterBiometricResponse>

    @POST("/api/biometric/cancel")
    suspend fun cancelBiometric(
        @Body request: RegisterBiometricRequest
    ): Response<Unit>


}