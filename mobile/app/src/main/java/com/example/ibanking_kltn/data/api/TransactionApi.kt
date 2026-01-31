package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.dtos.definitions.Pagination
import com.example.ibanking_kltn.dtos.requests.ConfirmTransferRequest
import com.example.ibanking_kltn.dtos.requests.DepositTransactionRequest
import com.example.ibanking_kltn.dtos.requests.DistributionStatisticRequest
import com.example.ibanking_kltn.dtos.requests.FilterTransactionRequest
import com.example.ibanking_kltn.dtos.requests.PrepareTransferRequest
import com.example.ibanking_kltn.dtos.requests.TrendStatisticRequest
import com.example.ibanking_kltn.dtos.responses.AllExpenseTypeResponse
import com.example.ibanking_kltn.dtos.responses.DepositTransactionResponse
import com.example.ibanking_kltn.dtos.responses.DistributionStatisticResponse
import com.example.ibanking_kltn.dtos.responses.PrepareTransactionResponse
import com.example.ibanking_kltn.dtos.responses.TransactionHistoryResponse
import com.example.ibanking_kltn.dtos.responses.TransactionResponse
import com.example.ibanking_kltn.dtos.responses.TrendStatisticResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface TransactionApi {


    @POST("/api/transactions/transfer/pending")
    suspend fun prepareTransfer(
        @Body request: PrepareTransferRequest
    ): Response<PrepareTransactionResponse>

    @POST("/api/transactions/transfer/confirm")
    suspend fun confirmTransfer(
        @Body request: ConfirmTransferRequest
    ): Response<TransactionResponse>

    @GET("/api/expense-types/")
    suspend fun getAllExpenseType(
    ): Response<AllExpenseTypeResponse>


    @POST("/api/transactions/gateway-deposit/")
    suspend fun createDepositTransaction(
        @Body request: DepositTransactionRequest

    ): Response<DepositTransactionResponse>


    @GET("/api/transactions/gateway-deposit/vnpay-return")
    suspend fun handleVNPayReturn(
        @Query("vnp_ResponseCode") vnp_ResponseCode: String,
        @Query("vnp_TxnRef") vnp_TxnRef: String,
    ): Response<TransactionResponse>


    @GET("/api/transactions/{transactionId}")
    suspend fun getTransactionStatus(
        @Path ("transactionId") transactionId: String,
    ): Response<TransactionHistoryResponse>


    @POST("/api/transactions/filter")
    suspend fun getTransactionHistory(
       @Body request : FilterTransactionRequest
    ): Response<Pagination<TransactionHistoryResponse>>


    @POST("/api/transactions/statistics/trends")
    suspend fun getTrendStatistic(
       @Body request : TrendStatisticRequest
    ): Response<TrendStatisticResponse>

    @POST("/api/transactions/statistics/distribution")
    suspend fun getDistributionStatistic(
       @Body request : DistributionStatisticRequest
    ): Response<DistributionStatisticResponse>





}