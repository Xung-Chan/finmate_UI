package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.dtos.definitions.TransactionStatus

data class TransactionResultUiState(
    val screenState: StateType = StateType.NONE,
    val service: String = "",
    val amount: Long = 0L,
    val status: TransactionStatus = TransactionStatus.COMPLETED
)

