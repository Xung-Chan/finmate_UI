package com.example.ibanking_kltn.data.usecase

import com.example.ibanking_kltn.data.dtos.MoneyFlowType
import com.example.ibanking_kltn.data.dtos.requests.TrendStatisticRequest
import com.example.ibanking_kltn.data.dtos.responses.TrendStatisticResponse
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_soa.data.utils.ApiResult
import jakarta.inject.Inject
import java.time.LocalDate

class GetTrendStatisticUC @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(
        request: TrendStatisticRequest = TrendStatisticRequest(
            moneyFlowType = MoneyFlowType.OUTGOING.name,
            year = LocalDate.now().year
        )
    ): ApiResult<TrendStatisticResponse> {
        var message = ""
        repeat(3) {
            val apiResult = transactionRepository.getTrendStatistic(request)
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