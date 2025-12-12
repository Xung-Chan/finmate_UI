package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.responses.AllExpenseTypeResponse

data class CreateBillUiState(
    val screenState: StateType = StateType.NONE,

    val amount: Long = 0L,
    val description: String = "",
    val allExpenseTypeResponse: AllExpenseTypeResponse = AllExpenseTypeResponse(),
    val expenseType: String = "",
    val expiryDate: String = ""
)
