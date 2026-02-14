package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.dtos.responses.AnalyzeResponse
import com.example.ibanking_kltn.dtos.responses.ExtractTransactionResponse
import com.example.ibanking_kltn.dtos.responses.SpendingAnalyzeResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface AiApi {
    @GET("/api/ai/analytics/")
    suspend fun getAnalyze(
        @Query("analyzeRequestId") analyzeRequestId: String,
    ): Response<AnalyzeResponse>

    @GET("/api/ai/analytics/spending")
    suspend fun getAnalyzeSpending(
        @Query("analyzeRequestId") analyzeRequestId: String,
    ): Response<SpendingAnalyzeResponse>

    @Multipart
    @POST("/api/ai/ocr/extract/transaction/file")
    suspend fun extractTransactionImage(
        @Part file: MultipartBody.Part
    ): Response<ExtractTransactionResponse>

}