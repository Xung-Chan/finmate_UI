package com.example.ibanking_kltn.ui.viewmodels


import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.dtos.definitions.BillPayload
import com.example.ibanking_kltn.dtos.definitions.QRPayload
import com.example.ibanking_kltn.dtos.definitions.QRType
import com.example.ibanking_kltn.dtos.definitions.TransferPayload
import com.example.ibanking_kltn.dtos.definitions.Screens
import com.example.ibanking_kltn.ui.event.QrScannerEffect
import com.example.ibanking_kltn.ui.event.QrScannerEvent
import com.example.ibanking_kltn.ui.uistates.QRScannerUiState
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_kltn.utils.jsonInstance
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltViewModel
class QRScannerViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(QRScannerUiState())
    val uiState: StateFlow<QRScannerUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<QrScannerEffect>()
    val uiEffect = _uiEffect.asSharedFlow()


    fun onEvent(event: QrScannerEvent) {
        when (event) {
            is QrScannerEvent.AnalyzeImage -> onAnalyzeImage(
                context = event.context,
                uri = event.uri,
            )

            is QrScannerEvent.Detecting -> onDetecting(
                qrCode = event.qrCode,
            )
        }
    }


    private fun onDetecting(
        qrCode: String,
    ) {
        _uiState.update {
            it.copy(
                state = StateType.LOADING
            )
        }
        try {
            val payload = jsonInstance().decodeFromString<QRPayload>(qrCode)
            when (payload.qrType) {
                QRType.BILL.name -> {
                    val billCode = (payload as BillPayload).billCode
                    val route = "${Screens.PayBill.name}?billCode=${billCode}"

                    _uiState.update {
                        it.copy(
                            state = StateType.SUCCESS,
                        )
                    }
                    viewModelScope.launch {
                        _uiEffect.emit(
                            QrScannerEffect.Navigate(route = route)
                        )
                    }
                }

                QRType.TRANSFER.name -> {
                    val transferInfo = (payload as TransferPayload)
                    val route =
                        "${Screens.Transfer.name}?toWalletNumber=${transferInfo.toWalletNumber}&amount=${transferInfo.amount}"
                    _uiState.update {
                        it.copy(
                            state = StateType.SUCCESS,
                        )
                    }
                    viewModelScope.launch {
                        _uiEffect.emit(
                            QrScannerEffect.Navigate(route = route)
                        )
                    }
                }

                else -> {
                    val message = "Mã QR không hợp lệ"
                    _uiState.update {
                        it.copy(
                            state = StateType.FAILED(message),
                        )
                    }
                    viewModelScope.launch {
                        _uiEffect.emit(
                            QrScannerEffect.ShowSnackBar(
                                snackBar = SnackBarUiState(
                                    message = message,
                                    type = SnackBarType.ERROR
                                )
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val message = "Mã QR không hợp lệ"
            _uiState.update {
                it.copy(
                    state = StateType.FAILED(message),
                )
            }
            viewModelScope.launch {
                _uiEffect.emit(
                    QrScannerEffect.ShowSnackBar(
                        snackBar = SnackBarUiState(
                            message = message,
                            type = SnackBarType.ERROR
                        )
                    )
                )
            }

        }

    }

    private fun onAnalyzeImage(
        context: Context,
        uri: Uri,
    ) {
        _uiState.update {
            it.copy(
                state = StateType.LOADING
            )
        }
        viewModelScope.launch {
            val image = InputImage.fromFilePath(context, uri)
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()

            val scanner = BarcodeScanning.getClient(options)

            val barcodes = scanner.process(image).await()

            val qrCode = barcodes.firstOrNull {
                it.format == Barcode.FORMAT_QR_CODE
            }?.rawValue

            scanner.close()

            if (qrCode == null) {

                val message = "Mã QR không hợp lệ"
                _uiState.update {
                    it.copy(
                        state = StateType.FAILED(message),
                    )
                }
                _uiEffect.emit(
                    QrScannerEffect.ShowSnackBar(
                        snackBar = SnackBarUiState(
                            message = message,
                            type = SnackBarType.ERROR
                        )
                    )
                )
                return@launch
            }
            onDetecting(
                qrCode = qrCode
            )
        }

    }


}