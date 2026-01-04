package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.PayLaterApi
import com.example.ibanking_kltn.data.dtos.Pagination
import com.example.ibanking_kltn.data.dtos.requests.FilterBillingCyclesRequest
import com.example.ibanking_kltn.data.dtos.requests.FilterPayLaterApplicationRequest
import com.example.ibanking_kltn.data.dtos.requests.PayLaterApplicationRequest
import com.example.ibanking_kltn.data.dtos.responses.BillingCycleResonse
import com.example.ibanking_kltn.data.dtos.responses.PayLaterApplicationResponse
import com.example.ibanking_kltn.data.dtos.responses.PayLaterResponse
import com.example.ibanking_kltn.ui.exception.safeApiCall
import com.example.ibanking_soa.data.utils.ApiResult
import jakarta.inject.Inject

class PayLaterRepository @Inject constructor(
    private val payLaterApi: PayLaterApi
) {
    suspend fun getMyPayLater(): ApiResult<PayLaterResponse> {
       return safeApiCall(
           apiCall = { payLaterApi.getMyPaylater() }
       )
        // delay(1000L)
        // return ApiResult.Success(
        //     data = PayLaterResponse(
        //         approvedAt = "2025-01-12T10:35:00Z",
        //         approvedBy = "AdminSystem",
        //         availableCredit = 1500000.0,
        //         creditLimit = 5000000.0,
        //         id = "PL-ACC-123456",
        //         interestRate = 2.5,
        //         nextBillingDate = "2025-02-01",
        //         nextDueDate = "2025-02-10",
        //         payLaterAccountNumber = "9990011223344",
        //         status = PayLaterAccountStatus.ACTIVE,
        //         usedCredit = 3500000.0,
        //         username = "xungchan",
        //         walletNumber = "WLT-556677"
        //     )
        // )
    }
    suspend fun submitApplication(
        request: PayLaterApplicationRequest
    ): ApiResult<Unit> {
       return safeApiCall(
           apiCall = { payLaterApi.submitApplication(request) }
       )
        // delay(1000L)
        // return ApiResult.Success(
        //     data = Unit
        // )
    }

    suspend fun filterApplication(
        request: FilterPayLaterApplicationRequest
    ): ApiResult<Pagination<PayLaterApplicationResponse>> {
       return safeApiCall(
           apiCall = { payLaterApi.filterApplications(request) }
       )
        // val dummyData = listOf(
        //     PayLaterApplicationResponse(
        //         id = "APP-1001",
        //         username = "xungchan",
        //         type = PayLaterApplicationType.LIMIT_ADJUSTMENT,
        //         requestedCreditLimit = 3000000.0,
        //         approvedLimit = 2500000.0,
        //         reason = "Need extra credit for shopping",
        //         rejectionReason = null,
        //         status = PayLaterApplicationStatus.APPROVED,
        //         approvedBy = "AdminSystem",
        //         appliedAt = "2024-12-20",
        //         processedAt = "2024-12-22"
        //     ),
        //     PayLaterApplicationResponse(
        //         id = "APP-1002",
        //         username = "xungchan",
        //         type = PayLaterApplicationType.ACTIVATION,
        //         requestedCreditLimit = 2000000.0,
        //         approvedLimit = null,
        //         reason = "Want to increase my credit limit",
        //         rejectionReason = "Insufficient credit history",
        //         status = PayLaterApplicationStatus.REJECTED,
        //         approvedBy = "AdminSystem",
        //         appliedAt = "2024-12-20",
        //         processedAt = "2024-12-22"
        //     )
        // )

        // delay(1000L)
        // return ApiResult.Success(
        //     data = Pagination<PayLaterApplicationResponse>(
        //         contents = dummyData,
        //         currentPage = 1,
        //         pageSize = 10,
        //         totalElements = 100,
        //         totalPages = 10
        //     )

        // )
    }


    suspend fun filterBillingCycles(
        request: FilterBillingCyclesRequest
    ): ApiResult<Pagination<BillingCycleResonse>> {
       return safeApiCall(
           apiCall = { payLaterApi.filterBillingCycles(request) }
       )
        // val dummyData = listOf(
        //     BillingCycleResonse(
        //         code = "BC-202401",
        //         startDate = "2024-01-01",
        //         endDate = "2024-01-31",
        //         dueDate = "2024-02-10",
        //         totalSpent = 2000000.0,
        //         paidPrincipal = 1500000.0,
        //         minimumPayment = 300000.0,
        //         totalInterest = 50000.0,
        //         paidInterest = 30000.0,
        //         lateInterestRate = 1.5,
        //         penaltyFee = null,
        //         penaltyApplied = false,

        //         status = BillingCycleStatus.OVERDUE            ),
        //     BillingCycleResonse(
        //         code = "BC-202402",
        //         startDate = "2024-02-01",
        //         endDate = "2024-02-28",
        //         dueDate = "2024-03-10",
        //         totalSpent = 2500000.0,
        //         paidPrincipal = 1000000.0,
        //         minimumPayment = 400000.0,
        //         totalInterest = 60000.0,
        //         paidInterest = 20000.0,
        //         lateInterestRate = 1.5,
        //         penaltyFee = 5000.0,
        //         penaltyApplied = true,
        //         status = BillingCycleStatus.OPEN
        //     )
        // )

        // delay(1000L)
        // return ApiResult.Success(
        //     data = Pagination<BillingCycleResonse>(
        //         contents = dummyData,
        //         currentPage = 1,
        //         pageSize = 10,
        //         totalElements = 50,
        //         totalPages = 5
        //     )

        // )
    }

    suspend fun getBillingCycles(
        billingCycleCode:String
    ): ApiResult<BillingCycleResonse> {
       return safeApiCall(
           apiCall = { payLaterApi.getBillingCycles(billingCycleCode =billingCycleCode ) }
       )
        // delay(1000L)
        // return ApiResult.Success(
        //     data = BillingCycleResonse(
        //         code = "BC-202401",
        //         startDate = "2024-01-01",
        //         endDate = "2024-01-31",
        //         dueDate = "2024-02-10",
        //         totalSpent = 2000000.0,
        //         paidPrincipal = 1500000.0,
        //         minimumPayment = 300000.0,
        //         totalInterest = 50000.0,
        //         paidInterest = 30000.0,
        //         lateInterestRate = 1.5,
        //         penaltyFee = null,
        //         penaltyApplied = false,

        //         status = BillingCycleStatus.OVERDUE
        //     )

        // )
    }


}