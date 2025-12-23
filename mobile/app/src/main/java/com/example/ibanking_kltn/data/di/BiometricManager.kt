package com.example.ibanking_kltn.data.di

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.ibanking_kltn.data.dtos.BiometricStorage
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import java.security.MessageDigest
import java.util.UUID

@Singleton
class BiometricManager @Inject constructor(
    private val tokenManager: TokenManager,
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val BIOMETRIC_KEY = "BIOMETRIC_KEY"
        private const val DEVICE_TOKEN_KEY = "DEVICE_TOKEN_KEY"

    }

    fun generateDeviceToken(): String {
        val components = listOf(
            UUID.randomUUID().toString(),
            System.currentTimeMillis().toString(),
        )

        val rawToken = components.joinToString("-")

        val token = MessageDigest.getInstance("SHA-256")
            .digest(rawToken.toByteArray())
            .joinToString("") { "%02x".format(it) }

        setDeviceToken(token)

        return token
    }
    private fun setDeviceToken(deviceToken: String) {
        sharedPreferences.edit {
            putString(DEVICE_TOKEN_KEY, deviceToken)
        }
    }

    fun getDeviceToken(): String? {
        return sharedPreferences.getString(DEVICE_TOKEN_KEY, null)
    }

    fun getBiometricKey(): BiometricStorage? {
        val payload= sharedPreferences.getString(BIOMETRIC_KEY, null)
        return payload?.let {
            Json.decodeFromString<BiometricStorage>(it)
        }
    }


    fun setBioBiometricKey(biometricKey: String) {
        val lastLoginUser = tokenManager.getLastLoginUser()
        if (lastLoginUser == null) {
            throw IllegalStateException("No logged in user found")
        }
        val biometricPayload = BiometricStorage(
            biometricKey = biometricKey,
            deviceId = getDeviceToken() ?: generateDeviceToken(),
            username =lastLoginUser.username
        )
        val payload = Json.encodeToString(biometricPayload)
        sharedPreferences.edit {
            putString(BIOMETRIC_KEY, payload)
        }
    }



    fun clear() {
        sharedPreferences.edit {
            remove(BIOMETRIC_KEY)
        }
    }
}