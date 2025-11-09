package com.example.ibanking_kltn.data.security

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.ibanking_kltn.data.api.NonAuthApi
import com.example.ibanking_kltn.data.dtos.requests.RefreshTokenRequest
import com.example.ibanking_kltn.data.dtos.responses.LoginResponse
import jakarta.inject.Inject
import jakarta.inject.Named
import jakarta.inject.Singleton
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit

@Singleton
class TokenAuthenticator @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    @Named("NonAuthRetrofit") private val retrofit: Retrofit
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) {
            val refreshToken = sharedPreferences.getString("refresh", null) ?: return null
            val newTokenResponse = refreshAccessToken(refreshToken) ?: return null
            sharedPreferences.edit {
                putString("access", newTokenResponse.access_token)
                putString("refresh", newTokenResponse.refresh_token)
            }
            return response.request().newBuilder()
                .header("Authorization", "Bearer ${newTokenResponse.access_token}")
                .build()
        }

    }

    fun refreshAccessToken(refreshToken: String): LoginResponse? {
        val response = retrofit.create(NonAuthApi::class.java)
            .refreshToken(request = RefreshTokenRequest(refreshToken))
            .execute()

        if (!response.isSuccessful) {
            return null
        }
        val apiResponse = response.body()
        if (apiResponse == null) {
            return null
        }
        return apiResponse
    }

}
