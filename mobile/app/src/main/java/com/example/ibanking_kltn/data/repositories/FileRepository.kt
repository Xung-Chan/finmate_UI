package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.FileApi
import com.example.ibanking_kltn.data.api.NonAuthApi
import com.example.ibanking_kltn.data.exception.safeApiCall
import com.example.ibanking_kltn.dtos.requests.PresignUploadRequest
import com.example.ibanking_kltn.dtos.responses.FileUploadResponse
import com.example.ibanking_soa.data.utils.ApiResult
import okhttp3.RequestBody
import javax.inject.Inject

class FileRepository @Inject constructor(
    private val fileApi: FileApi,
    private val nonAuthApi: NonAuthApi,

) {
    suspend fun uploadFile(
        url: String,
        contentType: String,
        body: RequestBody
    ): ApiResult<Unit> {
        return safeApiCall { nonAuthApi.uploadFile(url, contentType, body) }
    }

    suspend fun presignUpload(request: PresignUploadRequest): ApiResult<FileUploadResponse> {
        return safeApiCall { fileApi.presignUpload(request) }
    }

    suspend fun getFileInfo(fullObjectKey: String): ApiResult<FileUploadResponse> {
        return safeApiCall { fileApi.getFileInfo(fullObjectKey) }
    }
}

