package com.example.ibanking_kltn

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.ibanking_kltn.data.di.AppSessionManager
import com.example.ibanking_kltn.ui.AppScreen
import com.example.ibanking_kltn.ui.theme.IBanking_KLTNTheme
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @Inject
    lateinit var appSessionManager: AppSessionManager

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val multiplePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
        val notificationGranted =
            permissions[Manifest.permission.POST_NOTIFICATIONS] ?: false

        if (!cameraGranted) {
            Toast.makeText(
                this,
                "Quyền Camera bị từ chối. Vui lòng cấp quyền để sử dụng eKYC",
                Toast.LENGTH_LONG
            ).show()
        }

        if (!notificationGranted) {
            Toast.makeText(this, "Quyền Thông báo bị từ chối", Toast.LENGTH_LONG).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(appSessionManager)

        multiplePermissionLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.POST_NOTIFICATIONS
            )
        )
        val context = this.applicationContext
        createNotificationChannel(context)

        registerFcmTokenIfNeeded()

        enableEdgeToEdge()
        setContent {
            IBanking_KLTNTheme {
                // Sử dụng EkycScreen mới với kiến trúc MVI/MVVM
//                EkycScreen(
//                    onNavigateBack = {
//                        // Handle back navigation
//                        finish()
//                    },
//                    onEkycComplete = { ocrData, faceCompareResult ->
//                        // Handle eKYC completion
//                        Toast.makeText(
//                            this,
//                            "eKYC hoàn tất: ${ocrData.name}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        // TODO: Navigate to next screen or save data
//                    }
//                )
                AppScreen()
            }
        }
    }

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            "default_channel",
            "Thông báo",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Thông báo hệ thống"
        }

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun registerFcmTokenIfNeeded() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM", token)
        }
    }

    // Old eKYC functions - MOVED TO EkycViewModel and EkycScreen
    // Keeping for reference, can be removed later

//    private val faceLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//
//            if (result.resultCode == Activity.RESULT_OK) {
//                val data = result.data ?: return@registerForActivityResult
//
//                // Lấy các kết quả từ eKYC face check
//                val compareFaceResult = data.getStringExtra(KeyResultConstants.COMPARE_FACE_RESULT)
//                val livenessFaceResult =
//                    data.getStringExtra(KeyResultConstants.LIVENESS_FACE_RESULT)
//                val clientSessionResult =
//                    data.getStringExtra(KeyResultConstants.CLIENT_SESSION_RESULT)
//                val maskedFaceResult = data.getStringExtra(KeyResultConstants.MASKED_FACE_RESULT)
//
//                // Đường dẫn ảnh chụp
//                val pathImageNear =
//                    data.getStringExtra(KeyResultConstants.PATH_IMAGE_FACE_NEAR_FULL)
//                val pathImageFar = data.getStringExtra(KeyResultConstants.PATH_IMAGE_FACE_FAR_FULL)
//
//                // Hash của ảnh
//                val hashImageNear = data.getStringExtra(KeyResultConstants.HASH_IMAGE_FACE_NEAR)
//                val hashImageFar = data.getStringExtra(KeyResultConstants.HASH_IMAGE_FACE_FAR)
//
//                val lastStep = data.getStringExtra(KeyResultConstants.LAST_STEP)
//
//                Log.d("EKYC_FACE", "Compare Face Result: $compareFaceResult")
//                Log.d("EKYC_FACE", "Liveness Face Result: $livenessFaceResult")
//                Log.d("EKYC_FACE", "Client Session Result: $clientSessionResult")
//                Log.d("EKYC_FACE", "Masked Face Result: $maskedFaceResult")
//                Log.d("EKYC_FACE", "Path Image Near: $pathImageNear")
//                Log.d("EKYC_FACE", "Path Image Far: $pathImageFar")
//                Log.d("EKYC_FACE", "Hash Image Near: $hashImageNear")
//                Log.d("EKYC_FACE", "Hash Image Far: $hashImageFar")
//                Log.d("EKYC_FACE", "Last Step: $lastStep")
//
//                if (lastStep != SDKEnum.LastStepEnum.Done.getValue()) {
//                    Log.d("EKYC_FACE", "User thoát giữa chừng")
//                    Toast.makeText(this, "Đã hủy eKYC", Toast.LENGTH_SHORT).show()
//                    return@registerForActivityResult
//                }
//
//                Toast.makeText(this, "eKYC xác thực khuôn mặt thành công!", Toast.LENGTH_SHORT)
//                    .show()
//            } else {
//                Toast.makeText(this, "eKYC thất bại hoặc bị hủy", Toast.LENGTH_SHORT).show()
//            }
//        }


}