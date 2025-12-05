package com.example.ibanking_kltn.data.di

import android.content.SharedPreferences
import androidx.core.content.edit
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    private val ACCESS_KEY = "ACCESS_TOKEN"
    private val REFRESH_KEY = "REFRESH_TOKEN"

    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_KEY, null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_KEY, null)
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