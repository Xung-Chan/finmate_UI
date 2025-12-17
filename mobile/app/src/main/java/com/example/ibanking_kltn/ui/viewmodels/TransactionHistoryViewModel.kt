package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.ui.pagingsources.TransactionHistoryPagingSource
import com.example.ibanking_kltn.ui.uistates.TransactionHistoryUiState
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

@HiltViewModel
class TransactionHistoryViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionHistoryUiState())
    val uiState: StateFlow<TransactionHistoryUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactionHistoryPager = uiState
        .map {
            it.selectedStatus
        }
        .distinctUntilChanged()
        .flatMapLatest { status ->
            Pager(
                PagingConfig(
                    pageSize = 10,
                    prefetchDistance = 2
                )
            ) {
                TransactionHistoryPagingSource(transactionRepository, uiState.value.selectedStatus)
            }.flow.cachedIn(viewModelScope)
        }


    fun clearState() {
        _uiState.value = TransactionHistoryUiState()
    }

//    private fun countFilter() {
//        var count = 0
//        if (uiState.value.selectedStatus.isNotEmpty()) {
//            count++
//        }
//        if (uiState.value.selectedSort.isNotEmpty()) {
//            count++
//        }
//        _uiState.update {
//            it.copy(
//                filterCount = count
//            )
//        }
//    }

    fun onClickFilter() {
        _uiState.update {
            it.copy(
                isShowFilter = !it.isShowFilter
            )
        }
    }

//
//    fun onResetAll(
//        onError: (String) -> Unit
//    ) {
//        _uiState.update {
//            it.copy(
//                selectedStatus = "",
//                selectedSort = ""
//            )
//        }
//        onApply(
//            selectedStatus = "",
//            selectedSort = "",
//            onError = {
//                onError(it)
//            }
//        )
//    }
//
//    fun onApply(
//        selectedStatus: String,
//        selectedSort: String,
//        onError: (String) -> Unit
//    ) {
//        _uiState.update {
//            it.copy(
//                screenState = StateType.LOADING,
//                isShowFilter = false,
//                selectedStatus = selectedStatus,
//                selectedSort = selectedSort
//            )
//        }
//        countFilter()
//        viewModelScope.launch {
//            val request = FilterBillRequest(
//                status = uiState.value.selectedStatus,
//                page = 0
////                sortBy = uiState.value.selectedSort
//            )
//            val apiResult = billRepository.filterBill(
//                request = request
//            )
//
//            when (apiResult) {
//                is ApiResult.Success -> {
//                    _uiState.update {
//                        it.copy(
//                            screenState = StateType.SUCCESS,
//                        )
//                    }
//                }
//
//                is ApiResult.Error -> {
//                    _uiState.update {
//                        it.copy(
//                            screenState = StateType.FAILED(apiResult.message)
//                        )
//                    }
//                    onError(apiResult.message)
//                }
//
//            }
//        }
//    }


}