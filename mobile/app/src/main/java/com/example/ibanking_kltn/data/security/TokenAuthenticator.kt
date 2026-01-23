package com.example.ibanking_kltn.data.security

import com.example.ibanking_kltn.data.api.NonAuthApi
import com.example.ibanking_kltn.data.di.TokenManager
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
    @Named("NonAuthRetrofit") private val retrofit: Retrofit,
    private val tokenManager: TokenManager
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null


        synchronized(this) {
            val currentToken = tokenManager.getAccessToken()
            val requestToken = response.request.header("Authorization")

            if (requestToken != null && requestToken != "Bearer $currentToken") {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            val refreshToken = tokenManager.getRefreshToken() ?: return null
            val newTokenResponse = refreshAccessToken(refreshToken) ?: return null
            tokenManager.updateToken(
                access = newTokenResponse.access_token,
                refresh = newTokenResponse.refresh_token
            )
            return response.request.newBuilder()
                .header("Authorization", "Bearer ${newTokenResponse.access_token}")
                .build()
        }

    }
    private fun responseCount(response: Response): Int {
        var result = 1
        var prior = response.priorResponse
        while (prior != null) {
            result++
            prior = prior.priorResponse
        }
        return result
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


