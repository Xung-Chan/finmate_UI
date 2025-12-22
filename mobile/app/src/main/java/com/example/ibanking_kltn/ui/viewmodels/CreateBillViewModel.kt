package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.PaymentAccount
import com.example.ibanking_kltn.data.dtos.requests.CreateBillRequest
import com.example.ibanking_kltn.data.dtos.responses.BillResponse
import com.example.ibanking_kltn.data.dtos.responses.ExpenseType
import com.example.ibanking_kltn.data.repositories.BillRepository
import com.example.ibanking_kltn.ui.uistates.CreateBillUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class CreateBillViewModel @Inject constructor(

    private val billRepository: BillRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateBillUiState())
    val uiState: StateFlow<CreateBillUiState> = _uiState.asStateFlow()


    fun init(
    ) {
    }

    fun clearState() {
        _uiState.value = CreateBillUiState()
    }
    fun loadWallet(){

    }

    fun isEnableCreateBill(): Boolean {
        return uiState.value.selectedAccountWallet != null
                && uiState.value.selectedExpenseType != null
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
                description = uiState.value.description,
                dueDate = uiState.value.expiryDate.toString(),
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

    fun onAccountTypeChange(accountType: PaymentAccount) {
        _uiState.update { it.copy(selectedAccountWallet = accountType) }
    }

    fun onExpiryDateChange(date: LocalDate) {
        _uiState.update { it.copy(expiryDate = date) }
    }


}