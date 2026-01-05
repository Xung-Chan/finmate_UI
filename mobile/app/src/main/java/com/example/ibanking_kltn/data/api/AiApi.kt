package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.data.dtos.responses.AnalyzeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AiApi {
    @GET("/api/wallets/me")
    suspend fun getAnalyze(
        @Query("analyzeRequestId") analyzeRequestId: String,
    ): Response<AnalyzeResponse>
}