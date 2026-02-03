package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.ibanking_kltn.data.repositories.SpendingRepository
import com.example.ibanking_kltn.dtos.definitions.NavKey
import com.example.ibanking_kltn.ui.event.SpendingDetailEffect
import com.example.ibanking_kltn.ui.event.SpendingDetailEvent
import com.example.ibanking_kltn.ui.pagingsources.SpendingHistoryPagingSource
import com.example.ibanking_kltn.ui.uistates.ChartType
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.uistates.SpendingDetailState
import com.example.ibanking_kltn.ui.uistates.SpendingDetailTab
import com.example.ibanking_kltn.ui.uistates.SpendingDetailUiState
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
            SpendingDetailEvent.AddTransaction -> TODO()
            SpendingDetailEvent.ViewCategories -> toggleCategoriesDialog()
        }
    }

    private fun retryLoadData(
    ) {
        _uiState.update {
            it.copy(
                screenState = SpendingDetailState.INIT
            )
        }
        viewModelScope.launch {
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

    private fun toggleCategoriesDialog() {
        _uiState.update {
            it.copy(
                isShowCategoriesDialog = !it.isShowCategoriesDialog
            )
        }
    }

}