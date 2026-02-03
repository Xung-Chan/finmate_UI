package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.dtos.responses.SpendingSnapshotDetailResponse

enum class ChartType {
    BAR,
    PIE
}
enum class SpendingDetailTab {
    OVERVIEW,
    HISTORY
}

enum class SpendingDetailState {
    INIT,
    INIT_FAILED,
    NONE,
    LOADING,
    REFRESHING,
}

data class SpendingDetailUiState(
    val snapshotId: String? = null,
    val screenState: SpendingDetailState = SpendingDetailState.NONE,
    val spendingSnapshot: SpendingSnapshotDetailResponse? = null,
    val chartType: ChartType = ChartType.PIE,
    val selectedTab: SpendingDetailTab = SpendingDetailTab.OVERVIEW,
    val isShowCategoriesDialog: Boolean = false
)
