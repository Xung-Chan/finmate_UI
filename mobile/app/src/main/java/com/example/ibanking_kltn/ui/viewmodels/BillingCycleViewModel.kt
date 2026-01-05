package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.ibanking_kltn.data.dtos.SortOption
import com.example.ibanking_kltn.data.dtos.responses.BillingCycleResonse
import com.example.ibanking_kltn.data.repositories.PayLaterRepository
import com.example.ibanking_kltn.ui.pagingsources.BillingCyclePagingSource
import com.example.ibanking_kltn.ui.uistates.BillingCycleUiState
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
class BillingCycleViewModel @Inject constructor(
    private val payLaterRepository: PayLaterRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(BillingCycleUiState())
    val uiState: StateFlow<BillingCycleUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val billingCyclePager = uiState
        .map {
            it.sortBy
        }
        .distinctUntilChanged()
        .flatMapLatest { sortBy ->
            Pager(
                PagingConfig(
                    pageSize = 10,
                    prefetchDistance = 1
                )
            ) {
                BillingCyclePagingSource(
                    api = payLaterRepository,
                    sortOption = sortBy
                )
            }.flow.cachedIn(viewModelScope)
        }


    fun clearState() {
        _uiState.value = BillingCycleUiState()
    }

    fun onChangeSortOption() {
        _uiState.update {
            it.copy(
                sortBy = when (it.sortBy) {
                    SortOption.NEWEST -> SortOption.OLDEST
                    SortOption.OLDEST -> SortOption.NEWEST
                }
            )
        }
    }

    fun onSelectBillingCycle(billingCycleResonse: BillingCycleResonse?) {
        //todo
    }


}