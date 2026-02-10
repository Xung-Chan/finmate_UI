package com.example.ibanking_kltn.ui.screens.spending.category_management

import androidx.compose.ui.graphics.Color
import com.example.ibanking_kltn.dtos.definitions.CategoryIcon
import com.example.ibanking_kltn.dtos.responses.DefinedSpendingCategoryResponse
import com.example.ibanking_kltn.utils.toHexString

enum class CategoryManagementState {
    INIT,
    INIT_FAILED,
    NONE,
    LOADING
}

data class CategoryManagementUiState(
    val screenState: CategoryManagementState = CategoryManagementState.NONE,
    val definedCategories: List<DefinedSpendingCategoryResponse> = emptyList(),
    val categoryId: String = "",
    val selectedIcon: CategoryIcon = CategoryIcon.UNKNOWN,
    val categoryName: String = "",
    val color: String = Color.Black.toHexString(),
    val isEditMode: Boolean = false,
)
