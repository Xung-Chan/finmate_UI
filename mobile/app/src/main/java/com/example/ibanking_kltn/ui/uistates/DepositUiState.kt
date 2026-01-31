package com.example.ibanking_kltn.ui.uistates

data class DepositUiState(
    val screenState: StateType = StateType.NONE,
    val amount: Long = 0L,
) {
    val isEnableContinue: Boolean
        get() = amount >= 10000L && screenState != StateType.LOADING
}

