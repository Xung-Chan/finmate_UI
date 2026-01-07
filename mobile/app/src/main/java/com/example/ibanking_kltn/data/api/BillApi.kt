package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.data.dtos.Pagination
import com.example.ibanking_kltn.data.dtos.requests.CancelBillRequest
import com.example.ibanking_kltn.data.dtos.requests.CreateBillRequest
import com.example.ibanking_kltn.data.dtos.requests.FilterBillRequest
import com.example.ibanking_kltn.data.dtos.requests.PreparePayBillRequest
import com.example.ibanking_kltn.data.dtos.requests.PreparePrePaymentRequest
import com.example.ibanking_kltn.data.dtos.responses.BillResponse
import com.example.ibanking_kltn.data.dtos.responses.PrepareTransactionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface BillApi {

    @POST("/api/bills/")
    suspend fun createBill(
        @Body request: CreateBillRequest
    ): Response<BillResponse>

    @GET("/api/bills/")
    suspend fun getBillInfo(
        @Query(value="qrCode") qrCode: String
    ): Response<BillResponse>

    @POST("/api/bills/prepare-payment")
    suspend fun preparePayBill(
        @Body request: PreparePayBillRequest
    ): Response<PrepareTransactionResponse>

    @POST("/api/bills/prepare-repayment")
    suspend fun preparePrepayment(
        @Body request: PreparePrePaymentRequest
    ): Response<PrepareTransactionResponse>

    @POST("/api/bills/cancel")
    suspend fun cancelBill(
        @Body request: CancelBillRequest
    ): Response<Unit>


    @POST("/api/bills/filter")
    suspend fun filterBill(
        @Body request: FilterBillRequest
    ): Response<Pagination<BillResponse>>


}