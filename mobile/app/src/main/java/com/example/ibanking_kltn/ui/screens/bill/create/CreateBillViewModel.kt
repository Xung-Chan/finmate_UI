package com.example.ibanking_kltn.ui.screens.bill.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.repositories.BillRepository
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.dtos.requests.CreateBillRequest
import com.example.ibanking_kltn.dtos.responses.ExpenseType
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.uistates.StateType
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@HiltViewModel
class CreateBillViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val billRepository: BillRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateBillUiState())
    val uiState: StateFlow<CreateBillUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<CreateBillEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        loadExpenseType()
    }


    fun onEvent(event: CreateBillEvent) {
        when (event) {
            is CreateBillEvent.AmountChange -> onAmountChange(event.amount)
            is CreateBillEvent.DescriptionChange -> onDescriptionChange(event.description)
            is CreateBillEvent.ExpenseTypeChange -> onExpenseTypeChange(event.expenseType)
            is CreateBillEvent.ExpiryDateChange -> onExpiryDateChange(event.date)
            CreateBillEvent.ContinueClick -> onContinueClick()
            CreateBillEvent.BackClick -> onBackClick()
        }
    }

    private fun loadExpenseType() {
        _uiState.update { it.copy(screenState = StateType.LOADING) }
        viewModelScope.launch {
            val apiResult = transactionRepository.getAllExpenseType()
            when (apiResult) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS,
                            allExpenseTypeResponse = apiResult.data
                        )
                    }
                }
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(screenState = StateType.FAILED(apiResult.message))
                    }
                }
            }
        }
    }

    fun isEnableCreateBill(): Boolean {
        return uiState.value.selectedExpenseType != null
                && uiState.value.amount > 0L
                && uiState.value.expiryDate >= LocalDate.now()
                && uiState.value.description.isNotEmpty()
    }

    private fun onContinueClick() {
        _uiState.update { it.copy(screenState = StateType.LOADING) }
        viewModelScope.launch {
            val request = CreateBillRequest(
                amount = uiState.value.amount,
                description = removeVietnameseAccents(uiState.value.description),
                dueDate = LocalDateTime.of(uiState.value.expiryDate, LocalTime.now()).toString(),
                expenseTypeId = uiState.value.selectedExpenseType!!.id,
            )
            val apiResult = billRepository.createBill(request = request)
            when (apiResult) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(screenState = StateType.SUCCESS) }
                    _uiEffect.emit(CreateBillEffect.NavigateToBillDetail(apiResult.data))
                }
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(screenState = StateType.FAILED(apiResult.message))
                    }
                    _uiEffect.emit(
                        CreateBillEffect.ShowSnackBar(
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

    private fun onBackClick() {
        viewModelScope.launch {
            _uiEffect.emit(CreateBillEffect.NavigateBack)
        }
    }

    private fun onAmountChange(amount: String) {
        val formatAmount = amount.replace(".", "").replace(",", "")
        if (formatAmount.isEmpty()) {
            _uiState.update { it.copy(amount = 0L) }
            return
        }
        _uiState.update { it.copy(amount = formatAmount.toLong()) }
    }

    private fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    private fun onExpenseTypeChange(expenseType: ExpenseType) {
        _uiState.update { it.copy(selectedExpenseType = expenseType) }
    }

    private fun onExpiryDateChange(date: LocalDate) {
        _uiState.update { it.copy(expiryDate = date) }
    }

}