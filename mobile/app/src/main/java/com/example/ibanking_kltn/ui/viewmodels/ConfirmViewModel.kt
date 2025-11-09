package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.requests.ConfirmTransferRequest
import com.example.ibanking_kltn.data.dtos.requests.PrepareTransferRequest
import com.example.ibanking_kltn.data.repositories.PayLaterRepository
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.data.repositories.WalletRepository
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
    private val walletRepository: WalletRepository,
    private val payLaterRepository: PayLaterRepository,

    private val sharedPreferences: SharedPreferences,
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
        expenseType: String,
        service: String
    ) {
        loadPaymentInfo(
            accountType = accountType,
            amount = amount,
            toWalletNumber = toWalletNumber,
            description = description,
            toMerchantName = toMerchantName,
            expenseType = expenseType,
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
        expenseType: String,
        service: String
    ) {
        _uiState.update {
            it.copy(
                prepareTransactionRequest = PrepareTransferRequest(
                    accountType = accountType,
                    amount = amount,
                    toWalletNumber = toWalletNumber,
                    description = description
                ),
                toMerchantName = toMerchantName,
                expenseType = expenseType,
                service = service

            )
        }
    }

    fun onOtpChange(otp: String, onSuccess: () -> Unit) {
        _uiState.value = _uiState.value.copy(otp = otp)
        if (otp.length == 6) {
            _uiState.update {
                it.copy(screenState = StateType.LOADING)
            }
            viewModelScope.launch {
                val apiResult = transactionRepository.confirmTransfer(
                    request = ConfirmTransferRequest(
                        otp = otp,
                        transactionId = uiState.value.prepareTransactionResponse!!.transactionId
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
                        Toast.makeText(context, "Xác nhận giao dịch thành công", Toast.LENGTH_SHORT)
                            .show()
                        onSuccess()
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(screenState = StateType.NONE)
                        }
                        Toast.makeText(
                            context,
                            "Có lỗi xảy ra khi xác nhận giao dịch: ${apiResult.message}",
                            Toast.LENGTH_SHORT
                        ).show()
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

    fun onConfirmClick() {
        _uiState.update {
            it.copy(confirmState = StateType.LOADING)
        }
        viewModelScope.launch {
            val apiResult = transactionRepository.prepareTransfer(
                request = uiState.value.prepareTransactionRequest!!
            )
            when (apiResult) {
                is ApiResult.Success -> {
                    val prepareTransferResponse = apiResult.data
                    _uiState.update {
                        it.copy(
                            confirmState = StateType.NONE,
                            isOtpShow = true,
                            prepareTransactionResponse = prepareTransferResponse
                        )
                    }
                    Toast.makeText(context, "Đã gửi OTP đến email của bạn", Toast.LENGTH_SHORT)
                        .show()
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(confirmState = StateType.NONE)
                    }
                    Toast.makeText(
                        context,
                        apiResult.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}