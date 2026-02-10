package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.SpendingApi
import com.example.ibanking_kltn.data.exception.safeApiCall
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
import com.example.ibanking_soa.data.utils.ApiResult
import jakarta.inject.Inject

class SpendingRepository @Inject constructor(
    private val spendingApi: SpendingApi,
) {
    // ==================== Spending Snapshot APIs ====================

    suspend fun getAllSpending(): ApiResult<List<SpendingSnapshotResponse>> {
        return safeApiCall(
            apiCall = { spendingApi.getAllSpending() }
        )
//        delay(1000L)
//        return ApiResult.Success(
//            listOf(
//                SpendingSnapshotResponse(
//                    id = "1",
//                    snapshotName = "January 2024",
//                    budgetAmount = BigDecimal(20000000),
//                    usedAmount = BigDecimal(15000000),
//                    monthlySpending = "2024-01",
//                    description = "Spending snapshot for January 2024",
//                )
//            )
//        )
    }

    suspend fun createSpendingSnapshot(
        request: SpendingSnapshotRequest
    ): ApiResult<SpendingSnapshotResponse> {
        return safeApiCall(
            apiCall = { spendingApi.createSpendingSnapshot(request) }
        )
//        delay(1000L)
//        return ApiResult.Success(
//            SpendingSnapshotResponse(
//                id = "1",
//                snapshotName = request.snapshotName,
//                budgetAmount = request.budgetAmount,
//                usedAmount = BigDecimal(0),
//                monthlySpending = request.monthlySpending,
//                description = "Spending snapshot for ${request.monthlySpending}",
//            )
//        )
    }

    suspend fun updateSpendingSnapshot(
        snapshotId: String,
        request: SpendingSnapshotRequest
    ): ApiResult<SpendingSnapshotResponse> {
        return safeApiCall(
            apiCall = { spendingApi.updateSpendingSnapshot(snapshotId, request) }
        )
    }

    suspend fun deleteSpendingSnapshot(
        snapshotId: String
    ): ApiResult<Unit> {
        return safeApiCall(
            apiCall = { spendingApi.deleteSpendingSnapshot(snapshotId) }
        )
    }

    suspend fun getDetailSpendingSnapshot(
        snapshotId: String
    ): ApiResult<SpendingSnapshotDetailResponse> {
        return safeApiCall(
            apiCall = { spendingApi.getDetailSpendingSnapshot(snapshotId) }
        )
//        delay(1000L)
//        return ApiResult.Success(
//            data = SpendingSnapshotDetailResponse(
//                id = "1",
//                snapshotName = "Chi tiêu tháng 6",
//                budgetAmount = BigDecimal(5000000),
//                usedAmount = BigDecimal(3500000),
//                monthlySpending = "8/2025",
//                spendingCategories = listOf(
//                    SpendingCategoryDetailResponse(
//                        categoryName = "Ăn uống",
//                        categoryCode = "food",
//                        categoryIcon = "",
//                        textColor = "#FFFFFF",
//                        backgroundColor = "#FF6B6B",
//                        budgetAmount = BigDecimal(5000000),
//                        usedAmount = BigDecimal(3500000),
//                        categoryId = "cat_001"
//                    ),
//                    SpendingCategoryDetailResponse(
//                        categoryName = "Đi lại",
//                        categoryCode = "transport",
//                        categoryIcon = "",
//                        textColor = "#FFFFFF",
//                        backgroundColor = "#4D96FF",
//                        budgetAmount = BigDecimal(5000000),
//                        usedAmount = BigDecimal(3500000),
//                        categoryId = "cat_002"
//                    ),
//                    SpendingCategoryDetailResponse(
//                        categoryName = "Mua sắm",
//                        categoryCode = "shopping",
//                        categoryIcon = "",
//                        textColor = "#000000",
//                        backgroundColor = "#FFD93D",
//                        budgetAmount = BigDecimal(5000000),
//                        usedAmount = BigDecimal(3500000),
//                        categoryId = "cat_003"
//                    ),
//                    SpendingCategoryDetailResponse(
//                        categoryName = "Giải trí",
//                        categoryCode = "entertainment",
//                        categoryIcon = "",
//                        textColor = "#FFFFFF",
//                        backgroundColor = "#6BCB77",
//                        budgetAmount = BigDecimal(5000000),
//                        usedAmount = BigDecimal(3500000),
//                        categoryId = "cat_004"
//                    ),
//                    SpendingCategoryDetailResponse(
//                        categoryName = "Sức khỏe",
//                        categoryCode = "health",
//                        categoryIcon = "",
//                        textColor = "#FFFFFF",
//                        backgroundColor = "#9D4EDD",
//                        budgetAmount = BigDecimal(5000000),
//                        usedAmount = BigDecimal(3500000),
//                        categoryId = "cat_005"
//                    ),
//                )
//                ,
//            )
//        )
    }

    // ==================== Spending Category APIs ====================

    suspend fun upsertSpendingCategoryDetail(
        snapshotId: String,
        request: SpendingCategorySnapshotRequest
    ): ApiResult<SpendingCategoryDetailResponse> {
        return safeApiCall(
            apiCall = { spendingApi.upsertSpendingCategoryDetail(snapshotId, request) }
        )
    }

    suspend fun deleteSpendingCategoryDetail(
        snapshotId: String,
        categoryCode: String
    ): ApiResult<Unit> {
        return safeApiCall(
            apiCall = { spendingApi.deleteSpendingCategoryDetail(snapshotId, categoryCode) }
        )
    }

    suspend fun upsertSpendingCategoriesBatch(
        snapshotId: String,
        request: List<SpendingCategorySnapshotRequest>
    ): ApiResult<List<SpendingCategoryDetailResponse>> {
        return safeApiCall(
            apiCall = { spendingApi.upsertSpendingCategoriesBatch(snapshotId, request) }
        )
    }

    // ==================== Spending Record APIs ====================

    suspend fun createOrUpdateRecord(
        request: SpendingRecordRequest
    ): ApiResult<SpendingRecordResponse> {
        return safeApiCall(
            apiCall = { spendingApi.createOrUpdateRecord(request) }
        )
    }

    suspend fun deleteRecord(
        transactionId: String,
        spendingRecordType: SpendingRecordType
    ): ApiResult<Unit> {
        return safeApiCall(
            apiCall = { spendingApi.deleteRecord(transactionId, spendingRecordType) }
        )
    }

    suspend fun getRecordsBySnapshotAndCategory(
        snapshotId: String,
        categoryCode: String,
        page: Int = 0,
        size: Int = 10
    ): ApiResult<Pagination<SpendingRecordResponse>> {
        return safeApiCall(
            apiCall = {
                spendingApi.getRecordsBySnapshotAndCategory(
                    snapshotId,
                    categoryCode,
                    page,
                    size
                )
            }
        )
    }

    suspend fun getTransactionsByMonthlySpending(
        monthlySpending: String,
        page: Int = 0,
        size: Int = 10
    ): ApiResult<Pagination<SpendingRecordResponse>> {
        return safeApiCall(
            apiCall = {
                spendingApi.getTransactionsByMonthlySpending(
                    monthlySpending,
                    page,
                    size
                )
            }
        )
//        delay(1000L)
//        return ApiResult.Success(
//            data = Pagination(
//                contents = listOf(
//                    SpendingRecordResponse(
//                        id = "rec_001",
//                        snapshotId = "snap_20260202",
//                        transactionId = "txn_889900",
//                        amount = BigDecimal("150000.00"),
//                        description = "Thanh toán tiền điện",
//                        destinationAccountName = "EVN HCMC",
//                        destinationAccountNumber = "19001001",
//                        recordType = SpendingRecordType.EXPENSE,
//                        categoryCode = "UTIL",
//                        categoryName = "Tiện ích",
//                        categoryIcon = R.drawable.airplane_service.toString(),
//                        categoryTextColor = "#FFFFFF",
//                        categoryBackgroundColor = "#FF9800",
//                        occurredAt = LocalDateTime.now().toString()
//                    )
//                ),
//                currentPage = page,
//                pageSize = size,
//                totalElements = 100,
//                totalPages = 10
//            )
//        )

    }

    // ==================== Defined Spending Category APIs ====================

    suspend fun getAllSystemSpendingCategories(): ApiResult<List<SpendingCategoryDetailResponse>> {
        return safeApiCall(
            apiCall = { spendingApi.getAllSystemSpendingCategories() }
        )
    }

    suspend fun getAllDefinedSpendingCategories(): ApiResult<List<DefinedSpendingCategoryResponse>> {
        return safeApiCall(
            apiCall = { spendingApi.filterDefinedSpendingCategories() }
        )
//        delay(1000L)
//        return ApiResult.Success(
//            data = listOf(
//                DefinedSpendingCategoryResponse(
//                    id = "defcat_001",
//                    name = "Ăn uống",
//                    code = "cook",
//                    icon = "fooog",
//                    textColor = "TODO()",
//                    backgroundColor = "TODO()",
//                    systemCategoryCode = "food",
//                    systemCategoryName = "Ăn uống",
//                ),
//            )
//        )
    }

    suspend fun createDefinedSpendingCategories(
        request: DefinedSpendingCategoryRequest
    ): ApiResult<DefinedSpendingCategoryResponse> {
        return safeApiCall(
            apiCall = { spendingApi.createDefinedSpendingCategories(request) }
        )
    }

    suspend fun updateDefinedSpendingCategories(
        request: DefinedSpendingCategoryRequest
    ): ApiResult<DefinedSpendingCategoryResponse> {
        return safeApiCall(
            apiCall = { spendingApi.updateDefinedSpendingCategories(request) }
        )
    }

    suspend fun deleteDefinedSpendingCategories(
        definedSpendingCategoryId: String
    ): ApiResult<Unit> {
        return safeApiCall(
            apiCall = { spendingApi.deleteDefinedSpendingCategories(definedSpendingCategoryId) }
        )
    }

}