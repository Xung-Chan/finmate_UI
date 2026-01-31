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
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show()
        }

        if (!notificationGranted) {
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_LONG).show()
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
                AppScreen()
            }
        }
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

}