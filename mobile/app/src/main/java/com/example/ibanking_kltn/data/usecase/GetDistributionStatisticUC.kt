package com.example.ibanking_kltn.data.usecase

import com.example.ibanking_kltn.dtos.requests.DistributionStatisticRequest
import com.example.ibanking_kltn.dtos.responses.DistributionStatisticResponse
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_soa.data.utils.ApiResult
import jakarta.inject.Inject
import java.time.LocalDate

class GetDistributionStatisticUC @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(
        request: DistributionStatisticRequest = DistributionStatisticRequest(
            referenceDate = LocalDate.now().toString()
        )
    ): ApiResult<DistributionStatisticResponse> {
        var message = ""
        repeat(3) {
            val apiResult = transactionRepository.getDistributionStatistic(request)
            when (apiResult) {
                is ApiResult.Success -> {
                    return apiResult
                }

                is ApiResult.Error -> {
                    message = apiResult.message
                }
            }

        }
        return ApiResult.Error(message = message)
    }
}