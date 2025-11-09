package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.AccountType
import com.example.ibanking_kltn.data.repositories.PayLaterRepository
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.data.repositories.WalletRepository
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.ui.uistates.TransferUiState
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
class TransferViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val walletRepository: WalletRepository,
    private val payLaterRepository: PayLaterRepository,
    @ApplicationContext private val context: Context,

    ) : ViewModel(), IViewModel {
    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState.asStateFlow()

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

    fun init() {
        loadBalance()
        loadExpenseType()
        loadAvailableAccount()
    }


    override fun clearState() {
        _uiState.value = TransferUiState()
    }


    private fun loadAvailableAccount() {
        _uiState.update {
            it.copy(screenState = StateType.LOADING)
        }
        viewModelScope.launch {
            val availableAccount = mutableListOf<String>(AccountType.WALLET.toString())
            val payLaterResponse = payLaterRepository.getMyPayLater()
            when (payLaterResponse) {
                is ApiResult.Success -> {
                    if (payLaterResponse.data.availableCredit != null) {
                        availableAccount.add(AccountType.PAY_LATER.toString())
                    }
                }

                is ApiResult.Error -> {
                    Toast.makeText(
                        context,
                        "Không thể tải thông tin PayLater: ${payLaterResponse.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            _uiState.update {
                it.copy(
                    screenState = StateType.SUCCESS,
                    availableAccount = availableAccount.toList()
                )
            }
        }
    }

    fun isEnableContinue(): Boolean {
        val data = uiState.value
        val result = data.toMerchantName.isNotEmpty() &&
                data.amount > 0L &&
                data.accountType.isNotEmpty() &&
                data.expenseType.isNotEmpty()

        return result
    }

    fun loadBalance(
    ) {
        _uiState.update {
            it.copy(screenState = StateType.LOADING)
        }
        viewModelScope.launch {
            val apiResult = walletRepository.getMyWalletInfor()
            when (apiResult) {
                is ApiResult.Success -> {
                    val walletResponse = apiResult.data
                    val balance = walletResponse.balance?.toLong()
                    if (balance == null) {
                        error(message = "Lỗi không thể tải số dư ví")
                        return@launch
                    }
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS,
                            balance = apiResult.data.balance.toLong()
                        )
                    }
                }

                is ApiResult.Error -> {
                    error(apiResult.message)
                }
            }
        }


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


    fun onDoneWalletNumber() {
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
                            toMerchantName = walletResponse.merchantName
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

    fun onAccountTypeChange(accountType: String) {
        _uiState.update {
            it.copy(accountType = accountType)
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
        val formatAmount = amount.replace(".", "")
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


}
