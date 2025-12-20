package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.di.ServiceManager
import com.example.ibanking_kltn.data.dtos.AccountType
import com.example.ibanking_kltn.data.repositories.BillRepository
import com.example.ibanking_kltn.data.repositories.PayLaterRepository
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.data.repositories.WalletRepository
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

    private val transactionRepository: TransactionRepository,
    private val billRepository: BillRepository,
    private val walletRepository: WalletRepository,
    private val payLaterRepository: PayLaterRepository,
    private val sharedPreferences: SharedPreferences,
    @ApplicationContext private val context: Context
) : ViewModel(), IViewModel {
    private val _uiState = MutableStateFlow(BillUiState())
    val uiState: StateFlow<BillUiState> = _uiState.asStateFlow()

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


    override fun clearState() {
        _uiState.value = BillUiState()
    }

    fun init() {
        clearState()
        loadAvailableAccount()
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

    fun onChangeAccountType(accountType: String) {
        _uiState.update {
            it.copy(accountType = accountType)
        }
    }
    fun onCheckingBill() {
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
                    error(billResult.message)
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