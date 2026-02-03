package com.example.ibanking_kltn.ui.event

import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class SpendingDetailEvent {
    object ChangeChartType: SpendingDetailEvent()
    object ChangeTab: SpendingDetailEvent()
    object RetryLoadData: SpendingDetailEvent()
    object AddTransaction: SpendingDetailEvent()
    object ViewCategories: SpendingDetailEvent()
}

sealed class SpendingDetailEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : SpendingDetailEffect()
}