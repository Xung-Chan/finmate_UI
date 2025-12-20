package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.ibanking_kltn.data.dtos.AccountType
import com.example.ibanking_kltn.data.dtos.ServiceType
import com.example.ibanking_kltn.data.dtos.SortOption
import com.example.ibanking_kltn.data.dtos.TransactionStatus
import com.example.ibanking_kltn.data.dtos.requests.FilterTransactionPara
import com.example.ibanking_kltn.data.dtos.requests.FilterTransactionRequest
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.ui.pagingsources.TransactionHistoryPagingSource
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.ui.uistates.TransactionHistoryUiState
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
import java.time.LocalDate

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
            val filterSort = when(it.selectedSort){
                SortOption.NEWEST -> "processed_at_desc"
                SortOption.OLDEST -> "processed_at_asc"
            }
            FilterTransactionPara(
                fromDate = it.fromDate,
                toDate = it.toDate,
                accountType =it.accountType?.name,
                status = it.selectedStatus?.name,
                type = it.type?.name,
                sortBy =filterSort
            )
        }
        .distinctUntilChanged()
        .flatMapLatest { filterPara->
            Pager(
                PagingConfig(
                    pageSize = 10,
                    prefetchDistance = 1
                )
            ) {
                TransactionHistoryPagingSource(transactionRepository, filterPara)
            }.flow.cachedIn(viewModelScope)
        }


    fun clearState() {
        _uiState.value = TransactionHistoryUiState()
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
                selectedSort = SortOption.NEWEST,
                type = null,
                accountType = null
            )
        }
        onApply(
            selectedStatus = null,
            selectedSort = SortOption.NEWEST,
            selectedService = null,
            selectedAccountType = null,
            onError = {
                onError(it)
            }
        )
    }

    fun onApply(
        selectedStatus: TransactionStatus?,
        selectedService: ServiceType?,
        selectedAccountType: AccountType?,
        selectedSort: SortOption,
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING,
                isShowFilter = false,
                selectedStatus = selectedStatus,
                type=selectedService,
                accountType = selectedAccountType,
                selectedSort = selectedSort
            )
        }
        viewModelScope.launch {
            val sortBy = when (selectedSort) {
                SortOption.NEWEST -> "processed_at_desc"
                SortOption.OLDEST -> "processed_at_asc"
            }
            val request = FilterTransactionRequest(
                status = uiState.value.selectedStatus?.name,
                page = 0,
                sortBy = sortBy,
                fromDate = LocalDate.now().minusMonths(1).toString(),
                toDate = LocalDate.now().toString(),
                accountType = uiState.value.accountType?.name,
                type = uiState.value.type?.name,
                size = 10,
            )
            val apiResult = transactionRepository.getTransactionHistory(
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