package com.example.ibanking_kltn.ui.event

import android.content.Context
import android.net.Uri
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class QrScannerEvent {
    data class Detecting(val qrCode: String) : QrScannerEvent()
    data class AnalyzeImage(val context: Context, val uri : Uri): QrScannerEvent()
}

sealed class QrScannerEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : QrScannerEffect()
    data class Navigate(val route: String) : QrScannerEffect()

}