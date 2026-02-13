package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.dtos.requests.RegisterEkycRequest
import com.example.ibanking_kltn.dtos.requests.VerifyEkycRequest
import com.example.ibanking_kltn.dtos.responses.RegisterEkycResponse
import com.example.ibanking_kltn.dtos.responses.VerifyEkycResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface EkycApi {
    @POST("/api/ekyc/register")
    suspend fun registerEkyc(
        @Body request: RegisterEkycRequest
    ): Response<RegisterEkycResponse>


    @POST("/api/ekyc/transactions/verify")
    suspend fun transactionVerify(
        @Body request: VerifyEkycRequest
    ): Response<VerifyEkycResponse>


}
