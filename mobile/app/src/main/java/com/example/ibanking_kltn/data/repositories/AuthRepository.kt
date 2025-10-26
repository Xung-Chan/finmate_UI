package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.AuthApi
import com.example.ibanking_kltn.data.dtos.requests.LoginRequest
import com.example.ibanking_kltn.data.dtos.responses.LoginResponse
import com.example.ibanking_kltn.utils.safeApiCall
import com.example.ibanking_soa.data.utils.ApiResult
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private  val  api: AuthApi
) {
    suspend fun login(loginRequest: LoginRequest): ApiResult<LoginResponse> {
        return safeApiCall { api.login(request = loginRequest) }
    }
}