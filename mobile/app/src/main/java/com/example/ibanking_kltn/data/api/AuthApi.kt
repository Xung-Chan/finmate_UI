package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.data.dtos.requests.ChangePasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


//for auth activity
interface AuthApi {

    @POST("/api/accounts/change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): Response<Unit>


}