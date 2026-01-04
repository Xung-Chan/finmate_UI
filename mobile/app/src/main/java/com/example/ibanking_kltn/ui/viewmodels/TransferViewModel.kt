package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.SavedReceiver
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.data.repositories.WalletRepository
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.ui.uistates.TransferUiState
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val walletRepository: WalletRepository,
    ) : ViewModel() {
    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState.asStateFlow()


    fun init() {
        loadExpenseType()
    }


     fun clearState() {
        _uiState.value = TransferUiState()
    }



    fun isEnableContinue(): Boolean {
        val data = uiState.value
        val result = data.toMerchantName.isNotEmpty() &&
                data.amount > 0L &&
                data.expenseType.isNotEmpty()

        return result
    }


    fun loadExpenseType() {
        _uiState.update {
            it.copy(screenState = StateType.LOADING)
        }

        viewModelScope.launch {
            val apiResult = transactionRepository.getAllExpenseType()
            when (apiResult) {
                is ApiResult.Success -> {
                    val expenseTypeResponse = apiResult.data
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS,
                            allExpenseTypeResponse = expenseTypeResponse
                        )
                    }
                }

                is ApiResult.Error -> {
                    error(apiResult.message)
                }
            }
        }
    }


    fun onDoneWalletNumber(
        onError: (String) -> Unit
    ) {
        if(uiState.value.toWalletNumber.isEmpty()){
            _uiState.update {
                it.copy(
                    toMerchantName = "",
                )
            }
            onError("Số ví phải gồm 10 chữ số")
            return
        }
        _uiState.update {
            it.copy(screenState = StateType.LOADING)
        }
        viewModelScope.launch {
            val apiResult = walletRepository.getInfoByWalletNumber(
                walletNumber = uiState.value.toWalletNumber
            )
            when (apiResult) {
                is ApiResult.Success -> {
                    val walletResponse = apiResult.data
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS,
                            toMerchantName = walletResponse.merchantName,
                            isVerified = walletResponse.verified
                        )
                    }
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            toMerchantName = "",
                        )
                    }
                    error(apiResult.message)
                }
            }
        }
    }


    fun onExpenseTypeChange(expenseType: String) {
        _uiState.update {
            it.copy(expenseType = expenseType)
        }
    }

    fun onToWalletNumberChange(toWalletNumber: String) {
        _uiState.value = _uiState.value.copy(toWalletNumber = toWalletNumber)
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


    fun onContentChange(content: String) {
        _uiState.value = _uiState.value.copy(description = content)
    }

    fun onSelectSavedReceiver( savedReceiver: SavedReceiver) {
        _uiState.update {
            it.copy(
                toWalletNumber = savedReceiver.toWalletNumber,
            )
        }
        viewModelScope.launch {
            onDoneWalletNumber {  }
        }
    }

    fun onChangeSaveReceiver() {
        _uiState.update {
            it.copy(isSaveReceiver = !uiState.value.isSaveReceiver)
        }
    }
}
