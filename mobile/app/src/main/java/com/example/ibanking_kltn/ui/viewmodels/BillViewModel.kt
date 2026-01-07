package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.BillStatus
import com.example.ibanking_kltn.data.repositories.BillRepository
import com.example.ibanking_kltn.ui.uistates.BillUiState
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
class BillViewModel @Inject constructor(

    private val billRepository: BillRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(BillUiState())
    val uiState: StateFlow<BillUiState> = _uiState.asStateFlow()


    fun clearState() {
        _uiState.value = BillUiState()
    }

    fun init() {
        clearState()
    }


    fun onCheckingBill(
        onError: (String) -> Unit
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
                        onError("Hóa đơn không hợp lệ")
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
                    onError(billResult.message)
                }
            }
        }
    }

    fun onChangeBillCode(billCode: String) {
        _uiState.update {
            it.copy(
                billCode = billCode,
                checkingState = StateType.NONE,
            )
        }

    }


}