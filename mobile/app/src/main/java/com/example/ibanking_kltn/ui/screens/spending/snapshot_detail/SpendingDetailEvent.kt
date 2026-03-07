package com.example.ibanking_kltn.ui.screens.spending.snapshot_detail

import com.example.ibanking_kltn.dtos.responses.SpendingRecordResponse
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class SpendingDetailEvent {
    object ChangeChartType : SpendingDetailEvent()
    object ChangeTab : SpendingDetailEvent()
    object RetryLoadData : SpendingDetailEvent()
    object RefreshSnapshot : SpendingDetailEvent()
    object AddTransaction : SpendingDetailEvent()
    object ViewCategories : SpendingDetailEvent()
    object ChangeVisibleAddDialog : SpendingDetailEvent()
    object ChangeVisibleEditDialog : SpendingDetailEvent()
    object AddSpendingCategory : SpendingDetailEvent()
    object UpdateSpendingCategory : SpendingDetailEvent()
    object Analyze : SpendingDetailEvent()
    object ShowCategoryIconBottomSheet : SpendingDetailEvent()
    object HideCategoryIconBottomSheet : SpendingDetailEvent()

    data class DeleteSpendingCategory(val categoryCode: String) : SpendingDetailEvent()
    data class ShowEditDialog(val categoryCode: String) : SpendingDetailEvent()
    data class ChangeCategoryName(val categoryName: String) : SpendingDetailEvent()
    data class ChangeCategoryBudget(val categoryBudget: String) : SpendingDetailEvent()
    data class ChangeCategoryIcon(val id: String, val icon: String, val color: String) :
        SpendingDetailEvent()

    // Record reclassification
    data class ShowReclassifyBottomSheet(val record: SpendingRecordResponse) : SpendingDetailEvent()
    object HideReclassifyBottomSheet : SpendingDetailEvent()
    data class ReclassifyRecord(val categoryCode: String) : SpendingDetailEvent()

    // Delete defined transaction (EXTERNAL)
    data class ShowDeleteRecordDialog(val recordId: String) : SpendingDetailEvent()
    object HideDeleteRecordDialog : SpendingDetailEvent()
    object ConfirmDeleteDefinedTransaction : SpendingDetailEvent()

    // Recommend spending
    object ShowRecommendInputDialog : SpendingDetailEvent()
    object HideRecommendInputDialog : SpendingDetailEvent()
    data class ChangeRecommendRequirement(val requirement: String) : SpendingDetailEvent()
    object SubmitRecommend : SpendingDetailEvent()
    object ApplyRecommend : SpendingDetailEvent()
    object HideRecommendResultDialog : SpendingDetailEvent()

}
sealed class SpendingDetailEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : SpendingDetailEffect()
    object ReclassifyRecordSuccess : SpendingDetailEffect()
    object NavigateToAddTransaction : SpendingDetailEffect()
    object NavigateToCategory : SpendingDetailEffect()
}