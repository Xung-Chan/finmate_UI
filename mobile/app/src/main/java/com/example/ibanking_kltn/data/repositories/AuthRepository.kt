package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.AuthApi
import com.example.ibanking_kltn.data.api.NonAuthApi
import com.example.ibanking_kltn.data.dtos.requests.LoginRequest
import com.example.ibanking_kltn.data.dtos.responses.LoginResponse
import com.example.ibanking_kltn.ui.exception.safeApiCall
import com.example.ibanking_soa.data.utils.ApiResult
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val nonAuthApi: NonAuthApi
) {
    suspend fun login(request: LoginRequest): ApiResult<LoginResponse> {
        return safeApiCall{ nonAuthApi.login(request = request) }

    }
}