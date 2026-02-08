package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.dtos.definitions.Pagination
import com.example.ibanking_kltn.dtos.definitions.SpendingRecordType
import com.example.ibanking_kltn.dtos.requests.DefinedSpendingCategoryRequest
import com.example.ibanking_kltn.dtos.requests.SpendingCategorySnapshotRequest
import com.example.ibanking_kltn.dtos.requests.SpendingRecordRequest
import com.example.ibanking_kltn.dtos.requests.SpendingSnapshotRequest
import com.example.ibanking_kltn.dtos.responses.DefinedSpendingCategoryResponse
import com.example.ibanking_kltn.dtos.responses.SpendingCategoryDetailResponse
import com.example.ibanking_kltn.dtos.responses.SpendingRecordResponse
import com.example.ibanking_kltn.dtos.responses.SpendingSnapshotDetailResponse
import com.example.ibanking_kltn.dtos.responses.SpendingSnapshotResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface SpendingApi {
    @GET("/api/spending/snapshots/")
    suspend fun getAllSpending(
    ): Response<List<SpendingSnapshotResponse>>

    @POST("/api/spending/snapshots/")
    suspend fun createSpendingSnapshot(
        @Body request: SpendingSnapshotRequest
    ): Response<SpendingSnapshotResponse>


    @PUT("/api/spending/snapshots/{snapshotId}")
    suspend fun updateSpendingSnapshot(
        @Path("snapshotId") snapshotId: String,
        @Body request: SpendingSnapshotRequest
    ): Response<SpendingSnapshotResponse>

    @DELETE("/api/spending/snapshots/")
    suspend fun deleteSpendingSnapshot(
        @Query("snapshotId") snapshotId: String,
    ): Response<Unit>

    @GET("/api/spending/snapshots/{snapshotId}")
    suspend fun getDetailSpendingSnapshot(
        @Path("snapshotId") snapshotId: String,
    ): Response<SpendingSnapshotDetailResponse>

    @POST("/api/spending/snapshots/{snapshotId}/categories")
    suspend fun upsertSpendingCategoryDetail(
        @Path("snapshotId") snapshotId: String,
        @Body request: SpendingCategorySnapshotRequest
    ): Response<SpendingCategoryDetailResponse>


    @DELETE("/api/spending/snapshots/categories")
    suspend fun deleteSpendingCategoryDetail(
        @Query("snapshotId") snapshotId: String,
        @Query("categoryCode") categoryCode: String
    ): Response<Unit>

    @POST("/api/spending/snapshots/{snapshotId}/categories/batch")
    suspend fun upsertSpendingCategoriesBatch(
        @Path("snapshotId") snapshotId: String,
        @Body request: List<SpendingCategorySnapshotRequest>
    ): Response<List<SpendingCategoryDetailResponse>>


    // --- SpendingRecordController endpoints (server controller: /api/spending/records) ---
    @POST("/api/spending/records/")
    suspend fun createOrUpdateRecord(
        @Body request: SpendingRecordRequest
    ): Response<SpendingRecordResponse>

    @DELETE("/api/spending/records/")
    suspend fun deleteRecord(
        @Query("transactionId") transactionId: String,
        @Query("spendingRecordType") spendingRecordType: SpendingRecordType
    ): Response<Unit>

    @GET("/api/spending/records/")
    suspend fun getRecordsBySnapshotAndCategory(
        @Query("snapshotId") snapshotId: String,
        @Query("categoryCode") categoryCode: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<Pagination<SpendingRecordResponse>>

    @GET("/api/spending/records/transactions-by-monthly-spending")
    suspend fun getTransactionsByMonthlySpending(
        @Query("monthlySpending") monthlySpending: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<Pagination<SpendingRecordResponse>>



    // GET /system
    @GET("/api/spending/categories/system/")
    suspend fun getAllSystemSpendingCategories(

    ):    Response<List<SpendingCategoryDetailResponse>>

    // GET /defined (requires auth)
    @GET("/api/spending/categories/defined")
    suspend fun filterDefinedSpendingCategories(): Response<List<DefinedSpendingCategoryResponse>>

    // POST /defined
    @POST("/api/spending/categories/defined")
    suspend fun createDefinedSpendingCategories(
        @Body req: DefinedSpendingCategoryRequest
    ): Response<DefinedSpendingCategoryResponse>

    // PUT /defined
    @PUT("/api/spending/categories/defined")
    suspend fun updateDefinedSpendingCategories(
        @Body req: DefinedSpendingCategoryRequest
    ): Response<DefinedSpendingCategoryResponse>

    // DELETE /defined
    @DELETE("/api/spending/categories/defined")
    suspend fun deleteDefinedSpendingCategories(
        @Query("definedSpendingCategoryId") definedSpendingCategoryId: String
    ): Response<Unit>
}



