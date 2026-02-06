package com.example.ibanking_kltn.ui.screens.pay_later.billing_cycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.ibanking_kltn.dtos.definitions.ServiceType
import com.example.ibanking_kltn.dtos.definitions.SortOption
import com.example.ibanking_kltn.dtos.responses.BillingCycleResonse
import com.example.ibanking_kltn.data.repositories.PayLaterRepository
import com.example.ibanking_kltn.ui.paging_sources.BillingCyclePagingSource
import com.example.ibanking_kltn.ui.screens.confirm_transaction.ConfirmContent
import com.example.ibanking_kltn.utils.formatterDateString
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class BillingCycleViewModel @Inject constructor(
    private val payLaterRepository: PayLaterRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BillingCycleUiState())
    val uiState: StateFlow<BillingCycleUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<BillingCycleEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

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


    fun onEvent(event: BillingCycleEvent) {
        when (event) {
            BillingCycleEvent.ChangeSortingOption -> onChangeSortOption()
            is BillingCycleEvent.SelectBillingCycle -> onSelectBillingCycle(event.billingCycle)
            is BillingCycleEvent.RepayBill -> onClickPayBill(
                billingCycle = event.billingCycle,
                payAmount = event.payAmount,
                totalDept = event.totalDept
            )
        }
    }


    private fun onChangeSortOption() {
        _uiState.update {
            it.copy(
                sortBy = when (it.sortBy) {
                    SortOption.NEWEST -> SortOption.OLDEST
                    SortOption.OLDEST -> SortOption.NEWEST
                }
            )
        }
    }

    private fun onSelectBillingCycle(billingCycleResponse: BillingCycleResonse?) {
        _uiState.update {
            it.copy(
                selectedBillingCycle = billingCycleResponse
            )
        }
    }


    private fun onClickPayBill(
        billingCycle: BillingCycleResonse, payAmount: Long, totalDept: Long
    ) {
        val confirmContent = ConfirmContent.BILL_REPAYMENT(
            billCode = billingCycle.code,
            term = "${
                formatterDateString(
                    LocalDate.parse(
                        billingCycle.startDate
                    )
                )
            } - ${
                formatterDateString(
                    LocalDate.parse(
                        billingCycle.endDate
                    )
                )
            }",
            dueDate = formatterDateString(
                LocalDate.parse(
                    billingCycle.dueDate
                )
            ),
            totalDept = totalDept,
            afterRepayDept = totalDept - payAmount,
            amount = payAmount,
            service = ServiceType.PAY_LATER_REPAYMENT,
        )
        viewModelScope.launch {
            _uiEffect.emit(
                BillingCycleEffect.NavigateToConfirmScreen(
                    confirmContent = confirmContent
                )
            )
        }
    }


}