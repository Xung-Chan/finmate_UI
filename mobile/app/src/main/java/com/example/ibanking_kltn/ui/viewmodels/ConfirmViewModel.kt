package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.AccountType
import com.example.ibanking_kltn.data.dtos.PaymentAccount
import com.example.ibanking_kltn.data.dtos.ServiceType
import com.example.ibanking_kltn.data.dtos.requests.ConfirmTransferRequest
import com.example.ibanking_kltn.data.dtos.requests.PreparePayBillRequest
import com.example.ibanking_kltn.data.dtos.requests.PrepareTransferRequest
import com.example.ibanking_kltn.data.dtos.responses.PrepareTransactionResponse
import com.example.ibanking_kltn.data.repositories.BillRepository
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
    private val walletRepository: WalletRepository,
    private val payLaterRepository: PayLaterRepository,
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
        amount: Long,
        toWalletNumber: String,
        description: String,
        toMerchantName: String,
        expenseType: String? = null,
        billCode: String? = null,
        service: ServiceType,
        isVerified: Boolean = false
    ) {
        loadPaymentInfo(
            amount = amount,
            toWalletNumber = toWalletNumber,
            description = description,
            toMerchantName = toMerchantName,
            expenseType = expenseType,
            billCode = billCode,
            service = service,
        )
        loadAvailableAccount(isVerified)
    }

    override fun clearState() {
        _uiState.value = ConfirmUiState()
    }

    private fun loadPaymentInfo(
        amount: Long,
        toWalletNumber: String,
        description: String,
        toMerchantName: String,
        expenseType: String? = null,
        billCode: String? = null,
        service: ServiceType,
    ) {
        _uiState.update {
            it.copy(
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

    private fun loadAvailableAccount(isVerified: Boolean) {

        _uiState.update {
            it.copy(screenState = StateType.LOADING)
        }
        val availableAccount = mutableListOf<PaymentAccount>()
        viewModelScope.launch {
            val apiResult = walletRepository.getMyWalletInfor()
            when (apiResult) {
                is ApiResult.Success -> {
                    availableAccount.add(
                        PaymentAccount(
                            accountType = AccountType.WALLET,
                            accountNumber = apiResult.data.walletNumber,
                            merchantName = apiResult.data.merchantName,
                            balance = apiResult.data.balance.toLong()
                        )
                    )
                }

                is ApiResult.Error -> {
                    //todo retry
                }
            }
            if (isVerified) {
                val payLaterInfo = payLaterRepository.getMyPayLater()
                when (payLaterInfo) {
                    is ApiResult.Success -> {
                        availableAccount.add(
                            PaymentAccount(
                                accountType = AccountType.PAY_LATER,
                                accountNumber = payLaterInfo.data.walletNumber,
                                merchantName = "Pay Later",
                                balance = payLaterInfo.data.availableCredit.toLong()
                            )
                        )
                    }

                    is ApiResult.Error -> {
                        //todo retry
                    }
                }

            }

            _uiState.update {
                it.copy(
                    screenState = StateType.SUCCESS,
                    availableAccount = availableAccount
                )
            }
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
                ServiceType.TRANSFER -> {
                    apiResult = transactionRepository.prepareTransfer(
                        request = PrepareTransferRequest(
                            accountType = uiState.value.accountType!!.accountType.name,
                            amount = uiState.value.amount,
                            description = uiState.value.description,
                            toWalletNumber = uiState.value.toWalletNumber
                        )
                    )
                }

                ServiceType.BILL_PAYMENT -> {
                    apiResult = billRepository.preparePayBill(
                        request = PreparePayBillRequest(
                            accountType = uiState.value.accountType!!.accountType.name,
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
    fun onSelectAccountType(account: PaymentAccount) {
        _uiState.update {
            it.copy(
                accountType = account,
                balance = account.balance
            )
        }
    }

}