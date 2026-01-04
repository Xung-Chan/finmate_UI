package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.ibanking_kltn.data.dtos.PayLaterApplicationStatus
import com.example.ibanking_kltn.data.dtos.PayLaterApplicationType
import com.example.ibanking_kltn.data.dtos.requests.FilterPayLaterApplicationPara
import com.example.ibanking_kltn.data.repositories.PayLaterRepository
import com.example.ibanking_kltn.ui.pagingsources.PayLaterApplicationHistoryPagingSource
import com.example.ibanking_kltn.ui.uistates.PayLaterApplicationHistoryUiState
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
import java.time.LocalDate

@HiltViewModel
class PayLaterApplicationHistoryViewModel @Inject constructor(
    private val payLaterRepository: PayLaterRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(PayLaterApplicationHistoryUiState())
    val uiState: StateFlow<PayLaterApplicationHistoryUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val payLaterApplicationHistoryPager = uiState
        .map {
            //!
            FilterPayLaterApplicationPara(
                fromDate = it.fromDate,
                toDate = it.toDate,
                status = it.selectedStatus,
                type = it.selectedType
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
                PayLaterApplicationHistoryPagingSource(
                    api = payLaterRepository,
                    filterPara = filterPara
                )
            }.flow.cachedIn(viewModelScope)
        }


    fun clearState() {
        _uiState.value = PayLaterApplicationHistoryUiState()
    }


    fun onApply(
        selectedStatus: PayLaterApplicationStatus?,
        selectedType: PayLaterApplicationType?,
        fromDate: LocalDate,
        toDate: LocalDate,
    ) {
//        _uiState.update {
//            it.copy(
//                screenState = StateType.LOADING,
//                isShowFilter = false,
//                selectedStatus = selectedStatus,
//                type=selectedService,
//                accountType = selectedAccountType,
//                selectedSort = selectedSort
//            )
//        }
//        viewModelScope.launch {
//            val sortBy = when (selectedSort) {
//                SortOption.NEWEST -> "processed_at_desc"
//                SortOption.OLDEST -> "processed_at_asc"
//            }
//            val request = FilterTransactionRequest(
//                status = uiState.value.selectedStatus?.name,
//                page = 0,
//                sortBy = sortBy,
//                fromDate = LocalDate.now().minusMonths(1).toString(),
//                toDate = LocalDate.now().toString(),
//                accountType = uiState.value.accountType?.name,
//                type = uiState.value.type?.name,
//                size = 10,
//            )
//            val apiResult = transactionRepository.getTransactionHistory(
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
    }


}