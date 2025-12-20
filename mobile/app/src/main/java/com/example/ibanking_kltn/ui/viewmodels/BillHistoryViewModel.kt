package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.ibanking_kltn.data.dtos.BillStatus
import com.example.ibanking_kltn.data.dtos.SortOption
import com.example.ibanking_kltn.data.dtos.requests.FilterBillParam
import com.example.ibanking_kltn.data.dtos.requests.FilterBillRequest
import com.example.ibanking_kltn.data.repositories.BillRepository
import com.example.ibanking_kltn.ui.pagingsources.BillHistoryPagingSource
import com.example.ibanking_kltn.ui.uistates.BillHistoryUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class BillHistoryViewModel @Inject constructor(
    private val billRepository: BillRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(BillHistoryUiState())
    val uiState: StateFlow<BillHistoryUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val billPager = uiState
        .map {

            val filterSort = when (it.selectedSort) {
                SortOption.NEWEST -> "date_desc"
                SortOption.OLDEST -> "date_asc"
            }
            return@map FilterBillParam(
                status = uiState.value.selectedStatus?.name,
                sortBy = filterSort
            )
        }
        .distinctUntilChanged()
        .flatMapLatest { filterPara ->
            Pager(
                PagingConfig(
                    pageSize = 10,
                    prefetchDistance = 1
                )
            ) {
                BillHistoryPagingSource(billRepository, filterPara)
            }.flow.cachedIn(viewModelScope)
        }


    fun clearState() {
        _uiState.value = BillHistoryUiState()
    }


    fun onClickFilter() {
        _uiState.update {
            it.copy(
                isShowFilter = !it.isShowFilter
            )
        }
    }


    fun onResetAll(
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                selectedStatus = null,
                selectedSort = SortOption.NEWEST
            )
        }
        onApply(
            selectedStatus = null,
            selectedSort = SortOption.NEWEST,
            onError = {
                onError(it)
            }
        )
    }

    fun onApply(
        selectedStatus: BillStatus?,
        selectedSort: SortOption,
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING,
                isShowFilter = false,
                selectedStatus = selectedStatus,
                selectedSort = selectedSort
            )
        }
        viewModelScope.launch {
            val sortBy = when (selectedSort) {
                SortOption.NEWEST -> "date_desc"
                SortOption.OLDEST -> "date_asc"
            }
            val request = FilterBillRequest(
                status = uiState.value.selectedStatus?.name,
                page = 0,
                sortBy = sortBy
            )
            val apiResult = billRepository.filterBill(
                request = request
            )

            when (apiResult) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS,
                        )
                    }
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.FAILED(apiResult.message)
                        )
                    }
                    onError(apiResult.message)
                }

            }
        }
    }


}