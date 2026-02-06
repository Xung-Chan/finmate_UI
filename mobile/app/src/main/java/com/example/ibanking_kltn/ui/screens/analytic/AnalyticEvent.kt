package com.example.ibanking_kltn.ui.screens.analytic

import com.example.ibanking_kltn.dtos.definitions.MoneyFlowType
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class AnalyticEvent {
    data class ChangeMoneyFlowType (val flowType: MoneyFlowType): AnalyticEvent()
    object RetryLoadData : AnalyticEvent()
    object Analyze : AnalyticEvent()
    object MinusMonth : AnalyticEvent()
    object PlusMonth : AnalyticEvent()
}

sealed class AnalyticEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : AnalyticEffect()
}