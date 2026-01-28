package com.example.ibanking_kltn

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.ibanking_kltn.data.di.AppSessionManager
import com.example.ibanking_kltn.ui.AppScreen
import com.example.ibanking_kltn.ui.theme.IBanking_KLTNTheme
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @Inject
    lateinit var appSessionManager: AppSessionManager

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(appSessionManager)

        //request camera permission
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        val context = this.applicationContext
        createNotificationChannel(context)
        enableEdgeToEdge()
        try {

            setContent {
                IBanking_KLTNTheme {
                    AppScreen()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                this,
                "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}