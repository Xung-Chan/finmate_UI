package com.example.ibanking_kltn.ui.screens.deposit

import android.content.Context
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class DepositEvent {
    data class ContinuePayment(val context : Context) : DepositEvent()
    data class AmountChange(val amount: String) : DepositEvent()
}

sealed class DepositEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : DepositEffect()
}