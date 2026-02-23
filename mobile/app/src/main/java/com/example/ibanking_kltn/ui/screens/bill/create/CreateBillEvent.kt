package com.example.ibanking_kltn.ui.screens.bill.create

import com.example.ibanking_kltn.dtos.responses.BillResponse
import com.example.ibanking_kltn.dtos.responses.ExpenseType
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import java.time.LocalDate

sealed class CreateBillEvent {
    object BackClick : CreateBillEvent()
    object ContinueClick : CreateBillEvent()
    data class AmountChange(val amount: String) : CreateBillEvent()
    data class DescriptionChange(val description: String) : CreateBillEvent()
    data class ExpenseTypeChange(val expenseType: ExpenseType) : CreateBillEvent()
    data class ExpiryDateChange(val date: LocalDate) : CreateBillEvent()
}

sealed class CreateBillEffect {
    object NavigateBack : CreateBillEffect()
    data class NavigateToBillDetail(val bill: BillResponse) : CreateBillEffect()
    data class ShowSnackBar(val snackBar: SnackBarUiState) : CreateBillEffect()
}

