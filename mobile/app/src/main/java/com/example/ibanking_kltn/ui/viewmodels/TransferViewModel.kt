package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.di.SavedReceiverManager
import com.example.ibanking_kltn.dtos.definitions.SavedReceiverInfo
import com.example.ibanking_kltn.dtos.definitions.ServiceType
import com.example.ibanking_kltn.dtos.responses.ExpenseType
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.data.repositories.WalletRepository
import com.example.ibanking_kltn.data.session.UserSession
import com.example.ibanking_kltn.ui.event.TransferEffect
import com.example.ibanking_kltn.ui.event.TransferEvent
import com.example.ibanking_kltn.ui.uistates.ConfirmContent
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.ui.uistates.TransferUiState
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_kltn.utils.removeVietnameseAccents
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
class TransferViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val walletRepository: WalletRepository,
    private val savedReceiverManager: SavedReceiverManager,
    private val userSession: UserSession,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<TransferEffect>()
    val uiEffect = _uiEffect.asSharedFlow()


    init {
        val walletNumber = savedStateHandle.get<String>("toWalletNumber")
        if (!walletNumber.isNullOrEmpty()) {
            _uiState.update {
                it.copy(toWalletNumber = walletNumber)
            }
            viewModelScope.launch {
                onDoneWalletNumber()
            }
        }
        val amount = savedStateHandle.get<Long>("amount")
        if (amount != null && amount > 0L) {
            _uiState.update {
                it.copy(amount = amount)
            }
        }
        loadExpenseType()
        loadSavedReceivers()
    }


    fun onEvent(event: TransferEvent) {
        when (event) {
            TransferEvent.DoneWalletNumber -> onDoneWalletNumber()
            is TransferEvent.AmountChange -> onAmountChange(event.amount)
            TransferEvent.ChangeSaveReceiver -> onChangeSaveReceiver()
            is TransferEvent.ContentChange -> onContentChange(event.content)
            is TransferEvent.ExpenseTypeChange -> onExpenseTypeChange(event.expenseType)
            is TransferEvent.SaveReceiver -> onSaveReceiver(event.savedReceiver)
            is TransferEvent.SelectSavedReceiver -> onSelectSavedReceiver(event.savedReceiver)
            is TransferEvent.ToWalletNumberChange -> onToWalletNumberChange(event.walletNumber)
            TransferEvent.ConfirmTransfer -> onConfirmClick()
        }
    }

    private fun loadExpenseType(
    ) {
        var message = ""
        viewModelScope.launch {
            repeat(3) {
                _uiState.update {
                    it.copy(
                        screenState = StateType.LOADING
                    )
                }
                val apiResult = transactionRepository.getAllExpenseType()
                when (apiResult) {
                    is ApiResult.Success -> {
                        val expenseTypeResponse = apiResult.data
                        _uiState.update {
                            it.copy(
                                screenState = StateType.SUCCESS,
                                allExpenseTypeResponse = expenseTypeResponse,
                            )
                        }
                        return@launch


                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                screenState = StateType.FAILED(apiResult.message),
                            )
                        }
                        message = apiResult.message
                    }
                }
            }
            _uiState.update {
                it.copy(
                    screenState = StateType.FAILED(message),
                )
            }
            _uiEffect.emit(
                TransferEffect.ShowSnackBar(
                    SnackBarUiState(
                        message = message,
                        type = SnackBarType.ERROR
                    )
                )
            )
        }
    }

    private fun loadSavedReceivers() {
        val savedReceivers = savedReceiverManager.getAll()
        _uiState.update {
            it.copy(
                savedReceivers = savedReceivers
            )
        }
    }

    private fun onConfirmClick() {
        val confirmContent = ConfirmContent.TRANSFER(
            amount = uiState.value.amount,
            service = ServiceType.TRANSFER,
            isVerified = uiState.value.isVerified,
            toWalletNumber = uiState.value.toWalletNumber,
            toMerchantName = uiState.value.toMerchantName,
            description = removeVietnameseAccents(uiState.value.description.ifEmpty { "Chuyen tien den ${uiState.value.toMerchantName}" }),
            expenseType = uiState.value.expenseType?.name,
            expenseTypeId = uiState.value.expenseType?.id,
        )
        viewModelScope.launch {
            _uiEffect.emit(
                TransferEffect.NavigateToConfirmScreen(
                    confirmContent = confirmContent
                )
            )
        }

    }

    private fun onDoneWalletNumber(
    ) {
        if (uiState.value.toWalletNumber.isEmpty()) {
            _uiState.update {
                it.copy(
                    toMerchantName = "",
                )
            }
            viewModelScope.launch {

                _uiEffect.emit(
                    TransferEffect.ShowSnackBar(
                        SnackBarUiState(
                            message = "Vui lòng nhập số ví",
                            type = SnackBarType.WARNING
                        )
                    )
                )
            }
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
                    if(walletResponse.walletNumber == userSession.user.value?.wallet?.walletNumber){
                        val message = "Không thể chuyển tiền cho chính mình"
                        _uiState.update {
                            it.copy(
                                screenState = StateType.FAILED(message),
                                toMerchantName = "",
                            )
                        }
                        _uiEffect.emit(
                            TransferEffect.ShowSnackBar(
                                SnackBarUiState(
                                    message = message,
                                    type = SnackBarType.ERROR
                                )
                            )
                        )
                        return@launch
                    }

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
                            screenState = StateType.FAILED(apiResult.message),
                            toMerchantName = "",
                        )
                    }
                    _uiEffect.emit(
                        TransferEffect.ShowSnackBar(
                            SnackBarUiState(
                                message = apiResult.message,
                                type = SnackBarType.ERROR
                            )
                        )
                    )
                }
            }
        }
    }


    private fun onExpenseTypeChange(expenseType: ExpenseType) {
        _uiState.update {
            it.copy(
                expenseType = expenseType,

            )
        }
    }

    private fun onToWalletNumberChange(toWalletNumber: String) {
        _uiState.value = _uiState.value.copy(toWalletNumber = toWalletNumber)
    }

    private fun onAmountChange(amount: String) {
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


    private fun onContentChange(content: String) {
        _uiState.value = _uiState.value.copy(description = content)
    }

    private fun onSelectSavedReceiver(savedReceiver: SavedReceiverInfo) {
        _uiState.update {
            it.copy(
                toWalletNumber = savedReceiver.toWalletNumber,
            )
        }
        viewModelScope.launch {
            onDoneWalletNumber()
        }
    }

    private fun onChangeSaveReceiver() {
        _uiState.update {
            it.copy(isSaveReceiver = !uiState.value.isSaveReceiver)
        }
    }

    private fun onSaveReceiver(
        receiver: SavedReceiverInfo
    ) {
        savedReceiverManager.add(receiver)
    }
}
