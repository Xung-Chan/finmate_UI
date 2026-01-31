package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.dtos.requests.ChangePasswordRequest
import com.example.ibanking_kltn.dtos.responses.UpdateAvatarResponse
import com.example.ibanking_kltn.dtos.responses.UserInfoResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part


//for auth activity
interface AuthApi {

    @POST("/api/accounts/change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): Response<Unit>


    @GET("/api/accounts/profile/me")
    suspend fun getMyProfile(
    ): Response<UserInfoResponse>

    @Multipart
    @PUT("/api/accounts/avatar")
    suspend fun updateImageProfile(
        @Part file: MultipartBody.Part
    ): Response<UpdateAvatarResponse>

}