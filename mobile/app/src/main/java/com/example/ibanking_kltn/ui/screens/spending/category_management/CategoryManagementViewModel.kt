package com.example.ibanking_kltn.ui.screens.spending.category_management

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.repositories.SpendingRepository
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.dtos.definitions.CategoryIcon
import com.example.ibanking_kltn.dtos.requests.DefinedSpendingCategoryRequest
import com.example.ibanking_kltn.dtos.responses.DefinedSpendingCategoryResponse
import com.example.ibanking_kltn.dtos.responses.ExpenseType
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_kltn.utils.toHexString
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CategoryManagementViewModel @Inject constructor(
    private val spendingRepository: SpendingRepository,
    private val transactionRepository: TransactionRepository,

    ) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryManagementUiState())
    val uiState: StateFlow<CategoryManagementUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<CategoryManagementEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        retryInit()
    }

    fun onEvent(event: CategoryManagementEvent) {
        when (event) {
            CategoryManagementEvent.RetryInitCategories -> retryInit()
            CategoryManagementEvent.AddDefinedCategory -> addNewCategory()
            CategoryManagementEvent.UpdateDefinedCategory -> updateCategory()
            CategoryManagementEvent.ResetForm -> resetForm()
            CategoryManagementEvent.ShowBottomSheet -> _uiState.update { it.copy(isShowBottomSheet = true) }
            CategoryManagementEvent.HideBottomSheet -> _uiState.update {
                it.copy(
                    isShowBottomSheet = false,
                    errorMessage = null
                )
            }

            is CategoryManagementEvent.DeleteDefinedCategory -> deleteCategory(event.categoryId)
            is CategoryManagementEvent.OpenEditDialog -> openEditDialog(event.category)
            is CategoryManagementEvent.ChangeCategoryColor -> changeCategoryColor(event.color)
            is CategoryManagementEvent.ChangeCategoryName -> changeCategoryName(event.name)
            is CategoryManagementEvent.ChangeSelectedIcon -> changeSelectedIcon(event.icon)
            is CategoryManagementEvent.ExpenseTypeChange -> changeExpenseType(event.expenseType)
        }
    }


    private fun changeExpenseType(type: ExpenseType) {
        _uiState.update {
            it.copy(
                selectedExpenseType = type
            )
        }
    }

    private fun changeCategoryName(name: String) {
        _uiState.update {
            it.copy(
                categoryName = name
            )
        }
    }

    private fun changeCategoryColor(color: String) {
        _uiState.update {
            it.copy(
                color = color
            )
        }
    }

    private fun changeSelectedIcon(icon: CategoryIcon) {
        _uiState.update {
            it.copy(
                selectedIcon = icon
            )
        }
    }

    private fun resetForm() {
        _uiState.update {
            it.copy(
                isEditMode = false,
                categoryId = "",
                categoryName = "",
                color = Color.Black.toHexString(),
                selectedIcon = CategoryIcon.UNKNOWN,
                isShowBottomSheet = true,
                errorMessage = null,
                selectedExpenseType = null
            )
        }
    }

    private fun retryInit() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    screenState = CategoryManagementState.INIT
                )
            }
            val categories = spendingRepository.getAllDefinedSpendingCategories()
            val expenseTypes = transactionRepository.getAllExpenseType()

            if (expenseTypes is ApiResult.Error) {
                _uiState.update {
                    it.copy(
                        screenState = CategoryManagementState.INIT_FAILED
                    )
                }
                _uiEffect.emit(
                    CategoryManagementEffect.ShowSnackBar(
                        SnackBarUiState(
                            message = expenseTypes.message, type = SnackBarType.ERROR
                        )
                    )
                )
                return@launch
            }

            if (categories is ApiResult.Error) {
                _uiState.update {
                    it.copy(
                        screenState = CategoryManagementState.INIT_FAILED
                    )
                }
                _uiEffect.emit(
                    CategoryManagementEffect.ShowSnackBar(
                        SnackBarUiState(
                            message = categories.message, type = SnackBarType.ERROR
                        )
                    )
                )
                return@launch
            }

            _uiState.update {
                it.copy(
                    definedCategories = (categories as ApiResult.Success).data,
                    allExpenseTypeResponse = (expenseTypes as ApiResult.Success).data,
                    screenState = CategoryManagementState.NONE
                )
            }
        }
    }

    private fun checkValidInput(): Boolean {
        if (uiState.value.categoryName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Tên danh mục không thể trống") }
            return false
        }
        if (uiState.value.selectedExpenseType == null) {
            _uiState.update { it.copy(errorMessage = "Phân loại không thể trống") }
            return false
        }
        if (uiState.value.selectedIcon == CategoryIcon.UNKNOWN) {
            _uiState.update { it.copy(errorMessage = "Vui lòng chọn một icon") }
            return false
        }
        return true
    }

    private fun addNewCategory() {
        viewModelScope.launch {
            if (!checkValidInput()) {
                return@launch
            }


            _uiState.update {
                it.copy(
                    screenState = CategoryManagementState.LOADING
                )
            }
            val randomKey = System.currentTimeMillis().toString()
            val request = DefinedSpendingCategoryRequest(
                code = "${uiState.value.selectedIcon.code}_${randomKey}",
                systemCategoryId = uiState.value.selectedExpenseType?.id,
                name = uiState.value.categoryName,
                icon = uiState.value.selectedIcon.code,
                textColor = uiState.value.color,
                backgroundColor = uiState.value.color,
            )
            val result = spendingRepository.createDefinedSpendingCategories(
                request = request
            )
            when (result) {
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = CategoryManagementState.NONE
                        )
                    }
                    _uiEffect.emit(
                        CategoryManagementEffect.ShowSnackBar(
                            SnackBarUiState(
                                message = result.message, type = SnackBarType.ERROR
                            )
                        )
                    )
                }

                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            screenState = CategoryManagementState.NONE,
                            definedCategories = it.definedCategories + result.data,
                            isShowBottomSheet = false,
                            errorMessage = null,
                        )
                    }
                }
            }
        }
    }

    private fun openEditDialog(category: DefinedSpendingCategoryResponse) {
        _uiState.update {
            it.copy(
                isEditMode = true,
                categoryId = category.id,
                categoryName = category.name,
                color = category.textColor,
                selectedIcon = CategoryIcon.fromCode(category.icon),
                selectedExpenseType = uiState.value.allExpenseTypeResponse.firstOrNull { expenseType ->
                    expenseType.tag == category.systemCategoryCode
                },
                isShowBottomSheet = true
            )
        }
    }

    private fun updateCategory() {
        viewModelScope.launch {
            if (!checkValidInput()) {
                return@launch
            }


            _uiState.update {
                it.copy(screenState = CategoryManagementState.LOADING)
            }
            val request = DefinedSpendingCategoryRequest(
                id = uiState.value.categoryId,
                code = uiState.value.selectedIcon.code,
                systemCategoryId = uiState.value.selectedExpenseType?.id,
                name = uiState.value.categoryName,
                icon = uiState.value.selectedIcon.code,
                textColor = uiState.value.color,
                backgroundColor = uiState.value.color,
            )

            val result = spendingRepository.updateDefinedSpendingCategories(request)

            when (result) {
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(screenState = CategoryManagementState.NONE)
                    }
                    _uiEffect.emit(
                        CategoryManagementEffect.ShowSnackBar(
                            SnackBarUiState(
                                message = result.message,
                                type = SnackBarType.ERROR
                            )
                        )
                    )
                }

                is ApiResult.Success -> {
                    val updatedCategories = uiState.value.definedCategories.map { category ->
                        if (category.id == uiState.value.categoryId) result.data else category
                    }
                    _uiState.update {
                        it.copy(
                            screenState = CategoryManagementState.NONE,
                            definedCategories = updatedCategories,
                            isEditMode = false,
                            categoryId = "",
                            categoryName = "",
                            color = Color.Black.toHexString(),
                            selectedIcon = CategoryIcon.UNKNOWN,
                            isShowBottomSheet = false,
                            errorMessage = null
                        )
                    }
                    _uiEffect.emit(
                        CategoryManagementEffect.ShowSnackBar(
                            SnackBarUiState(
                                message = "Cập nhật danh mục thành công",
                                type = SnackBarType.SUCCESS
                            )
                        )
                    )
                }
            }
        }
    }

    private fun deleteCategory(categoryId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(screenState = CategoryManagementState.LOADING)
            }

            val result = spendingRepository.deleteDefinedSpendingCategories(categoryId)

            when (result) {
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(screenState = CategoryManagementState.NONE)
                    }
                    _uiEffect.emit(
                        CategoryManagementEffect.ShowSnackBar(
                            SnackBarUiState(
                                message = result.message,
                                type = SnackBarType.ERROR
                            )
                        )
                    )
                }

                is ApiResult.Success -> {
                    val updatedCategories = uiState.value.definedCategories.filter {
                        it.id != categoryId
                    }

                    _uiState.update {
                        it.copy(
                            screenState = CategoryManagementState.NONE,
                            definedCategories = updatedCategories
                        )
                    }
                    _uiEffect.emit(
                        CategoryManagementEffect.ShowSnackBar(
                            SnackBarUiState(
                                message = "Xóa danh mục thành công",
                                type = SnackBarType.SUCCESS
                            )
                        )
                    )
                }
            }
        }
    }


}