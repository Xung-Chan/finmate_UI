package com.example.ibanking_kltn.ui.viewmodels


import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.TransactionStatus
import com.example.ibanking_kltn.data.dtos.requests.DepositTransactionRequest
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.ui.uistates.DepositUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DepositViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(DepositUiState())
    val uiState: StateFlow<DepositUiState> = _uiState.asStateFlow()

    fun openCustomTab(context: Context, url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()

        customTabsIntent.launchUrl(context, url.toUri())
    }

    fun onAmountChange(amount: String) {
        val formatAmount = amount
            .replace(".", "")
            .replace(",", "")
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

    fun onAccountTypeChange(accountType: String) {
        _uiState.update {
            it.copy(accountType = accountType)
        }
    }

    fun onContinuePayment(
        context: Context,
        onError: (String) -> Unit,
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
                    onError(apiResult.message)
                }

                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS
                        )
                    }
                    openCustomTab(
                        context = context,
                        url = apiResult.data.url
                    )
                }
            }
        }

    }

    fun handleVNPayReturn(
        vnp_ResponseCode: String,
        vnp_TxnRef: String,
        onError: (String) -> Unit,
        onNavigateToTransactionResult: (String, Boolean) -> Unit
    ) {
        viewModelScope.launch {
            var transactionId = ""
            val handleResult = transactionRepository.handleVNPayReturn(
                vnp_ResponseCode = vnp_ResponseCode,
                vnp_TxnRef = vnp_TxnRef
            )
            when (handleResult) {
                is ApiResult.Error -> {
                    onError(handleResult.message)
                    return@launch
                }

                is ApiResult.Success -> {
                    transactionId = handleResult.data
                }
            }
            val statusResult = transactionRepository.getTransactionStatus(
                transactionId = transactionId
            )
            when (statusResult) {
                is ApiResult.Error -> {
                    onError(statusResult.message)
                    return@launch
                }

                is ApiResult.Success -> {
                    val transactionHistory = statusResult.data

                    if (transactionHistory.status == TransactionStatus.COMPLETED.name) {

                    }
                    else{

                    }
                }


            }

        }
    }
}