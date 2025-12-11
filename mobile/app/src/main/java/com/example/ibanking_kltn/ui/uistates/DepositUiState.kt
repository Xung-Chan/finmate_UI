package com.example.ibanking_kltn.ui.uistates

data class DepositUiState(
    val screenState: StateType = StateType.NONE,
    val balance : Long = 0L,
    val availableAccount: List<String> = listOf(),
    val amount: Long = 0L,
    val accountType: String = "",
)

