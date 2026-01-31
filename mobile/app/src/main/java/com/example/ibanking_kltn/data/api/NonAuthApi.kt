package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.dtos.requests.LoginRequest
import com.example.ibanking_kltn.dtos.requests.LoginViaBiometricRequest
import com.example.ibanking_kltn.dtos.requests.RefreshTokenRequest
import com.example.ibanking_kltn.dtos.requests.RequestOtpRequest
import com.example.ibanking_kltn.dtos.requests.ResetPasswordRequest
import com.example.ibanking_kltn.dtos.requests.SendOtpRequest
import com.example.ibanking_kltn.dtos.requests.VerifyOtpRequest
import com.example.ibanking_kltn.dtos.responses.LoginResponse
import com.example.ibanking_kltn.dtos.responses.RequestOtpResponse
import com.example.ibanking_kltn.dtos.responses.VerifyOtpResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface NonAuthApi {
    @POST("/auth/login")
    suspend fun login(
        @Header("X-App-Token") appToken: String = "android_app_secret_key",
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("/auth/biometric/login")
    suspend fun loginViaBiometric(
        @Header("X-App-Token") appToken: String = "android_app_secret_key",
        @Body request: LoginViaBiometricRequest
    ): Response<LoginResponse>


    @POST("/auth/refresh")
    fun refreshToken(
        @Header("X-App-Token") appToken: String = "android_app_secret_key",
        @Body request: RefreshTokenRequest
    ): Call<LoginResponse>


    @POST("/api/accounts/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<Unit>

    @POST("/api/accounts/send-otp")
    suspend fun sendOtp(
        @Body request: SendOtpRequest
    ): Response<Unit>

    @POST("/api/accounts/request-otp")
    suspend fun requestOtp(
        @Body request: RequestOtpRequest
    ): Response<RequestOtpResponse>


    @POST("/api/accounts/verify-otp")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    ): Response<VerifyOtpResponse>


}