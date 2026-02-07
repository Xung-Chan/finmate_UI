package com.example.ibanking_kltn.ui.screens.transaction_result

import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class TransactionResultEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : TransactionResultEffect()
    object InitFailed : TransactionResultEffect()
}