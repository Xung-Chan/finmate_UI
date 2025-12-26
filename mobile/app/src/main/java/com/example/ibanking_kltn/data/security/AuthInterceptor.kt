package com.example.ibanking_kltn.data.security

import com.example.ibanking_kltn.data.di.TokenManager
import jakarta.inject.Inject
import jakarta.inject.Singleton
import okhttp3.Interceptor
import okhttp3.Response

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token =tokenManager.getAccessToken()
        val request = chain.request().newBuilder().apply {
            if (token != null) {
                header("Authorization", "Bearer $token")
            }
        }.build()
        return chain.proceed(request)
    }
}