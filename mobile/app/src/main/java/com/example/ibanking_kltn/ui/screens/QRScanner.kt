package com.example.ibanking_kltn.ui.screens

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.ibanking_kltn.ui.uistates.QRScannerUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.QRCodeAnalyzer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScannerScreen(
    uiState: QRScannerUiState,
//    onBillDetecting: (BillPayload) -> Unit,
//    onTransferDetecting: (TransferPayload) -> Unit,
    detecting: (String) -> Unit,
//    onError: (String) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(factory = { ctx ->
        val previewView = PreviewView(ctx)
        val cameraProviderFuture = ProcessCameraProvider.Companion.getInstance(ctx)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().apply {
                surfaceProvider = previewView.surfaceProvider
            }
            val analyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build().also {
                    it.setAnalyzer(
                        ContextCompat.getMainExecutor(ctx),
                        QRCodeAnalyzer { qrCode ->
                            if (uiState.state != StateType.LOADING && !uiState.isDetectSuccess)
                                detecting(qrCode)

                        }
                    )
                }
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                analyzer
            )
        }, ContextCompat.getMainExecutor(ctx))
        previewView
    }, modifier = Modifier.Companion.fillMaxSize())
}