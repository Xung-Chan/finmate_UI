package com.example.ibanking_kltn.ui.event

import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class HandleDepositEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : HandleDepositEffect()
    data class NavigateToTransactionResult(val transactionId: String) : HandleDepositEffect()
}