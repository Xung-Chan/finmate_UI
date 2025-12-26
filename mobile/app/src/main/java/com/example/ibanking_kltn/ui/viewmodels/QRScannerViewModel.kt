package com.example.ibanking_kltn.ui.viewmodels


import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.BillPayload
import com.example.ibanking_kltn.data.dtos.QRPayload
import com.example.ibanking_kltn.data.dtos.QRType
import com.example.ibanking_kltn.data.dtos.TransferPayload
import com.example.ibanking_kltn.ui.uistates.QRScannerUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.jsonInstance
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltViewModel
class QRScannerViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(QRScannerUiState())
    val uiState: StateFlow<QRScannerUiState> = _uiState.asStateFlow()

    fun clearState() {
        _uiState.update {
            it.copy(
                state = StateType.NONE,
                isDetectSuccess = false
            )
        }
    }

    fun onDetecting(
        qrCode: String,
        onBillDetecting: (BillPayload) -> Unit,
        onTransferDetecting: (TransferPayload) -> Unit,
        onError: (String) -> Unit
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
                    _uiState.update {
                        it.copy(
                            state = StateType.SUCCESS,
                            isDetectSuccess = true
                        )
                    }
                    onBillDetecting(payload as BillPayload)
                    _uiState.update {
                        it.copy(
                            state = StateType.NONE,
                            isDetectSuccess = false
                        )
                    }
                }

                QRType.TRANSFER.name -> {
                    _uiState.update {
                        it.copy(
                            state = StateType.SUCCESS,
                            isDetectSuccess = true
                        )
                    }
                    onTransferDetecting(payload as TransferPayload)
                    _uiState.update {
                        it.copy(
                            state = StateType.NONE,
                            isDetectSuccess = false
                        )
                    }

                }

                else -> {
                    val message = "Mã QR không hợp lệ"
                    onError(message)
                    _uiState.update {
                        it.copy(
                            state = StateType.FAILED(message),
                            isDetectSuccess = false
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val message = "Mã QR không hợp lệ"
            onError(message)
            _uiState.update {
                it.copy(
                    state = StateType.FAILED(message),
                    isDetectSuccess = false
                )
            }
        }

    }

    fun onAnalyzeImage(
        context: Context,
        uri: Uri,
        onBillDetecting: (BillPayload) -> Unit,
        onTransferDetecting: (TransferPayload) -> Unit,
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                state = StateType.LOADING
            )
        }
        viewModelScope.launch {
            val image = InputImage.fromFilePath(context, uri)

            // Tạo scanner với options
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()

            val scanner = BarcodeScanning.getClient(options)

            // Scan và await kết quả (Kotlin Coroutines)
            val barcodes = scanner.process(image).await()

            // Lấy QR code đầu tiên
            val qrCode = barcodes.firstOrNull {
                it.format == Barcode.FORMAT_QR_CODE
            }?.rawValue

            // Close scanner
            scanner.close()
            if (qrCode != null) {
                onDetecting(
                    qrCode = qrCode,
                    onBillDetecting = onBillDetecting,
                    onTransferDetecting = onTransferDetecting,
                    onError = onError
                )
            } else {
                val message = "Mã QR không hợp lệ"
                onError(message)
                _uiState.update {
                    it.copy(
                        state = StateType.FAILED(message),
                        isDetectSuccess = false
                    )
                }
            }
        }

    }


}