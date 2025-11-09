package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.data.dtos.requests.ConfirmTransferRequest
import com.example.ibanking_kltn.data.dtos.requests.PrepareTransferRequest
import com.example.ibanking_kltn.data.dtos.responses.AllExpenseTypeResponse
import com.example.ibanking_kltn.data.dtos.responses.PrepareTransactionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface TransactionApi {


    @POST("/api/transactions/transfer/pending")
    suspend fun prepareTransfer(
        @Body request: PrepareTransferRequest
    ): Response<PrepareTransactionResponse>

    @POST("/api/transactions/transfer/confirm")
    suspend fun confirmTransfer(
        @Body request: ConfirmTransferRequest
    ): Response<Unit>

    @GET("/api/expense-types/")
    suspend fun getAllExpenseType(
    ): Response<AllExpenseTypeResponse>


}