package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.BillApi
import com.example.ibanking_kltn.data.dtos.requests.CancelBillRequest
import com.example.ibanking_kltn.data.dtos.requests.CreateBillRequest
import com.example.ibanking_kltn.data.dtos.requests.PreparePayBillRequest
import com.example.ibanking_kltn.data.dtos.requests.PreparePrePaymentRequest
import com.example.ibanking_kltn.data.dtos.responses.BillResponse
import com.example.ibanking_kltn.data.dtos.responses.PrepareTransactionResponse
import com.example.ibanking_kltn.ui.exception.safeApiCall
import com.example.ibanking_soa.data.utils.ApiResult
import jakarta.inject.Inject

class BillRepository @Inject constructor(
    private val billApi: BillApi
) {
    suspend fun createBill(request: CreateBillRequest): ApiResult<BillResponse> {
        return safeApiCall { billApi.createBill(request) }
    }

    suspend fun getBillInfo(qrCode: String): ApiResult<BillResponse> {
        return safeApiCall { billApi.getBillInfo(qrCode) }
    }

    suspend fun preparePayBill(request: PreparePayBillRequest): ApiResult<PrepareTransactionResponse> {
        return safeApiCall { billApi.preparePayBill(request) }
    }

    suspend fun preparePrepayment(request: PreparePrePaymentRequest): ApiResult<PrepareTransactionResponse> {
        return safeApiCall { billApi.preparePrepayment(request) }
    }

    suspend fun cancelBill(request: CancelBillRequest): ApiResult<Unit> {
        return safeApiCall { billApi.cancelBill(request) }
    }
}
