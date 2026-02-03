package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.dtos.responses.SpendingSnapshotResponse


enum class SpendingFundState {
    INIT,
    NONE,
    LOADING
}

data class SpendingFundUiState(
    val screenState: SpendingFundState = SpendingFundState.NONE,
    val spendingFunds: List<SpendingSnapshotResponse> = emptyList()

)
