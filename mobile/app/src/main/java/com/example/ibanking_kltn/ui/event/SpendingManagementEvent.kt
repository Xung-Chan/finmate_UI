package com.example.ibanking_kltn.ui.event

import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import java.time.LocalDate


sealed class SpendingManagementEvent {
    data class CreateSpendingSnapshot(
        val snapshotName: String,
        val budgetAmount: Long,
        val monthlySpending: LocalDate
    ) : SpendingManagementEvent()
    data class NavigateToDetail(val snapshotId: String) : SpendingManagementEvent()
    object RetryInitSpendingSnapshots : SpendingManagementEvent()
    object RefreshSpendingSnapshots : SpendingManagementEvent()
    object  ChangeAddDialogState : SpendingManagementEvent()
}

sealed class SpendingManagementEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : SpendingManagementEffect()
    data class NavigateToDetail(val route: String) : SpendingManagementEffect()
}