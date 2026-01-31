package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.dtos.requests.CreateBillRequest
import com.example.ibanking_kltn.dtos.responses.BillResponse
import com.example.ibanking_kltn.dtos.responses.ExpenseType
import com.example.ibanking_kltn.data.repositories.BillRepository
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.ui.uistates.CreateBillUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.removeVietnameseAccents
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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


    fun init(
    ) {
        clearState()
        loadExpenseType()
    }

    fun clearState() {
        _uiState.value = CreateBillUiState()
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
                    _uiState.update {
                        it.copy(
                            screenState = StateType.FAILED(apiResult.message),
                        )
                    }
//                    error(apiResult.message)
                }
            }
        }
    }



    fun isEnableCreateBill(): Boolean {
        return  uiState.value.selectedExpenseType != null
                && uiState.value.amount > 0L
                && uiState.value.expiryDate >= LocalDate.now()
                && uiState.value.description.isNotEmpty()
    }

    fun onContinueClick(
        onSucess: (BillResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING,
            )
        }
        viewModelScope.launch {
            val request = CreateBillRequest(
                amount = uiState.value.amount,
                description = removeVietnameseAccents(uiState.value.description),
                dueDate = LocalDateTime.of(uiState.value.expiryDate, LocalTime.now()).toString(),

                expenseTypeId = uiState.value.selectedExpenseType!!.id,
            )
            val apiResult = billRepository.createBill(
                request = request
            )

            when (apiResult) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS,
                        )
                    }
                    onSucess(apiResult.data)
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.FAILED(apiResult.message)
                        )
                    }
                    onError(apiResult.message)
                }

            }
        }
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

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onExpenseTypeChange(expenseType: ExpenseType) {
        _uiState.update { it.copy(selectedExpenseType = expenseType) }
    }


    fun onExpiryDateChange(date: LocalDate) {
        _uiState.update { it.copy(expiryDate = date) }
    }


}