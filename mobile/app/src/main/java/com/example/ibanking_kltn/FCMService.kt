package com.example.ibanking_kltn

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.ibanking_kltn.data.di.TokenManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {
    //    @Inject
//    lateinit var appScope: AppCoroutineScope
//
//    @Inject
//    lateinit var fcmRepository: FCMRepository
//
//    @Inject
//    lateinit var biometricManager: BiometricManager
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        tokenManager.setFcmToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val data = remoteMessage.notification
        Log.d(
            "FCM",
            "Message received from: ${remoteMessage.from}, data: ${data?.body}, title: ${data?.title}"
        )
        if (data != null) {
            showNotification(data.title, data.body, null)
            return
        }
    }

    private fun showNotification(
        title: String?,
        body: String?,
        screen: String?
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("screen", screen)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "default_channel")
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle(title ?: "Thông báo")
            .setContentText(body ?: "")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}