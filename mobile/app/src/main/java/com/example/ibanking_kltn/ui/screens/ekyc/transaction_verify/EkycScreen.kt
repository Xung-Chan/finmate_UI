//package com.example.ibanking_kltn.ui.screens.ekyc.transaction_verify
//
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.RequiresApi
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.ibanking_kltn.BuildConfig
//import com.example.ibanking_kltn.ui.screens.ekyc.register.EkycEvent
//import com.example.ibanking_kltn.ui.screens.ekyc.register.EkycStep
//import com.example.ibanking_kltn.ui.screens.ekyc.register.FullEkycUiState
//import com.vnptit.idg.sdk.activity.VnptIdentityActivity
//import com.vnptit.idg.sdk.activity.VnptPortraitActivity
//import com.vnptit.idg.sdk.utils.KeyIntentConstants
//import com.vnptit.idg.sdk.utils.SDKEnum
//
///**
// * Main eKYC Screen - Sử dụng kiến trúc MVI/MVVM
// */
//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//@Composable
//fun EkycScreen(
//    uiState: FullEkycUiState,
//    onEvent: (EkycEvent) -> Unit,
//) {
//    val context = LocalContext.current
//
//    // Activity Result Launcher for Full eKYC
//    val fullEkycLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        viewModel.handleEkycResult(result.resultCode, result.data)
//    }
//
//    // Activity Result Launcher for Face Capture only
//    val faceLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        viewModel.handleEkycResult(result.resultCode, result.data)
//    }
//
//    LaunchedEffect(uiState.ekycStep) {
//        when (uiState.ekycStep) {
//            EkycStep.Initial -> {
//                if (checkCameraPermission(context)) {
//                    val intent = registerFullEkycIntent(context)
//                    fullEkycLauncher.launch(intent)
//                } else {
//                    requestCameraPermission(permissionLauncher)
//                }
//            }
//            EkycStep.FaceCapture -> {
//                if (checkCameraPermission(context)) {
//                    val intent = authorizationFaceIntent(context)
//                    faceLauncher.launch(intent)
//                } else {
//                    requestCameraPermission(permissionLauncher)
//                }
//            }
//            else -> { /* No action needed */ }
//        }
//    }
//
//}
//
//
//
//private fun registerFullEkycIntent(context: Context): Intent {
//    return Intent(context, VnptIdentityActivity::class.java).apply {
//        // Cấu hình token và key - BẮT BUỘC
//        putExtra(KeyIntentConstants.ACCESS_TOKEN, BuildConfig.VNPT_ACCESS_TOKEN)
//        putExtra(KeyIntentConstants.TOKEN_ID, BuildConfig.VNPT_TOKEN_ID)
//        putExtra(KeyIntentConstants.TOKEN_KEY, BuildConfig.VNPT_TOKEN_KEY)
//
//        // Cấu hình loại tài liệu
//        putExtra(KeyIntentConstants.DOCUMENT_TYPE, SDKEnum.DocumentTypeEnum.IDENTITY_CARD.value)
//
//        // Cấu hình phiên bản SDK
//        putExtra(KeyIntentConstants.VERSION_SDK, SDKEnum.VersionSDKEnum.ADVANCED.value)
//
//        // Cấu hình hiển thị
//        putExtra(KeyIntentConstants.IS_SHOW_TUTORIAL, true)
//        putExtra(KeyIntentConstants.IS_ENABLE_GOT_IT, true)
//
//        // Cấu hình so sánh khuôn mặt - BẮT BUỘC để có COMPARE_FACE_RESULT
//        putExtra(KeyIntentConstants.IS_ENABLE_COMPARE, true)
//
//        // Cấu hình kiểm tra liveness
//        putExtra(KeyIntentConstants.CHECK_LIVENESS_FACE, SDKEnum.ModeCheckLiveNessFace.iBETA.value)
//        putExtra(KeyIntentConstants.IS_CHECK_MASKED_FACE, true)
//        putExtra(KeyIntentConstants.IS_CHECK_LIVENESS_CARD, true)
//
//        // Cấu hình validation
//        putExtra(KeyIntentConstants.IS_VALIDATE_POSTCODE, true)
//        putExtra(KeyIntentConstants.VALIDATE_DOCUMENT_TYPE, SDKEnum.ValidateDocumentType.Basic.value)
//
//        // Cấu hình ngôn ngữ
//        putExtra(KeyIntentConstants.LANGUAGE_SDK, SDKEnum.LanguageEnum.VIETNAMESE.value)
//
//        // Cấu hình scan QR code
//        putExtra(KeyIntentConstants.IS_ENABLE_SCAN_QRCODE, true)
//    }
//}
//
//private fun authorizationFaceIntent(context: Context): Intent {
//    return Intent(context, VnptPortraitActivity::class.java).apply {
//        // Cấu hình token và key - BẮT BUỘC
//        putExtra(KeyIntentConstants.ACCESS_TOKEN, BuildConfig.VNPT_ACCESS_TOKEN)
//        putExtra(KeyIntentConstants.TOKEN_ID, BuildConfig.VNPT_TOKEN_ID)
//        putExtra(KeyIntentConstants.TOKEN_KEY, BuildConfig.VNPT_TOKEN_KEY)
//
//        // Cấu hình hiển thị
//        putExtra(KeyIntentConstants.IS_SHOW_TUTORIAL, true)
//        putExtra(KeyIntentConstants.IS_CHECK_MASKED_FACE, true)
//        putExtra(KeyIntentConstants.IS_ENABLE_GOT_IT, true)
//
//        // Cấu hình so sánh khuôn mặt
//        putExtra(KeyIntentConstants.IS_ENABLE_COMPARE, true)
//
//        // Cấu hình kiểm tra liveness
//        putExtra(KeyIntentConstants.CHECK_LIVENESS_FACE, SDKEnum.ModeCheckLiveNessFace.iBETA.value)
//
//        // Cấu hình phiên bản SDK
//        putExtra(KeyIntentConstants.VERSION_SDK, SDKEnum.VersionSDKEnum.ADVANCED.value)
//
//        // Cấu hình ngôn ngữ
//        putExtra(KeyIntentConstants.LANGUAGE_SDK, SDKEnum.LanguageEnum.VIETNAMESE.value)
//    }
//}
//
