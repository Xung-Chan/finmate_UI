package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.ibanking_kltn.dtos.definitions.AccountType
import com.example.ibanking_kltn.dtos.definitions.ServiceType
import com.example.ibanking_kltn.dtos.definitions.SortOption
import com.example.ibanking_kltn.dtos.definitions.TransactionStatus
import com.example.ibanking_kltn.dtos.requests.FilterTransactionPara
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.data.session.UserSession
import com.example.ibanking_kltn.ui.pagingsources.TransactionHistoryPagingSource
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.ui.uistates.TransactionHistoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val userSession: UserSession
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionHistoryUiState())
    val uiState: StateFlow<TransactionHistoryUiState> = _uiState.asStateFlow()



    @OptIn(ExperimentalCoroutinesApi::class)
    val transactionHistoryPager = uiState
        .map {
            FilterTransactionPara(
                fromDate = it.fromDate,
                toDate = it.toDate,
                accountType = it.accountType,
                status = it.selectedStatus,
                type = it.type,
                sortBy = it.selectedSort
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
                TransactionHistoryPagingSource(transactionRepository, filterPara)
            }.flow.cachedIn(viewModelScope)
        }

    init {
        viewModelScope.launch {
            userSession.user.collect {
                user->
                    _uiState.update {
                        it.copy(
                            myWalletNumber = user?.wallet?.walletNumber ?: ""
                        )
                    }
            }
        }

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
                accountType = AccountType.WALLET
            )
        }
        onApply(
            selectedStatus = null,
            selectedSort = SortOption.NEWEST,
            selectedService = null,
            selectedAccountType = AccountType.WALLET,
            selectedFromDate = LocalDate.now().minusMonths(1),
            selectedToDate = LocalDate.now(),
            onError = {
                onError(it)
            }
        )
    }

    fun onApply(
        selectedStatus: TransactionStatus?,
        selectedService: ServiceType?,
        selectedAccountType: AccountType,
        selectedSort: SortOption,
        selectedFromDate: LocalDate,
        selectedToDate: LocalDate,
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING,
                isShowFilter = false,
                selectedStatus = selectedStatus,
                type = selectedService,
                accountType = selectedAccountType,
                selectedSort = selectedSort,
                fromDate = selectedFromDate,
                toDate = selectedToDate
            )
        }
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
//                accountType = uiState.value.accountType.name,
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