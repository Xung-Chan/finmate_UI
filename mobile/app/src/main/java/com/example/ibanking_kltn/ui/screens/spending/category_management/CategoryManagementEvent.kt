package com.example.ibanking_kltn.ui.screens.spending.category_management

import com.example.ibanking_kltn.dtos.definitions.CategoryIcon
import com.example.ibanking_kltn.dtos.responses.DefinedSpendingCategoryResponse
import com.example.ibanking_kltn.dtos.responses.ExpenseType
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class CategoryManagementEvent {
    object RetryInitCategories : CategoryManagementEvent()
    object AddDefinedCategory : CategoryManagementEvent()
    object UpdateDefinedCategory : CategoryManagementEvent()
    object ResetForm : CategoryManagementEvent()
    object ShowBottomSheet : CategoryManagementEvent()
    object HideBottomSheet : CategoryManagementEvent()
    data class DeleteDefinedCategory(val categoryId: String) : CategoryManagementEvent()
    data class OpenEditDialog(val category: DefinedSpendingCategoryResponse) : CategoryManagementEvent()
    data class ChangeCategoryName(val name: String) : CategoryManagementEvent()
    data class ChangeCategoryColor(val color: String) : CategoryManagementEvent()
    data class ChangeSelectedIcon(val icon: CategoryIcon) : CategoryManagementEvent()
    data class ExpenseTypeChange(val expenseType: ExpenseType) : CategoryManagementEvent()
}

sealed class CategoryManagementEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : CategoryManagementEffect()
}