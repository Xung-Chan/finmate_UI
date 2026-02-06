package com.example.ibanking_kltn.ui.screens.spending.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.ibanking_kltn.data.repositories.SpendingRepository
import com.example.ibanking_kltn.dtos.definitions.NavKey
import com.example.ibanking_kltn.ui.paging_sources.SpendingHistoryPagingSource
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal

@HiltViewModel
class SpendingDetailViewModel @Inject constructor(
    private val spendingRepository: SpendingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(SpendingDetailUiState())
    val uiState: StateFlow<SpendingDetailUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SpendingDetailEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        val snapshotId =
            savedStateHandle.get<String>(NavKey.SPENDING_SNAPSHOT_ID.name)
        if (snapshotId != null) {
            _uiState.update {
                it.copy(
                    snapshotId = snapshotId,
                )

            }
            retryLoadData()
        }


    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val records = uiState
        .map {
            uiState.value.spendingSnapshot?.monthlySpending
        }
        .distinctUntilChanged()
        .flatMapLatest { monthlySpending ->
            monthlySpending?.let {
                Pager(
                    PagingConfig(
                        pageSize = 10,
                        prefetchDistance = 1
                    )
                ) {
                    SpendingHistoryPagingSource(spendingRepository, monthlySpending)
                }.flow.cachedIn(viewModelScope)
            } ?: flowOf()
        }

    fun onEvent(event: SpendingDetailEvent) {
        when (event) {
            SpendingDetailEvent.RetryLoadData -> retryLoadData()
            SpendingDetailEvent.ChangeChartType -> changeChartType()
            SpendingDetailEvent.ChangeTab -> changeTab()
            SpendingDetailEvent.AddTransaction -> navigateToAddTransaction()
            SpendingDetailEvent.ViewCategories -> navigateToCategory()
            SpendingDetailEvent.AddSpendingCategory -> addCategory()
            SpendingDetailEvent.ChangeVisibleAddDialog -> changeVisibleAddDialog()
            is SpendingDetailEvent.ChangeCategoryBudget -> changeCategoryBudget(event.categoryBudget)
            is SpendingDetailEvent.ChangeCategoryIcon -> changeCategoryIcon(event.icon)
            is SpendingDetailEvent.ChangeCategoryName -> changeCategoryName(event.categoryName)
            is SpendingDetailEvent.DeleteSpendingCategory -> deleteCategory(event.categoryCode)
        }
    }

    private fun retryLoadData(
    ) {
        if (uiState.value.spendingSnapshot == null) {


            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        screenState = SpendingDetailState.INIT
                    )
                }
                val snapshot = spendingRepository.getDetailSpendingSnapshot(
                    snapshotId = uiState.value.snapshotId!!
                )
                when (snapshot) {
                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                screenState = SpendingDetailState.INIT_FAILED
                            )
                        }
                        _uiEffect.emit(
                            SpendingDetailEffect.ShowSnackBar(
                                SnackBarUiState(
                                    message = snapshot.message, type = SnackBarType.ERROR
                                )
                            )
                        )
                    }

                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                spendingSnapshot = snapshot.data,
                                screenState = SpendingDetailState.NONE
                            )
                        }
                    }
                }
            }
        }
        if (uiState.value.definedCategories.isEmpty()) {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        screenState = SpendingDetailState.INIT
                    )
                }
                val apiResult = spendingRepository.getAllDefinedSpendingCategories()
                when (apiResult) {
                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                screenState = SpendingDetailState.INIT_FAILED
                            )
                        }
                        _uiEffect.emit(
                            SpendingDetailEffect.ShowSnackBar(
                                SnackBarUiState(
                                    message = apiResult.message, type = SnackBarType.ERROR
                                )
                            )
                        )
                    }

                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                definedCategories = apiResult.data,
                                screenState = SpendingDetailState.NONE
                            )
                        }
                    }
                }
            }
        }

    }

    private fun changeChartType() {
        _uiState.update {
            it.copy(
                chartType = when (it.chartType) {
                    ChartType.BAR -> ChartType.PIE
                    ChartType.PIE -> ChartType.BAR
                }
            )
        }
    }

    private fun changeTab() {
        _uiState.update {
            it.copy(
                selectedTab = when (it.selectedTab) {
                    SpendingDetailTab.OVERVIEW -> SpendingDetailTab.HISTORY
                    SpendingDetailTab.HISTORY -> SpendingDetailTab.OVERVIEW
                }
            )
        }
    }

    private fun navigateToCategory() {
        viewModelScope.launch {

            _uiEffect.emit(
                SpendingDetailEffect.NavigateToCategory
            )
        }
    }

    private fun navigateToAddTransaction() {
        viewModelScope.launch {

            _uiEffect.emit(
                SpendingDetailEffect.NavigateToAddTransaction
            )
        }
    }

    private fun addCategory() {
        //todo
    }

    private fun deleteCategory(categoryCode: String) {
        //todo
    }

    private fun changeCategoryName(categoryName: String) {
        _uiState.update {
            it.copy(
                categoryBudgetName = categoryName
            )
        }
    }

    private fun changeCategoryBudget(categoryBudget: String) {
        val formatAmount = categoryBudget
            .replace(".", "")
            .replace(",", "")
        if (formatAmount == "") {
            _uiState.update {
                it.copy(categoryBudget = BigDecimal.ZERO)
            }
            return
        }
        _uiState.update {
            it.copy(categoryBudget = formatAmount.toBigDecimal())
        }

    }

    private fun changeCategoryIcon(icon: String) {
        _uiState.update {
            it.copy(
                //todo
            )
        }
    }

    private fun changeVisibleAddDialog() {
        _uiState.update {
            it.copy(
                isShowAddCategoryDialog = !it.isShowAddCategoryDialog
            )
        }
    }

}