package com.example.ibanking_kltn.ui.screens.spending.detail

import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class SpendingDetailEvent {
    object ChangeChartType : SpendingDetailEvent()
    object ChangeTab : SpendingDetailEvent()
    object RetryLoadData : SpendingDetailEvent()
    object AddTransaction : SpendingDetailEvent()
    object ViewCategories : SpendingDetailEvent()
    object ChangeVisibleAddDialog : SpendingDetailEvent()
    object AddSpendingCategory: SpendingDetailEvent()
    data class DeleteSpendingCategory( val categoryCode:String): SpendingDetailEvent()
    data class ChangeCategoryName( val categoryName:String): SpendingDetailEvent()
    data class ChangeCategoryBudget( val categoryBudget:String): SpendingDetailEvent()
    data class ChangeCategoryIcon( val icon:String): SpendingDetailEvent()
}

sealed class SpendingDetailEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : SpendingDetailEffect()
    object NavigateToAddTransaction : SpendingDetailEffect()
    object NavigateToCategory : SpendingDetailEffect()
}