package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.BillStatus
import com.example.ibanking_kltn.data.dtos.ServiceType
import com.example.ibanking_kltn.data.repositories.BillRepository
import com.example.ibanking_kltn.ui.event.PayBillEffect
import com.example.ibanking_kltn.ui.event.PayBillEvent
import com.example.ibanking_kltn.ui.uistates.BillUiState
import com.example.ibanking_kltn.ui.uistates.ConfirmContent
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
class BillViewModel @Inject constructor(
    private val billRepository: BillRepository,
    private val savedStateHandle: SavedStateHandle

) : ViewModel() {
    private val _uiState = MutableStateFlow(BillUiState())
    val uiState: StateFlow<BillUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<PayBillEffect>()
    val uiEffect = _uiEffect.asSharedFlow()


    init {
        val billCode = savedStateHandle.get<String>("billCode")
        if (!billCode.isNullOrEmpty()) {
            _uiState.update {
                it.copy(billCode = billCode)
            }
            onCheckingBill()
        }
    }

    fun onEvent(event: PayBillEvent) {
        when (event) {
            PayBillEvent.CheckingBill -> onCheckingBill()
            is PayBillEvent.ChangeBillCode -> onChangeBillCode(event.billCode)
            PayBillEvent.ConfirmPayBill -> onConfirmPayBill()
        }
    }


    private fun onCheckingBill(
    ) {
        _uiState.update {
            it.copy(screenState = StateType.LOADING)
        }
        viewModelScope.launch {
            val billResult = billRepository.getBillInfo(
                qrCode = _uiState.value.billCode
            )
            when (billResult) {
                is ApiResult.Success -> {

                    val data = billResult.data
                    if (data.billStatus != BillStatus.ACTIVE) {
                        _uiState.update {
                            it.copy(screenState = StateType.FAILED(message = "Hóa đơn không hợp lệ"))
                        }
                        _uiEffect.emit(
                            PayBillEffect.ShowSnackBar(
                                snackBar = com.example.ibanking_kltn.ui.uistates.SnackBarUiState(
                                    message = "Hóa đơn không hợp lệ",
                                    type = SnackBarType.ERROR
                                )
                            )
                        )
                        return@launch
                    }
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS,
                            checkingState = StateType.SUCCESS,
                            toMerchantName = data.merchantName,
                            toWalletNumber = data.walletNumber,
                            amount = data.amount,
                            description = data.description,
                            dueDate = data.dueDate
                        )
                    }
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(screenState = StateType.FAILED(message = billResult.message))
                    }
                    _uiEffect.emit(
                        PayBillEffect.ShowSnackBar(
                            snackBar = com.example.ibanking_kltn.ui.uistates.SnackBarUiState(
                                message = billResult.message,
                                type = SnackBarType.ERROR
                            )
                        )
                    )
                }
            }
        }
    }

    private fun onChangeBillCode(billCode: String) {
        _uiState.update {
            it.copy(
                billCode = billCode,
                checkingState = StateType.NONE,
            )
        }

    }

    private fun onConfirmPayBill() {
        val confirmContent = ConfirmContent.BILL_PAYMENT(
            amount = uiState.value.amount,
            service = ServiceType.BILL_PAYMENT,
            isVerified = true,
            billCode = uiState.value.billCode,
            toWalletNumber = uiState.value.toWalletNumber,
            toMerchantName = uiState.value.toMerchantName,
            description = uiState.value.description
        )
        viewModelScope.launch {
            _uiEffect.emit(
                PayBillEffect.NavigateToConfirmScreen(
                    confirmContent = confirmContent
                )
            )
        }
    }

}