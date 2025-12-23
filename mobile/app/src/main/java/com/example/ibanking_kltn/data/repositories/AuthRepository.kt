package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.AuthApi
import com.example.ibanking_kltn.data.api.BiometricApi
import com.example.ibanking_kltn.data.api.NonAuthApi
import com.example.ibanking_kltn.data.dtos.requests.ChangePasswordRequest
import com.example.ibanking_kltn.data.dtos.requests.LoginRequest
import com.example.ibanking_kltn.data.dtos.requests.LoginViaBiometricRequest
import com.example.ibanking_kltn.data.dtos.requests.RegisterBiometricRequest
import com.example.ibanking_kltn.data.dtos.requests.RequestOtpRequest
import com.example.ibanking_kltn.data.dtos.requests.ResetPasswordRequest
import com.example.ibanking_kltn.data.dtos.requests.SendOtpRequest
import com.example.ibanking_kltn.data.dtos.requests.VerifyOtpRequest
import com.example.ibanking_kltn.data.dtos.responses.LoginResponse
import com.example.ibanking_kltn.data.dtos.responses.RegisterBiometricResponse
import com.example.ibanking_kltn.data.dtos.responses.RequestOtpResponse
import com.example.ibanking_kltn.data.dtos.responses.VerifyOtpResponse
import com.example.ibanking_soa.data.utils.ApiResult
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val nonAuthApi: NonAuthApi,
    private val biometricApi: BiometricApi
) {


    suspend fun changePassword(
        request: ChangePasswordRequest
    ): ApiResult<Unit> {
//        return safeApiCall { authApi.changePassword(request = request) }
        return ApiResult.Success(Unit)
    }

    suspend fun login(request: LoginRequest): ApiResult<LoginResponse> {
//        return safeApiCall { nonAuthApi.login(request = request) }
        return ApiResult.Success(
            LoginResponse(
                access_token = "mocked-access-token",
                refresh_token = "mocked-refresh-token",
                id_token = "mocked-id-token",
                expires_in = 100
            )
        )
    }

    suspend fun loginViaBiometric(request: LoginViaBiometricRequest): ApiResult<LoginResponse> {
//        return safeApiCall { nonAuthApi.login(request = request) }
        return ApiResult.Success(
            LoginResponse(
                access_token = "mocked-access-token",
                refresh_token = "mocked-refresh-token",
                id_token = "mocked-id-token",
                expires_in = 100
            )
        )
    }

    suspend fun registerBiometric(request: RegisterBiometricRequest): ApiResult<RegisterBiometricResponse> {
//        return safeApiCall { biometricApi.registerBiometric(request = request) }
        return ApiResult.Success(
            RegisterBiometricResponse(
                biometricKey = "mocked-biometric-key"
            )
        )
    }

    suspend fun requestOtp(request: RequestOtpRequest): ApiResult<RequestOtpResponse> {
//        return safeApiCall { nonAuthApi.requestOtp(request = request) }
        return ApiResult.Success(
            RequestOtpResponse(
                maskedMail = "nmd****@gmail.com",
                verifyKeyDurationMinutes = 2000L,
                verifyKey = "mocked-verify-key"
            )
        )
        return ApiResult.Error("Username koong ton tai")
    }

    suspend fun sendOtp(request: SendOtpRequest): ApiResult<Unit> {
//        return safeApiCall { nonAuthApi.sendOtp(request = request) }
        return ApiResult.Success(
            data = Unit
        )
    }


    suspend fun verifyOtp(request: VerifyOtpRequest): ApiResult<VerifyOtpResponse> {
//        return safeApiCall { nonAuthApi.verifyOtp(request = request) }
        return ApiResult.Success(
            data = VerifyOtpResponse(
                email = "nmd****@gmail.com",
                resetPasswordToken = "mocked-verify-key",
            )
        )
    }


    suspend fun resetPassword(request: ResetPasswordRequest): ApiResult<Unit> {
//        return safeApiCall { nonAuthApi.resetPassword(request = request) }
        return ApiResult.Success(Unit)
    }

}