package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.responses.AllExpenseTypeResponse
import com.example.ibanking_kltn.data.dtos.responses.ExpenseType
import java.time.LocalDate

data class CreateBillUiState(
    val screenState: StateType = StateType.NONE,

    val allExpenseTypeResponse: AllExpenseTypeResponse = AllExpenseTypeResponse(),

    val selectedExpenseType: ExpenseType?=null,

    val amount: Long = 0L,
    val description: String = "",
    val expiryDate: LocalDate = LocalDate.now()
)
