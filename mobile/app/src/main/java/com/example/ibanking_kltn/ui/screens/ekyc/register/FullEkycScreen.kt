package com.example.ibanking_kltn.ui.screens.ekyc.register

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.ibanking_kltn.BuildConfig
import com.vnptit.idg.sdk.activity.VnptIdentityActivity
import com.vnptit.idg.sdk.utils.KeyIntentConstants
import com.vnptit.idg.sdk.utils.SDKEnum

/**
 * Main eKYC Screen - Sử dụng kiến trúc MVI/MVVM
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun FullEkycScreen(
    onEvent: (FullEkycEvent) -> Unit,
) {
    val context = LocalContext.current

    val fullEkycLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        onEvent(
            FullEkycEvent.HandleEkycResult(activityResult = result)
        )
    }

    // Use LaunchedEffect to launch the eKYC flow after composition completes
    LaunchedEffect(Unit) {
        val intent = createFullEkycIntent(context)
        fullEkycLauncher.launch(intent)
    }
}


private fun createFullEkycIntent(context: Context): Intent {
    return Intent(context, VnptIdentityActivity::class.java).apply {
        // Cấu hình token và key - BẮT BUỘC
        putExtra(KeyIntentConstants.ACCESS_TOKEN, BuildConfig.VNPT_ACCESS_TOKEN)
        putExtra(KeyIntentConstants.TOKEN_ID, BuildConfig.VNPT_TOKEN_ID)
        putExtra(KeyIntentConstants.TOKEN_KEY, BuildConfig.VNPT_TOKEN_KEY)

        // Cấu hình loại tài liệu
        putExtra(KeyIntentConstants.DOCUMENT_TYPE, SDKEnum.DocumentTypeEnum.IDENTITY_CARD.value)

        // Cấu hình phiên bản SDK
        putExtra(KeyIntentConstants.VERSION_SDK, SDKEnum.VersionSDKEnum.ADVANCED.value)

        // Cấu hình hiển thị
        putExtra(KeyIntentConstants.IS_SHOW_TUTORIAL, true)
        putExtra(KeyIntentConstants.IS_ENABLE_GOT_IT, true)

        // Cấu hình so sánh khuôn mặt - BẮT BUỘC để có COMPARE_FACE_RESULT
        putExtra(KeyIntentConstants.IS_ENABLE_COMPARE, true)

        // Cấu hình kiểm tra liveness
        putExtra(KeyIntentConstants.CHECK_LIVENESS_FACE, SDKEnum.ModeCheckLiveNessFace.iBETA.value)
        putExtra(KeyIntentConstants.IS_CHECK_MASKED_FACE, true)
        putExtra(KeyIntentConstants.IS_CHECK_LIVENESS_CARD, true)

        // Cấu hình validation
        putExtra(KeyIntentConstants.IS_VALIDATE_POSTCODE, true)
        putExtra(
            KeyIntentConstants.VALIDATE_DOCUMENT_TYPE,
            SDKEnum.ValidateDocumentType.Basic.value
        )

        // Cấu hình ngôn ngữ
        putExtra(KeyIntentConstants.LANGUAGE_SDK, SDKEnum.LanguageEnum.VIETNAMESE.value)

        // Cấu hình scan QR code
        putExtra(KeyIntentConstants.IS_ENABLE_SCAN_QRCODE, true)
    }
}

