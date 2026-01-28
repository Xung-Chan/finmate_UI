package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.AccountType
import com.example.ibanking_kltn.data.dtos.PaymentAccount
import com.example.ibanking_kltn.data.dtos.TransactionStatus
import com.example.ibanking_kltn.data.dtos.requests.ConfirmTransferRequest
import com.example.ibanking_kltn.data.dtos.requests.PreparePayBillRequest
import com.example.ibanking_kltn.data.dtos.requests.PreparePrePaymentRequest
import com.example.ibanking_kltn.data.dtos.requests.PrepareTransferRequest
import com.example.ibanking_kltn.data.dtos.responses.PrepareTransactionResponse
import com.example.ibanking_kltn.data.repositories.BillRepository
import com.example.ibanking_kltn.data.repositories.PayLaterRepository
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.data.repositories.WalletRepository
import com.example.ibanking_kltn.ui.Screens
import com.example.ibanking_kltn.ui.event.ConfirmEffect
import com.example.ibanking_kltn.ui.event.ConfirmEvent
import com.example.ibanking_kltn.ui.uistates.ConfirmContent
import com.example.ibanking_kltn.ui.uistates.ConfirmUiState
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ConfirmViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val payLaterRepository: PayLaterRepository,
    private val transactionRepository: TransactionRepository,
    private val billRepository: BillRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConfirmUiState())
    val uiState: StateFlow<ConfirmUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<ConfirmEffect>()
    val uiEffect = _uiEffect.asSharedFlow()


    fun onEvent(event: ConfirmEvent) {
        when (event) {
            is ConfirmEvent.Init -> loadPaymentInfo(event.confirmContent)
            is ConfirmEvent.SelectAccountType -> onSelectAccountType(event.account)
            is ConfirmEvent.OtpChange -> onOtpChange(event.otp)
            ConfirmEvent.ConfirmClick -> onConfirmClick()
            ConfirmEvent.OtpDismiss -> onOtpDismiss()
        }
    }


    fun clearState() {
        _uiState.value = ConfirmUiState()
    }

    private fun loadPaymentInfo(
        confirmContent: ConfirmContent
    ) {
        _uiState.update {
            it.copy(
                confirmContent = confirmContent,
            )
        }
        loadAvailableAccount(isVerified = uiState.value.confirmContent?.isVerified ?: false)
    }

    private fun loadAvailableAccount(isVerified: Boolean) {

        _uiState.update {
            it.copy(screenState = StateType.LOADING)
        }
        val availableAccount = mutableListOf<PaymentAccount>()
        viewModelScope.launch {
            repeat(3) {
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
                        return@launch
                    }

                    is ApiResult.Error -> {
                    }
                }
            }
        }
        if (isVerified) {
            viewModelScope.launch {
                repeat(3) {
                    val payLaterInfo = payLaterRepository.getMyPayLater()
                    when (payLaterInfo) {
                        is ApiResult.Success -> {
                            availableAccount.add(
                                PaymentAccount(
                                    accountType = AccountType.PAY_LATER,
                                    accountNumber = payLaterInfo.data.walletNumber,
                                    merchantName = "Pay Later",
                                    balance = (payLaterInfo.data.creditLimit - payLaterInfo.data.usedCredit).toLong()
                                )
                            )
                            return@launch
                        }

                        is ApiResult.Error -> {
                            //todo retry
                        }
                    }
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

    private fun onOtpChange(
        otp: String,
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
                        val route =
                            "${Screens.TransactionResult.name}?status=${TransactionStatus.COMPLETED}&service=${uiState.value.confirmContent?.service?.serviceName}&amount=${uiState.value.confirmContent?.amount}"
                        _uiState.update {
                            it.copy(
                                screenState = StateType.NONE,
                                isOtpShow = false,
                            )
                        }
                        _uiEffect.emit(
                            ConfirmEffect.PaymentSuccess(route)
                        )
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(screenState = StateType.NONE)
                        }
                        _uiEffect.emit(
                            ConfirmEffect.ShowSnackBar(
                                snackBar = SnackBarUiState(
                                    message = apiResult.message,
                                    type = SnackBarType.ERROR
                                )
                            )
                        )
                    }
                }

            }

        }
    }

    private fun onOtpDismiss() {
        _uiState.update {
            it.copy(isOtpShow = false)
        }
    }

    private fun onConfirmClick(
    ) {
        _uiState.update {
            it.copy(confirmState = StateType.LOADING)
        }
        viewModelScope.launch {
            var apiResult: ApiResult<Any>

            when (uiState.value.confirmContent) {
                is ConfirmContent.TRANSFER -> {
                    val transferData = uiState.value.confirmContent as ConfirmContent.TRANSFER
                    apiResult = transactionRepository.prepareTransfer(
                        request = PrepareTransferRequest(
                            accountType = uiState.value.accountType!!.accountType.name,
                            amount = (uiState.value.confirmContent as ConfirmContent.TRANSFER).amount,
                            description = transferData.description,
                            toWalletNumber = transferData.toWalletNumber,
                            expenseTypeId = transferData.expenseTypeId
                        )
                    )
                }

                is ConfirmContent.BILL_PAYMENT -> {
                    apiResult = billRepository.preparePayBill(
                        request = PreparePayBillRequest(
                            accountType = uiState.value.accountType!!.accountType.name,
                            billerCode = (uiState.value.confirmContent as ConfirmContent.BILL_PAYMENT).billCode,
                        )
                    )
                }

                is ConfirmContent.BILL_REPAYMENT -> {
                    val request = PreparePrePaymentRequest(
                        amount = (uiState.value.confirmContent as ConfirmContent.BILL_REPAYMENT).amount,
                        billerCode = (uiState.value.confirmContent as ConfirmContent.BILL_REPAYMENT).billCode
                    )
                    apiResult = billRepository.preparePrepayment(
                        request = request
                    )
                }

                else -> {
                    _uiEffect.emit(
                        ConfirmEffect.ShowSnackBar(
                            snackBar = SnackBarUiState(
                                message = "Dịch vụ không hợp lệ",
                                type = SnackBarType.ERROR
                            )
                        )
                    )
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
                    _uiEffect.emit(
                        ConfirmEffect.ShowSnackBar(
                            snackBar = SnackBarUiState(
                                message = "Chúng tôi đã gửi mã OTP đến email của bạn",
                                type = SnackBarType.INFO
                            )
                        )
                    )

                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(confirmState = StateType.NONE)
                    }

                    _uiEffect.emit(
                        ConfirmEffect.ShowSnackBar(
                            snackBar = SnackBarUiState(
                                message = apiResult.message,
                                type = SnackBarType.ERROR
                            )
                        )
                    )
                }
            }
        }
    }

    private fun onSelectAccountType(account: PaymentAccount) {
        _uiState.update {
            it.copy(
                accountType = account,
                balance = account.balance
            )
        }
    }

}