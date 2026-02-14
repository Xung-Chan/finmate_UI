package com.example.ibanking_kltn.ui.screens.spending.snapshot_detail

import com.example.ibanking_kltn.dtos.responses.DefinedSpendingCategoryResponse
import com.example.ibanking_kltn.dtos.responses.SpendingAnalyzeResponse
import com.example.ibanking_kltn.dtos.responses.SpendingSnapshotDetailResponse
import java.math.BigDecimal

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
    ANALYZING,
}

data class SpendingDetailUiState(
    val snapshotId: String? = null,
    val screenState: SpendingDetailState = SpendingDetailState.NONE,
    val spendingSnapshot: SpendingSnapshotDetailResponse? = null,
    val chartType: ChartType = ChartType.PIE,
    val selectedTab: SpendingDetailTab = SpendingDetailTab.OVERVIEW,

    // Category Management
    val isShowAddCategoryDialog: Boolean = false,
    val isShowEditCategoryDialog: Boolean = false,
    val categoryId: String = "",
    val selectedIconCode: String = "",
    val selectedIconColor: String = "#000000",
    val categoryBudget: BigDecimal = BigDecimal.ZERO,
    val categoryBudgetName: String = "",
    val editingCategoryCode: String? = null,

    val definedCategories: List<DefinedSpendingCategoryResponse> = listOf(),

    val analyzeResponse : SpendingAnalyzeResponse? = null,
)
