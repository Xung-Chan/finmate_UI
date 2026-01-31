package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.TransactionApi
import com.example.ibanking_kltn.data.exception.safeApiCall
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
import com.example.ibanking_soa.data.utils.ApiResult
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val api: TransactionApi,
) {
    suspend fun prepareTransfer(request: PrepareTransferRequest): ApiResult<PrepareTransactionResponse> {
        return safeApiCall(
            apiCall = { api.prepareTransfer(request = request) },
        )
//        delay(1000L)
//        return ApiResult.Success(
//            data = PrepareTransactionResponse(
//                retryTokenOtp = "mocked-retry-token-otp",
//                transactionId = "mocked-transaction-id"
//            )
//        )
    }

    suspend fun confirmTransfer(request: ConfirmTransferRequest): ApiResult<TransactionResponse> {
        return safeApiCall(
            apiCall = { api.confirmTransfer(request = request) },
        )
//        delay(1000L)
//         return ApiResult.Success(
//             data = Unit
//         )
    }


    suspend fun getAllExpenseType(): ApiResult<AllExpenseTypeResponse> {
        return safeApiCall(
            apiCall = { api.getAllExpenseType() },
        )
//        delay(1000L)
//         return ApiResult.Success(
//             data = AllExpenseTypeResponse().apply {
//                 add(
//                     ExpenseType(
//                         id = "1",
//                         name = "Ăn uống",
//                         tag = "food"
//                     )
//                 )
//                 add(
//                     ExpenseType(
//                         id = "2",
//                         name = "Mua sắm",
//                         tag = "shopping"
//                     )
//                 )
//                 add(
//                     ExpenseType(
//                         id = "3",
//                         name = "Giải trí",
//                         tag = "entertainment"
//                     )
//                 )
//             }
//         )

    }

    suspend fun createDepositTransaction(
        request: DepositTransactionRequest
    ): ApiResult<DepositTransactionResponse> {
        return safeApiCall(
            apiCall = { api.createDepositTransaction(request) },
        )
//        delay(1000L)
//         val url = ""
//         return ApiResult.Success(
//             data = DepositTransactionResponse(
//                 url = url
//             )
//         )
    }


    suspend fun handleVNPayReturn(
        vnp_ResponseCode: String,
        vnp_TxnRef: String,
    ): ApiResult<TransactionResponse> {
        return safeApiCall(
            apiCall = { api.handleVNPayReturn(vnp_ResponseCode, vnp_TxnRef) },
        )
//        delay(1000L)
//         val transactionId = ""
//         return ApiResult.Success(
//             data = HandleVNPayReturnResponse(
//                 transactionId = transactionId
//             )
//         )
    }

    suspend fun getTransactionStatus(
        transactionId: String,
    ): ApiResult<TransactionHistoryResponse> {

        return safeApiCall(
            apiCall = { api.getTransactionStatus(transactionId) },
        )
//        delay(1000L)
//         return ApiResult.Success(
//             data = TransactionHistoryResponse(
//                 id = "txn-123456",
//                 amount = 100000.0,
//                 description = "Deposit via VNPay",
//                 processedAt = "2024-06-01T12:00:00",
//                 sourceAccountNumber = "123456789",
//                 sourceBalanceUpdated = 900000.0,
//                 status = "COMPLETED",
//                 toWalletNumber = "aschass as",
//                 transactionType = ServiceType.TRANSFER
//             )
//         )
    }

    suspend fun getTransactionHistory(
        request: FilterTransactionRequest
    ): ApiResult<Pagination<TransactionHistoryResponse>> {
        return safeApiCall(
            apiCall = { api.getTransactionHistory(request) },
        )
//         delay(1000L)
//         return ApiResult.Success(
//             data = Pagination<TransactionHistoryResponse>(
//                 contents = listOf(
//                     TransactionHistoryResponse(
//                         id = "txn-123456",
//                         amount = 100000.0,
//                         description = "Chuyên tien cho Nguyen Van A vao ngay 01/06 lý do thanh toan tien nha",
//                         processedAt = "2024-06-01T12:00:00",
//                         sourceAccountNumber = "123456789",
//                         sourceBalanceUpdated = 900000.0,
//                         status = "COMPLETED",
//                         toWalletNumber = "0987654321",
//                         transactionType = ServiceType.TRANSFER
//                     ),
//                     TransactionHistoryResponse(
//                         id = "txn-123456",
//                         amount = 100000.0,
//                         description = "Chuyên tien cho Nguyen Van A vao ngay 01/06 lý do thanh toan tien nha",
//
//                         processedAt = "2024-06-01T12:00:00",
//                         sourceAccountNumber = "123456789",
//                         sourceBalanceUpdated = 900000.0,
//                         status = "COMPLETED",
//                         toWalletNumber = "0987654321",
//
//                         transactionType = ServiceType.TRANSFER
//                     ),
//                 ),
//                 totalPages = 10,
//                 currentPage = request.page?.let { it + 1 } ?: 1,
//                 pageSize = 10,
//                 totalElements = 1000,
//             )
//         )

    }

    suspend fun getTrendStatistic(
        request: TrendStatisticRequest
    ): ApiResult<TrendStatisticResponse> {
        return safeApiCall(
            apiCall = { api.getTrendStatistic(request) }
        )
//        delay(1000L)
//        return ApiResult.Success(
//            data = TrendStatisticResponse().apply {
//                for (month in 1..12) {
//                    this.add(
//                        com.example.ibanking_kltn.data.dtos.responses.TrendStatisticResponseItem(
//                            date = "2026-%02d".format(month),
//                            totalTransactions = (10..100).random(),
//                            totalValue = (1_000_000L..10_000_000L).random()
//                        )
//                    )
//                }
//            }
//        )
    }

    suspend fun getDistributionStatistic(
        request: DistributionStatisticRequest
    ): ApiResult<DistributionStatisticResponse> {
        return safeApiCall(
            apiCall = { api.getDistributionStatistic(request) }
        )
//        delay(1000L)
//        return ApiResult.Success(
//            data = DistributionStatisticResponse(
//                analyticId = "analytic-123456",
//                distributions = listOf(
//                    com.example.ibanking_kltn.data.dtos.responses.Distribution(
//                        label = "Ăn uống",
//                        expenseName = "Ăn uống",
//                        expenseTag = "food",
//                        totalTransactions = 50,
//                        totalValue = 5_000_000L
//                    ),
//                    com.example.ibanking_kltn.data.dtos.responses.Distribution(
//                        label = "Mua sắm",
//                        expenseName = "Mua sắm",
//                        expenseTag = "shopping",
//                        totalTransactions = 30,
//                        totalValue = 3_000_000L
//                    ),
//                    com.example.ibanking_kltn.data.dtos.responses.Distribution(
//                        label = "Giải trí",
//                        expenseName = "Giải trí",
//                        expenseTag = "entertainment",
//                        totalTransactions = 20,
//                        totalValue = 2_000_000L
//                    )
//                )
//            )
//        )
    }
}