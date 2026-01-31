package com.example.ibanking_kltn.data.di

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.ibanking_kltn.dtos.definitions.LastLoginUser
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json

@Singleton
class TokenManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    private val ACCESS_KEY = "ACCESS_TOKEN"
    private val REFRESH_KEY = "REFRESH_TOKEN"

    private val LAST_LOGIN = "LAST_LOGIN_USER"

    private val FCM_TOKEN ="FCM_TOKEN"

    fun setFcmToken(fcmToken: String) {
        sharedPreferences.edit {
            putString(FCM_TOKEN, fcmToken)
        }
    }
    fun getFcmToken(): String? {
        return sharedPreferences.getString(FCM_TOKEN, null)
    }
    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_KEY, null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_KEY, null)
    }

    fun getLastLoginUser(): LastLoginUser? {
        val payload= sharedPreferences.getString(LAST_LOGIN, null)
        return payload?.let {
            Json.decodeFromString<LastLoginUser>(it)
        }
    }
    fun clearLastLoginUser() {
        sharedPreferences.edit {
            remove(LAST_LOGIN)
        }
    }

    fun setLastLoginUser(lastLoginUser: LastLoginUser) {
        val payload = Json.encodeToString(lastLoginUser)
        sharedPreferences.edit {
            putString(LAST_LOGIN, payload)
        }
    }

    fun updateToken(access: String, refresh: String) {
        sharedPreferences.edit {
            putString(ACCESS_KEY,   access)
            putString(REFRESH_KEY, refresh)
        }
    }

    fun clearToken() {
        sharedPreferences.edit {
            remove(ACCESS_KEY)
            remove(REFRESH_KEY)
        }
    }
}