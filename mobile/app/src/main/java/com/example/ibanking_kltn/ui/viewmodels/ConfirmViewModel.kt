package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.Service
import com.example.ibanking_kltn.data.dtos.requests.ConfirmTransferRequest
import com.example.ibanking_kltn.data.dtos.requests.PreparePayBillRequest
import com.example.ibanking_kltn.data.dtos.requests.PrepareTransferRequest
import com.example.ibanking_kltn.data.dtos.responses.PrepareTransactionResponse
import com.example.ibanking_kltn.data.repositories.BillRepository
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.ui.uistates.ConfirmUiState
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
class ConfirmViewModel @Inject constructor(

    private val transactionRepository: TransactionRepository,
    private val billRepository: BillRepository,

    @ApplicationContext private val context: Context
) : ViewModel(), IViewModel {
    private val _uiState = MutableStateFlow(ConfirmUiState())
    val uiState: StateFlow<ConfirmUiState> = _uiState.asStateFlow()

    override fun error(message: String) {
        _uiState.update {
            it.copy(screenState = StateType.FAILED(message = message))
        }
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun init(
        accountType: String,
        amount: Long,
        toWalletNumber: String,
        description: String,
        toMerchantName: String,
        expenseType: String? = null,
        billCode: String? = null,
        service: Service
    ) {
        loadPaymentInfo(
            accountType = accountType,
            amount = amount,
            toWalletNumber = toWalletNumber,
            description = description,
            toMerchantName = toMerchantName,
            expenseType = expenseType,
            billCode = billCode,
            service = service
        )
    }

    override fun clearState() {
        _uiState.value = ConfirmUiState()
    }

    private fun loadPaymentInfo(
        accountType: String,
        amount: Long,
        toWalletNumber: String,
        description: String,
        toMerchantName: String,
        expenseType: String? = null,
        billCode: String? = null,
        service: Service
    ) {
        _uiState.update {
            it.copy(

                accountType = accountType,
                amount = amount,
                toWalletNumber = toWalletNumber,
                description = description,
                toMerchantName = toMerchantName,
                expenseType = expenseType,
                service = service,
                billCode = billCode

            )
        }
    }

    fun onOtpChange(
        otp: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        _uiState.value = _uiState.value.copy(otp = otp)
        if (otp.length == 6) {
            _uiState.update {
                it.copy(screenState = StateType.LOADING)
            }
            viewModelScope.launch {
                val apiResult = transactionRepository.confirmTransfer(
                    request = ConfirmTransferRequest(
                        otp = otp,
                        transactionId = uiState.value.prepareResponse!!.transactionId
                    )
                )
                when (apiResult) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                screenState = StateType.NONE,
                                isOtpShow = false,
                            )
                        }
                        onSuccess()
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(screenState = StateType.NONE)
                        }
                        onError(apiResult.message)
                    }
                }

            }

        }
    }

    fun onOtpDismiss() {
        _uiState.update {
            it.copy(isOtpShow = false)
        }
    }

    fun onConfirmClick(
        onSentOtp: () -> Unit,
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(confirmState = StateType.LOADING)
        }
        viewModelScope.launch {
            var apiResult: ApiResult<Any>

            when (uiState.value.service) {
                Service.TRANSFER -> {
                    apiResult = transactionRepository.prepareTransfer(
                        request = PrepareTransferRequest(
                            accountType = uiState.value.accountType,
                            amount = uiState.value.amount,
                            description = uiState.value.description,
                            toWalletNumber = uiState.value.toWalletNumber
                        )
                    )
                }

                Service.PAY_BILL -> {
                    apiResult = billRepository.preparePayBill(
                        request = PreparePayBillRequest(
                            accountType = uiState.value.accountType,
                            billerCode = uiState.value.billCode!!,
                        )
                    )
                }

                else -> {
                    error("Dịch vụ không hợp lệ")
                    return@launch
                }
            }

            when (apiResult) {
                is ApiResult.Success -> {
                    val prepareTransferResponse = apiResult.data as PrepareTransactionResponse
                    _uiState.update {
                        it.copy(
                            confirmState = StateType.NONE,
                            isOtpShow = true,
                            prepareResponse = prepareTransferResponse
                        )
                    }
                    onSentOtp()
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(confirmState = StateType.NONE)
                    }

                    onError(apiResult.message)
                }
            }
        }
    }

}