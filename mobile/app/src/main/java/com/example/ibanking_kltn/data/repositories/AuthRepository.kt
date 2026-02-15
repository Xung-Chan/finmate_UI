package com.example.ibanking_kltn.data.repositories

import android.content.Context
import com.example.ibanking_kltn.data.api.AuthApi
import com.example.ibanking_kltn.data.api.BiometricApi
import com.example.ibanking_kltn.data.api.NonAuthApi
import com.example.ibanking_kltn.data.exception.safeApiCall
import com.example.ibanking_kltn.dtos.requests.ChangePasswordRequest
import com.example.ibanking_kltn.dtos.requests.LoginRequest
import com.example.ibanking_kltn.dtos.requests.LoginViaBiometricRequest
import com.example.ibanking_kltn.dtos.requests.RegisterBiometricRequest
import com.example.ibanking_kltn.dtos.requests.RequestOtpRequest
import com.example.ibanking_kltn.dtos.requests.ResetPasswordRequest
import com.example.ibanking_kltn.dtos.requests.SendOtpRequest
import com.example.ibanking_kltn.dtos.requests.UpdateAvatarRequest
import com.example.ibanking_kltn.dtos.requests.VerifyOtpRequest
import com.example.ibanking_kltn.dtos.responses.LoginResponse
import com.example.ibanking_kltn.dtos.responses.RegisterBiometricResponse
import com.example.ibanking_kltn.dtos.responses.RequestOtpResponse
import com.example.ibanking_kltn.dtos.responses.UpdateAvatarResponse
import com.example.ibanking_kltn.dtos.responses.UserInfoResponse
import com.example.ibanking_kltn.dtos.responses.VerifyOtpResponse
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val nonAuthApi: NonAuthApi,
    private val biometricApi: BiometricApi,
    @ApplicationContext private val context: Context
) {
    //    suspend fun changePassword(
//        request: ChangePasswordRequest
//    ): ApiResult<Unit> {
//        return ApiResult.Success(Unit)
//    }
//
//
//    suspend fun login(
//        request: LoginRequest
//    ): ApiResult<LoginResponse> {
//        return ApiResult.Success(
//            LoginResponse(
//                access_token = "mocked-access-token",
//                refresh_token = "mocked-refresh-token",
//                id_token = "mocked-id-token",
//                expires_in = 100
//            )
//        )
//    }
//
//
//    suspend fun loginViaBiometric(
//        request: LoginViaBiometricRequest
//    ): ApiResult<LoginResponse> {
//        return ApiResult.Success(
//            LoginResponse(
//                access_token = "mocked-access-token",
//                refresh_token = "mocked-refresh-token",
//                id_token = "mocked-id-token",
//                expires_in = 100
//            )
//        )
//    }
//
//
//    suspend fun registerBiometric(
//        request: RegisterBiometricRequest
//    ): ApiResult<RegisterBiometricResponse> {
//        return ApiResult.Success(
//            RegisterBiometricResponse(
//                biometricKey = "mocked-biometric-key"
//            )
//        )
//    }
//
//
//    suspend fun cancelBiometric(
//        request: RegisterBiometricRequest
//    ): ApiResult<Unit> {
//        return ApiResult.Success(
//            data = Unit
//        )
//    }
//
//    suspend fun requestOtp(request: RequestOtpRequest): ApiResult<RequestOtpResponse> {
//        return ApiResult.Success(
//            RequestOtpResponse(
//                maskedMail = "nmd****@gmail.com",
//                verifyKeyDurationMinutes = 2000L,
//                verifyKey = "mocked-verify-key"
//            )
//        )
//    }
//
//    suspend fun sendOtp(request: SendOtpRequest): ApiResult<Unit> {
//
//
//        return ApiResult.Success(
//            data = Unit
//        )
//    }
//    suspend fun verifyOtp(request: VerifyOtpRequest): ApiResult<VerifyOtpResponse> {
//
//        return ApiResult.Success(
//            data = VerifyOtpResponse(
//                email = "nmd****@gmail.com",
//                resetPasswordToken = "mocked-verify-key",
//            )
//        )
//    }
//    suspend fun resetPassword(request: ResetPasswordRequest): ApiResult<Unit> {
//        return ApiResult.Success(
//            data = Unit
//        )
//    }
//
//    suspend fun getMyProfile(): ApiResult<UserInfoResponse> {
//        return ApiResult.Success(
//            UserInfoResponse(
//                id = "user-id-123",
//                username = "mocked-username",
//                fullName = "Mocked User",
//                mail = "mocked-mail",
//                status = UserStatusEnum.ACTIVE,
//                phoneNumber = "1234567890",
//                avatarUrl = "https://placehold.jp/150x150.png",
//                dateOfBirth = "1990-01-01",
//                gender = "Male",
//                address = "123 Mock St, Mock City",
//                cardId = "ID123456789"
//            )
//        )
//    }
//
//
//    suspend fun updateAvatar(
//        imageUrl: Uri
//    ): ApiResult<UpdateAvatarResponse> {
//        delay(1000L)
//        return ApiResult.Success(
//            data = UpdateAvatarResponse(
//                imageUrl = "https://placehold.jp/150x150.png"
//            )
//        )
//    }
    suspend fun updateAvatar(
        request: UpdateAvatarRequest
    ): ApiResult<UpdateAvatarResponse> {
        return safeApiCall {
            authApi.updateImageProfile(
                request = request
            )
        }
    }

    suspend fun getMyProfile(): ApiResult<UserInfoResponse> {
        return safeApiCall { authApi.getMyProfile() }
    }

    suspend fun resetPassword(request: ResetPasswordRequest): ApiResult<Unit> {
        return safeApiCall { nonAuthApi.resetPassword(request = request) }
    }

    suspend fun verifyOtp(request: VerifyOtpRequest): ApiResult<VerifyOtpResponse> {
        return safeApiCall { nonAuthApi.verifyOtp(request = request) }
    }

    suspend fun sendOtp(request: SendOtpRequest): ApiResult<Unit> {
        return safeApiCall { nonAuthApi.sendOtp(request = request) }
    }

    suspend fun cancelBiometric(
        request: RegisterBiometricRequest
    ): ApiResult<Unit> {
        return safeApiCall { biometricApi.cancelBiometric(request = request) }
    }

    suspend fun requestOtp(request: RequestOtpRequest): ApiResult<RequestOtpResponse> {
        return safeApiCall { nonAuthApi.requestOtp(request = request) }
    }

    suspend fun registerBiometric(
        request: RegisterBiometricRequest
    ): ApiResult<RegisterBiometricResponse> {
        return safeApiCall { biometricApi.registerBiometric(request = request) }
    }

    suspend fun loginViaBiometric(
        request: LoginViaBiometricRequest
    ): ApiResult<LoginResponse> {
        return safeApiCall { nonAuthApi.loginViaBiometric(request = request) }
    }

    suspend fun login(
        request: LoginRequest
    ): ApiResult<LoginResponse> {
        return safeApiCall { nonAuthApi.login(request = request) }
    }

    suspend fun changePassword(
        request: ChangePasswordRequest
    ): ApiResult<Unit> {
        return safeApiCall { authApi.changePassword(request = request) }
    }


}