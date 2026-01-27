package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.MoneyFlowType
import com.example.ibanking_kltn.data.dtos.responses.AnalyzeResponse
import com.example.ibanking_kltn.data.dtos.responses.DistributionStatisticResponse
import com.example.ibanking_kltn.data.dtos.responses.TrendStatisticResponse
import java.time.LocalDate

data class AnalyticUiState(
    val state: StateType = StateType.NONE,
    val analyzeState: StateType = StateType.NONE,
    val initState: StateType = StateType.NONE,

    val selectedTime : LocalDate = LocalDate.now(),
    val selectedFlowType: MoneyFlowType = MoneyFlowType.OUTGOING,

    val totalValue : Long = 0,
    val distributionStatistic: DistributionStatisticResponse? = null,
    val trendStatistic : TrendStatisticResponse? =null,
    val analyzeResponse: AnalyzeResponse?= null
)
