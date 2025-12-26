package com.example.ibanking_kltn.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.uistates.QRScannerUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.QRCodeAnalyzer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScannerScreen(
    uiState: QRScannerUiState,
    detecting: (String) -> Unit,
    onBackClick: () -> Unit,
    onAnalyzeImage: (Uri) -> Unit,
//    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    LaunchedEffect(
        selectedImageUri
    ) {
        selectedImageUri?.let { uri ->
            onAnalyzeImage(uri)
        }

    }
    // Single photo picker
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        selectedImageUri = uri
    }
    Box {
        Scaffold(
            topBar = {

                TopAppBar(
                    title = {
                        Text(text = "Quét mã QR")
                    },
                    navigationIcon = {
                        IconButton(onClick = {

                            onBackClick()
                        }) {
                            Icon(
                                Icons.Default.ArrowBackIosNew,
                                contentDescription = null,
                                modifier = Modifier.size(25.dp),
                                tint = White1
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        titleContentColor = White1,
                        containerColor = Black1
                    ),
                )
            },
            containerColor = Color.Transparent,
            modifier = Modifier.systemBarsPadding()
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                AndroidView(
                    factory = { ctx ->
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
                    },
                    modifier = Modifier.Companion.fillMaxSize()
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.scan),
                contentDescription = "QR Scanner Frame",
                modifier = Modifier.size(350.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .background(
                            color = White1,
                            shape = CircleShape
                        )
                        .shadow(
                            elevation = 10.dp,
                            shape = CircleShape,
                            ambientColor = Color.Transparent,
                            spotColor = Color.Transparent
                        )
                        .clickable {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                            )

                        }
                        .padding(10.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.image),
                        contentDescription = "Gallery Icon",
                        modifier = Modifier.size(25.dp)
                    )
                }
                Text(
                    text = "Chọn ảnh QR",
                    style = CustomTypography.bodyMedium,
                    color = White1
                )
            }
        }
    }

}

@androidx.compose.ui.tooling.preview.Preview(showSystemUi = true, showBackground = true)
@Composable
fun QRScannerPreview() {
    QRScannerScreen(
        QRScannerUiState(), detecting = {}, onBackClick = {},
        onAnalyzeImage = {}

    )
}