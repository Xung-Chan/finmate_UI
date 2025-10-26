package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.data.dtos.requests.LoginRequest
import com.example.ibanking_kltn.data.dtos.responses.ApiResponse
import com.example.ibanking_kltn.data.dtos.responses.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/login")

    suspend fun login(
    @Header("X-App-Token")appToken: String = "android_app_secret_key",
        @Body request: LoginRequest
    ): Response<LoginResponse>
}