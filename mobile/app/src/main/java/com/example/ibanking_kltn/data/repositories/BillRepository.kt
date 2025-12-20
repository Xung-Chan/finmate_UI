package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.BillApi
import com.example.ibanking_kltn.data.dtos.Pagination
import com.example.ibanking_kltn.data.dtos.requests.CancelBillRequest
import com.example.ibanking_kltn.data.dtos.requests.CreateBillRequest
import com.example.ibanking_kltn.data.dtos.requests.FilterBillRequest
import com.example.ibanking_kltn.data.dtos.requests.PreparePayBillRequest
import com.example.ibanking_kltn.data.dtos.requests.PreparePrePaymentRequest
import com.example.ibanking_kltn.data.dtos.responses.BillResponse
import com.example.ibanking_kltn.data.dtos.responses.PrepareTransactionResponse
import com.example.ibanking_kltn.ui.exception.safeApiCall
import com.example.ibanking_soa.data.utils.ApiResult
import jakarta.inject.Inject
import kotlinx.coroutines.delay

class BillRepository @Inject constructor(
    private val billApi: BillApi
) {
    suspend fun createBill(request: CreateBillRequest): ApiResult<BillResponse> {
        return safeApiCall { billApi.createBill(request) }
    }

    suspend fun getBillInfo(qrCode: String): ApiResult<BillResponse> {
//        return safeApiCall { billApi.getBillInfo(qrCode) }
        return ApiResult.Success(
            data = BillResponse(
                merchantName = "Mocked Merchant",
                amount = 250000,
                dueDate = "2024-12-31",
                billStatus = "UNPAID",
                description = "Mocked bill description",
                metadata = mapOf("orderId" to "ORD-123456"),
                qrIdentifier = "MOCKED-QR-CODE-12345",
                walletNumber = "0987654321"
            )
        )
    }

    suspend fun preparePayBill(request: PreparePayBillRequest): ApiResult<PrepareTransactionResponse> {
//        return safeApiCall { billApi.preparePayBill(request) }
        return ApiResult.Success(
            data = PrepareTransactionResponse(
                retryTokenOtp = "mocked-retry-token-otp",
                transactionId = "mocked-transaction-id"
            )
        )
    }

    suspend fun preparePrepayment(request: PreparePrePaymentRequest): ApiResult<PrepareTransactionResponse> {
//        return safeApiCall { billApi.preparePrepayment(request) }
        return ApiResult.Success(
            data = PrepareTransactionResponse(
                retryTokenOtp = "mocked-retry-token-otp",
                transactionId = "mocked-transaction-id"
            )
        )
    }

    suspend fun cancelBill(request: CancelBillRequest): ApiResult<Unit> {
//        return safeApiCall { billApi.cancelBill(request) }
        return ApiResult.Success(Unit)
    }

    suspend fun filterBill(request: FilterBillRequest): ApiResult<Pagination<BillResponse>> {
//        return safeApiCall {
//            billApi.filterBill(
//                request = FilterBillRequest()
//            )
//        }
        val paidBillsSortedByDueDate = listOf(
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "PAID",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "PAID",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "PAID",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "PAID",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "PAID",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "PAID",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "PAID",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "PAID",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "PAID",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "PAID",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
        )
        val activeBillsSortedByDueDate = listOf(
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "ACTIVE",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "ACTIVE",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "ACTIVE",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "ACTIVE",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "ACTIVE",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "ACTIVE",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "ACTIVE",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "ACTIVE",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "ACTIVE",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "ACTIVE",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
            BillResponse(
                merchantName = "Mocked Merchant 1",
                amount = 120000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = "ACTIVE",
                description = "Internet bill",
                metadata = mapOf("orderId" to "ORD-100001"),
                qrIdentifier = "QR-100001",
                walletNumber = "1234567890"
            ),
        )

        delay(1000)
        when (request.status) {
            null -> {
                return  ApiResult.Success(
                    data = Pagination<BillResponse>(
                        contents = paidBillsSortedByDueDate + activeBillsSortedByDueDate,
                        totalPages = 10,
                        totalElements = paidBillsSortedByDueDate.size+activeBillsSortedByDueDate.size,
                        currentPage = request.page ?: 1,
                        pageSize = 10

                    )
                )

            }
            "PAID" -> {
                return ApiResult.Success(
                    data = Pagination<BillResponse>(
                        contents = paidBillsSortedByDueDate,
                        totalPages = 10,
                        totalElements = paidBillsSortedByDueDate.size,
                        currentPage = request.page ?: 1,
                        pageSize = 10
                    )
                )
            }
            else -> {
                return  ApiResult.Success(
                    data = Pagination<BillResponse>(
                        contents = activeBillsSortedByDueDate,
                        totalPages = 10,
                        totalElements = activeBillsSortedByDueDate.size,
                        currentPage = request.page ?: 1,
                        pageSize = 10
                    )
                )

            }
        }
    }
}
