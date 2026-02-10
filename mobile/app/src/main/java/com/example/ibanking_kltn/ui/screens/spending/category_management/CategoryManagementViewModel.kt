package com.example.ibanking_kltn.ui.screens.spending.category_management

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.repositories.SpendingRepository
import com.example.ibanking_kltn.dtos.definitions.CategoryIcon
import com.example.ibanking_kltn.dtos.requests.DefinedSpendingCategoryRequest
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
    private val spendingRepository: SpendingRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryManagementUiState())
    val uiState: StateFlow<CategoryManagementUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<CategoryManagementEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        retryInitCategories()

    }

    fun onEvent(event: CategoryManagementEvent) {
        when (event) {
            CategoryManagementEvent.RetryInitCategories -> retryInitCategories()
            CategoryManagementEvent.AddDefinedCategory -> addNewCategory()
            CategoryManagementEvent.UpdateDefinedCategory -> updateCategory()
            CategoryManagementEvent.ResetForm -> resetForm()
            is CategoryManagementEvent.DeleteDefinedCategory -> deleteCategory(event.categoryId)
            is CategoryManagementEvent.OpenEditDialog -> openEditDialog(event.category)
            is CategoryManagementEvent.ChangeCategoryColor -> changeCategoryColor(event.color)
            is CategoryManagementEvent.ChangeCategoryName -> changeCategoryName(event.name)
            is CategoryManagementEvent.ChangeSelectedIcon -> changeSelectedIcon(event.icon)
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
                selectedIcon = CategoryIcon.UNKNOWN
            )
        }
    }

    private fun retryInitCategories() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    screenState = CategoryManagementState.INIT
                )
            }
            val categories = spendingRepository.getAllDefinedSpendingCategories()
            when (categories) {
                is ApiResult.Error -> {
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
                }

                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            definedCategories = categories.data,
                            screenState = CategoryManagementState.NONE
                        )
                    }
                }
            }
        }
    }


    private fun addNewCategory() {
        viewModelScope.launch {

            if (
                uiState.value.categoryName.isBlank()
            ) {
                _uiEffect.emit(
                    CategoryManagementEffect.ShowSnackBar(
                        SnackBarUiState(
                            message = "Please fill all the fields", type = SnackBarType.ERROR
                        )
                    )
                )
                return@launch
            }
            _uiState.update {
                it.copy(
                    screenState = CategoryManagementState.LOADING
                )
            }
            val request = DefinedSpendingCategoryRequest(
                code = uiState.value.selectedIcon.code,
                //todo
                systemCategoryId = null,
                name = uiState.value.categoryName,
                icon = uiState.value.selectedIcon.code,
                textColor = uiState.value.color,
                backgroundColor = uiState.value.color,
            )
            val result = spendingRepository.createDefinedSpendingCategories(
                request = request
            )
            when(result){
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
                            definedCategories = it.definedCategories + result.data
                        )
                    }
                }
            }
        }
    }

    private fun openEditDialog(category: com.example.ibanking_kltn.dtos.responses.DefinedSpendingCategoryResponse) {
        _uiState.update {
            it.copy(
                isEditMode = true,
                categoryId = category.id,
                categoryName = category.name,
                color = category.textColor ?: Color.Black.toHexString(),
                selectedIcon = CategoryIcon.fromCode(category.icon)
            )
        }
    }

    private fun updateCategory() {
        viewModelScope.launch {
            if (uiState.value.categoryName.isBlank()) {
                _uiEffect.emit(
                    CategoryManagementEffect.ShowSnackBar(
                        SnackBarUiState(
                            message = "Vui lòng điền đầy đủ thông tin",
                            type = SnackBarType.ERROR
                        )
                    )
                )
                return@launch
            }

            _uiState.update {
                it.copy(screenState = CategoryManagementState.LOADING)
            }

            val request = DefinedSpendingCategoryRequest(
                id= uiState.value.categoryId,
                code = uiState.value.selectedIcon.code,
                systemCategoryId = null,
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
                        if (category.id == uiState.value.categoryId) {
                            result.data
                        } else {
                            category
                        }
                    }

                    _uiState.update {
                        it.copy(
                            screenState = CategoryManagementState.NONE,
                            definedCategories = updatedCategories,
                            isEditMode = false,
                            categoryId = "",
                            categoryName = "",
                            color = Color.Black.toHexString(),
                            selectedIcon = CategoryIcon.UNKNOWN
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