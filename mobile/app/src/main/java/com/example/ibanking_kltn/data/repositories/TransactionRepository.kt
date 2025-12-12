package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.TransactionApi
import com.example.ibanking_kltn.data.dtos.requests.ConfirmTransferRequest
import com.example.ibanking_kltn.data.dtos.requests.DepositTransactionRequest
import com.example.ibanking_kltn.data.dtos.requests.PrepareTransferRequest
import com.example.ibanking_kltn.data.dtos.responses.AllExpenseTypeResponse
import com.example.ibanking_kltn.data.dtos.responses.DepositTransactionResponse
import com.example.ibanking_kltn.data.dtos.responses.PrepareTransactionResponse
import com.example.ibanking_kltn.data.dtos.responses.TransactionHistoryResponse
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
    suspend fun createDepositTransaction(
        request: DepositTransactionRequest
    ): ApiResult<DepositTransactionResponse> {
//        return safeApiCall(
//            apiCall = { api.createDepositTransaction(request) },
//        )
        val url =""
        return  ApiResult.Success(
            data = DepositTransactionResponse(
                url = url
            )
        )
    }


    suspend fun handleVNPayReturn(
        vnp_ResponseCode: String,
        vnp_TxnRef: String,
    ): ApiResult<String> {
//        return safeApiCall(
//            apiCall = { api.handleVNPayReturn(vnp_ResponseCode, vnp_TxnRef) },
//        )
        val transactionId=""
        return ApiResult.Success(
            data = transactionId
        )
    }

    suspend fun getTransactionStatus(
        transactionId: String,
    ): ApiResult<TransactionHistoryResponse> {

//        return safeApiCall(
//            apiCall = { api.getTransactionStatus(transactionId) },
//        )
        return ApiResult.Success(
            data = TransactionHistoryResponse(
                id = "txn-123456",
                amount = 100000.0,
                description = "Deposit via VNPay",
                destinationBalanceUpdated = null,
                metadata = null,
                processedAt = "2024-06-01T12:00:00Z",
                sourceAccountNumber = "123456789",
                sourceBalanceUpdated = null,
                status = "COMPLETED",
                toWalletNumber = null
            )
        )
    }


}