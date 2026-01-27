package com.example.ibanking_kltn.ui.event

import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class QrScannerEvent {

}

sealed class QrScannerEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : QrScannerEffect()
}