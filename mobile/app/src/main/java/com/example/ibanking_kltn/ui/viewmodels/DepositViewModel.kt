package com.example.ibanking_kltn.ui.viewmodels


import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.dtos.definitions.TransactionStatus
import com.example.ibanking_kltn.dtos.requests.DepositTransactionRequest
import com.example.ibanking_kltn.ui.event.DepositEffect
import com.example.ibanking_kltn.ui.event.DepositEvent
import com.example.ibanking_kltn.ui.uistates.DepositUiState
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.ui.uistates.TransactionResultUiState
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DepositViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DepositUiState())
    val uiState: StateFlow<DepositUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<DepositEffect>()
    val uiEffect = _uiEffect.asSharedFlow()


    fun onEvent(event: DepositEvent) {
        when (event) {
            is DepositEvent.AmountChange -> onAmountChange(event.amount)
            is DepositEvent.ContinuePayment -> onContinuePayment(event.context)
        }
    }

    private fun openCustomTab(context: Context, url: String) {
        val customTabsIntent = CustomTabsIntent.Builder().setShowTitle(true).build()

        customTabsIntent.launchUrl(context, url.toUri())
    }

    fun isEnableContinuePayment(): Boolean {
        return uiState.value.amount >= 10000L && uiState.value.screenState != StateType.LOADING
    }

    private fun onAmountChange(amount: String) {
        val formatAmount = amount.replace(".", "").replace(",", "")
        if (formatAmount == "") {
            _uiState.update {
                it.copy(amount = 0L)
            }
            return
        }
        _uiState.update {
            it.copy(amount = formatAmount.toLong())
        }
    }

    private fun onContinuePayment(
        context: Context,
    ) {
        _uiState.update {
            it.copy(screenState = StateType.LOADING)
        }

        val request = DepositTransactionRequest(
            amount = uiState.value.amount,
        )
        viewModelScope.launch {
            val apiResult = transactionRepository.createDepositTransaction(request)
            when (apiResult) {
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.FAILED(apiResult.message)
                        )
                    }
                    _uiEffect.emit(
                        DepositEffect.ShowSnackBar(
                            SnackBarUiState(
                                message = apiResult.message,
                                type = SnackBarType.ERROR
                            )
                        )
                    )
                }

                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS
                        )
                    }
                    openCustomTab(
                        context = context, url = apiResult.data.url
                    )
                }
            }
        }

    }


}