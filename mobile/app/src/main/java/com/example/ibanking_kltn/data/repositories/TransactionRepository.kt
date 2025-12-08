package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.TransactionApi
import com.example.ibanking_kltn.data.dtos.requests.ConfirmTransferRequest
import com.example.ibanking_kltn.data.dtos.requests.PrepareTransferRequest
import com.example.ibanking_kltn.data.dtos.responses.AllExpenseTypeResponse
import com.example.ibanking_kltn.data.dtos.responses.PrepareTransactionResponse
import com.example.ibanking_soa.data.utils.ApiResult
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val api: TransactionApi,
) {
    suspend fun prepareTransfer(request: PrepareTransferRequest): ApiResult<PrepareTransactionResponse> {
//        return safeApiCall(
//            apiCall = { api.prepareTransfer(request = request) },
//        )
        return ApiResult.Success(
            data = PrepareTransactionResponse(
                retryTokenOtp = "mocked-retry-token-otp",
                transactionId = "mocked-transaction-id"
            )
        )
    }

    suspend fun confirmTransfer(request: ConfirmTransferRequest): ApiResult<Unit> {
//        return safeApiCall(
//            apiCall = { api.confirmTransfer(request = request) },
//        )
        return ApiResult.Success(
            data = Unit
        )
    }


    suspend fun getAllExpenseType(): ApiResult<AllExpenseTypeResponse> {
//        return safeApiCall(
//            apiCall = { api.getAllExpenseType() },
//        )
        return ApiResult.Success(
            data = AllExpenseTypeResponse().apply {
                add(
                    com.example.ibanking_kltn.data.dtos.responses.ExpenseType(
                        id = "1",
                        name = "Ăn uống",
                        tag = "food"
                    )
                )
                add(
                    com.example.ibanking_kltn.data.dtos.responses.ExpenseType(
                        id = "2",
                        name = "Mua sắm",
                        tag = "shopping"
                    )
                )
                add(
                    com.example.ibanking_kltn.data.dtos.responses.ExpenseType(
                        id = "3",
                        name = "Giải trí",
                        tag = "entertainment"
                    )
                )
            }
        )
    }


}