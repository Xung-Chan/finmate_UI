package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.dtos.responses.SpendingSnapshotResponse


enum class SpendingManagementState {
    INIT,
    INIT_FAILED,
    REFRESHING,
    NONE,
    LOADING
}

data class SpendingUiState(
    val screenState: SpendingManagementState = SpendingManagementState.NONE,
    val spendingSnapshots: List<SpendingSnapshotResponse> = emptyList(),
    val isShowAddDialog : Boolean = false

)
