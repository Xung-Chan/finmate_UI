package com.example.ibanking_kltn.data.usecase

import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.dtos.responses.TransactionHistoryResponse
import com.example.ibanking_soa.data.utils.ApiResult
import jakarta.inject.Inject
import kotlinx.coroutines.delay

class GetTransactionUC @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(
        transactionId: String
    ): ApiResult<TransactionHistoryResponse> {
        var message = ""
        repeat(5) {
            val apiResult = transactionRepository.getTransactionStatus(
                transactionId = transactionId
            )
            when (apiResult) {
                is ApiResult.Success -> {
                    return apiResult
                }

                is ApiResult.Error -> {
                    message = apiResult.message
                }
            }
            delay(1000L)
        }
        return ApiResult.Error(message = message)
    }
}