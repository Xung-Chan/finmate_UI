package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.TransactionApi
import com.example.ibanking_kltn.data.dtos.Pagination
import com.example.ibanking_kltn.data.dtos.requests.ConfirmTransferRequest
import com.example.ibanking_kltn.data.dtos.requests.DepositTransactionRequest
import com.example.ibanking_kltn.data.dtos.requests.FilterTransactionRequest
import com.example.ibanking_kltn.data.dtos.requests.PrepareTransferRequest
import com.example.ibanking_kltn.data.dtos.responses.AllExpenseTypeResponse
import com.example.ibanking_kltn.data.dtos.responses.DepositTransactionResponse
import com.example.ibanking_kltn.data.dtos.responses.ExpenseType
import com.example.ibanking_kltn.data.dtos.responses.HandleVNPayReturnResponse
import com.example.ibanking_kltn.data.dtos.responses.PrepareTransactionResponse
import com.example.ibanking_kltn.data.dtos.responses.TransactionHistoryResponse
import com.example.ibanking_soa.data.utils.ApiResult
import kotlinx.coroutines.delay
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
                    ExpenseType(
                        id = "1",
                        name = "Ăn uống",
                        tag = "food"
                    )
                )
                add(
                    ExpenseType(
                        id = "2",
                        name = "Mua sắm",
                        tag = "shopping"
                    )
                )
                add(
                    ExpenseType(
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
        val url = ""
        return ApiResult.Success(
            data = DepositTransactionResponse(
                url = url
            )
        )
    }


    suspend fun handleVNPayReturn(
        vnp_ResponseCode: String,
        vnp_TxnRef: String,
    ): ApiResult<HandleVNPayReturnResponse> {
//        return safeApiCall(
//            apiCall = { api.handleVNPayReturn(vnp_ResponseCode, vnp_TxnRef) },
//        )
        val transactionId = ""
        return ApiResult.Success(
            data = HandleVNPayReturnResponse(
                transactionId = transactionId
            )
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
                processedAt = "2024-06-01T12:00:00",
                sourceAccountNumber = "123456789",
                sourceBalanceUpdated = 900000.0,
                status = "COMPLETED",
                toWalletNumber = "aschass as"
            )
        )
    }

    suspend fun getTransactionHistory(
        request: FilterTransactionRequest
    ): ApiResult<Pagination<TransactionHistoryResponse>> {
//        return safeApiCall(
//            apiCall = { api.getTransactionHistory(request) },
//        )
        delay(1000L)
        return ApiResult.Success(
            data = Pagination<TransactionHistoryResponse>(
                contents = listOf(
                    TransactionHistoryResponse(
                        id = "txn-123456",
                        amount = 100000.0,
                        description = "Chuyên tien cho Nguyen Van A vao ngay 01/06 lý do thanh toan tien nha",
                        processedAt = "2024-06-01T12:00:00",
                        sourceAccountNumber = "123456789",
                        sourceBalanceUpdated = 900000.0,
                        status = "COMPLETED",
                        toWalletNumber = "0987654321"
                    ),
                    TransactionHistoryResponse(
                        id = "txn-123456",
                        amount = 100000.0,
                        description = "Chuyên tien cho Nguyen Van A vao ngay 01/06 lý do thanh toan tien nha",

                        processedAt = "2024-06-01T12:00:00",
                        sourceAccountNumber = "123456789",
                        sourceBalanceUpdated = 900000.0,
                        status = "COMPLETED",
                        toWalletNumber = "0987654321"
                    ),
                    TransactionHistoryResponse(
                        id = "txn-123456",
                        amount = 100000.0,
                        description = "Chuyên tien cho Nguyen Van A vao ngay 01/06 lý do thanh toan tien nha",

                        processedAt = "2024-06-01T12:00:00",
                        sourceAccountNumber = "123456789",
                        sourceBalanceUpdated = 900000.0,
                        status = "COMPLETED",
                        toWalletNumber = "0987654321"
                    ),
                    TransactionHistoryResponse(
                        id = "txn-123456",
                        amount = 100000.0,
                        description = "Chuyên tien cho Nguyen Van A vao ngay 01/06 lý do thanh toan tien nha",

                        processedAt = "2024-06-01T12:00:00",
                        sourceAccountNumber = "123456789",
                        sourceBalanceUpdated = 900000.0,
                        status = "COMPLETED",
                        toWalletNumber = "0987654321"
                    ),
                    TransactionHistoryResponse(
                        id = "txn-123456",
                        amount = 100000.0,
                        description = "Chuyên tien cho Nguyen Van A vao ngay 01/06 lý do thanh toan tien nha",

                        processedAt = "2024-06-01T12:00:00",
                        sourceAccountNumber = "123456789",
                        sourceBalanceUpdated = 900000.0,
                        status = "COMPLETED",
                        toWalletNumber = "0987654321"
                    ),
                    TransactionHistoryResponse(
                        id = "txn-123456",
                        amount = 100000.0,
                        description = "Chuyên tien cho Nguyen Van A vao ngay 01/06 lý do thanh toan tien nha",

                        processedAt = "2024-06-01T12:00:00",
                        sourceAccountNumber = "123456789",
                        sourceBalanceUpdated = 900000.0,
                        status = "COMPLETED",
                        toWalletNumber = "0987654321"
                    ),
                    TransactionHistoryResponse(
                        id = "txn-123456",
                        amount = 100000.0,
                        description = "Chuyên tien cho Nguyen Van A vao ngay 01/06 lý do thanh toan tien nha",

                        processedAt = "2024-06-01T12:00:00",
                        sourceAccountNumber = "123456789",
                        sourceBalanceUpdated = 900000.0,
                        status = "COMPLETED",
                        toWalletNumber = "0987654321"
                    ),
                    TransactionHistoryResponse(
                        id = "txn-123456",
                        amount = 100000.0,
                        description = "Chuyên tien cho Nguyen Van A vao ngay 01/06 lý do thanh toan tien nha",

                        processedAt = "2024-06-01T12:00:00",
                        sourceAccountNumber = "123456789",
                        sourceBalanceUpdated = 900000.0,
                        status = "COMPLETED",
                        toWalletNumber = "0987654321"
                    ),
                    TransactionHistoryResponse(
                        id = "txn-123456",
                        amount = 100000.0,
                        description = "Chuyên tien cho Nguyen Van A vao ngay 01/06 lý do thanh toan tien nha",

                        processedAt = "2024-06-01T12:00:00",
                        sourceAccountNumber = "123456789",
                        sourceBalanceUpdated = 900000.0,
                        status = "COMPLETED",
                        toWalletNumber = "0987654321"
                    ),
                    TransactionHistoryResponse(
                        id = "txn-123456",
                        amount = 100000.0,
                        description = "Chuyên tien cho Nguyen Van A vao ngay 01/06 lý do thanh toan tien nha",

                        processedAt = "2024-06-01T12:00:00",
                        sourceAccountNumber = "123456789",
                        sourceBalanceUpdated = 900000.0,
                        status = "COMPLETED",
                        toWalletNumber = "0987654321"
                    ),
                    TransactionHistoryResponse(
                        id = "txn-789012",
                        amount = 200000.0,
                        description = "Giao dịch 2",
                        processedAt = "2024-06-02T15:30:00",
                        sourceAccountNumber = "123456789",
                        sourceBalanceUpdated = 700000.0,
                        status = "PENDING",
                        toWalletNumber = "1122334455"
                    )
                ),
                totalPages = 10,
                currentPage = request.page?.let { it + 1 } ?: 1,
                pageSize = 10,
                totalElements = 1000,
            )
        )

    }


}