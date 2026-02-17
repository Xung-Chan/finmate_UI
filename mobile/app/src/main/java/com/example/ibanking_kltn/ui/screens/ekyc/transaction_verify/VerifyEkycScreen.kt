package com.example.ibanking_kltn.ui.screens.ekyc.transaction_verify

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
import com.vnptit.idg.sdk.activity.VnptPortraitActivity
import com.vnptit.idg.sdk.utils.KeyIntentConstants
import com.vnptit.idg.sdk.utils.SDKEnum

/**
 * Main eKYC Screen - Sử dụng kiến trúc MVI/MVVM
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun VerifyTransactionEkycScreen(
    onEvent: (VerifyTransactionEkycEvent) -> Unit,
) {
    val context = LocalContext.current

    val faceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        onEvent(
            VerifyTransactionEkycEvent.HandleEkycResult(activityResult = result)
        )
    }
    LaunchedEffect(Unit) {
        val intent = createAuthorizationFaceIntent(context)
        faceLauncher.launch(intent)
    }

}


private fun createAuthorizationFaceIntent(context: Context): Intent {
    return Intent(context, VnptPortraitActivity::class.java).apply {
        // Cấu hình token và key - BẮT BUỘC
        putExtra(KeyIntentConstants.ACCESS_TOKEN, BuildConfig.VNPT_ACCESS_TOKEN)
        putExtra(KeyIntentConstants.TOKEN_ID, BuildConfig.VNPT_TOKEN_ID)
        putExtra(KeyIntentConstants.TOKEN_KEY, BuildConfig.VNPT_TOKEN_KEY)

        // Cấu hình hiển thị
        putExtra(KeyIntentConstants.IS_SHOW_TUTORIAL, true)
        putExtra(KeyIntentConstants.IS_CHECK_MASKED_FACE, true)
        putExtra(KeyIntentConstants.IS_ENABLE_GOT_IT, true)


        // Cấu hình kiểm tra liveness
        putExtra(KeyIntentConstants.CHECK_LIVENESS_FACE, SDKEnum.ModeCheckLiveNessFace.iBETA.value)

        // Cấu hình phiên bản SDK
        putExtra(KeyIntentConstants.VERSION_SDK, SDKEnum.VersionSDKEnum.ADVANCED.value)

        // Cấu hình ngôn ngữ
        putExtra(KeyIntentConstants.LANGUAGE_SDK, SDKEnum.LanguageEnum.VIETNAMESE.value)
    }
}

