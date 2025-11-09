package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.TransactionApi
import com.example.ibanking_kltn.data.dtos.requests.ConfirmTransferRequest
import com.example.ibanking_kltn.data.dtos.requests.PrepareTransferRequest
import com.example.ibanking_kltn.data.dtos.responses.AllExpenseTypeResponse
import com.example.ibanking_kltn.data.dtos.responses.PrepareTransactionResponse
import com.example.ibanking_kltn.ui.exception.safeApiCall
import com.example.ibanking_soa.data.utils.ApiResult
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val api: TransactionApi,
) {
    suspend fun prepareTransfer(request: PrepareTransferRequest): ApiResult<PrepareTransactionResponse> {
        return safeApiCall(
            apiCall = { api.prepareTransfer(request = request) },
        )
    }

    suspend fun confirmTransfer(request: ConfirmTransferRequest): ApiResult<Unit> {
        return safeApiCall(
            apiCall = { api.confirmTransfer(request = request) },
        )
    }


    suspend fun getAllExpenseType(): ApiResult<AllExpenseTypeResponse> {
        return safeApiCall(
            apiCall = { api.getAllExpenseType() },
        )
    }


}