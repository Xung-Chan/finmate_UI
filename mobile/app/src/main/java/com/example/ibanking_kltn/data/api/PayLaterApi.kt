package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.dtos.definitions.Pagination
import com.example.ibanking_kltn.dtos.requests.FilterBillingCyclesRequest
import com.example.ibanking_kltn.dtos.requests.FilterPayLaterApplicationRequest
import com.example.ibanking_kltn.dtos.requests.PayLaterApplicationRequest
import com.example.ibanking_kltn.dtos.responses.BillingCycleResonse
import com.example.ibanking_kltn.dtos.responses.PayLaterApplicationResponse
import com.example.ibanking_kltn.dtos.responses.PayLaterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface PayLaterApi {
    @GET("/api/pay-later/me")
    suspend fun getMyPaylater(
    ): Response<PayLaterResponse>


    @POST("/api/pay-later/applications")
    suspend fun submitApplication(
        @Body request: PayLaterApplicationRequest
    ): Response<Unit>


    @POST("/api/pay-later/applications/filter")
    suspend fun filterApplications(
        @Body request: FilterPayLaterApplicationRequest
    ): Response<Pagination<PayLaterApplicationResponse>>


    @POST("/api/pay-later/billing-cycles/filter")
    suspend fun filterBillingCycles(
        @Body request: FilterBillingCyclesRequest
    ): Response<Pagination<BillingCycleResonse>>


    @GET("/api/pay-later/billing-cycles")
    suspend fun getBillingCycles(
        @Query(value="billingCycleCode") billingCycleCode: String
    ): Response<BillingCycleResonse>
}