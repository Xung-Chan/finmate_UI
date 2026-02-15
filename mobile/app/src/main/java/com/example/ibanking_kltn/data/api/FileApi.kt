package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.BuildConfig.X_AUTHORIZATION_FILE
import com.example.ibanking_kltn.dtos.requests.PresignUploadRequest
import com.example.ibanking_kltn.dtos.responses.FileUploadResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.Url

interface FileApi {
   
    @POST("/api/files/presign/upload")
    suspend fun presignUpload(
        @Body request: PresignUploadRequest,
    ): Response<FileUploadResponse>

    @GET("/api/files/")
    suspend fun getFileInfo(
        @Query("fullObjectKey") fullObjectKey: String,
        @Header("X-Authorization-File") authorization: String = X_AUTHORIZATION_FILE
    ): Response<FileUploadResponse>


}
