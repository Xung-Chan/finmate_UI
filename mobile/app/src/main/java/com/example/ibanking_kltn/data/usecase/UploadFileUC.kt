package com.example.ibanking_kltn.data.usecase

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.ibanking_kltn.data.repositories.FileRepository
import com.example.ibanking_kltn.dtos.definitions.PresignedFileType
import com.example.ibanking_kltn.dtos.requests.PresignUploadRequest
import com.example.ibanking_kltn.dtos.responses.FileUploadResponse
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID
import javax.inject.Inject

class UploadFileUC @Inject constructor(
    private val fileRepository: FileRepository,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(
        fileType: PresignedFileType,
        uri: Uri
    ): ApiResult<FileUploadResponse> {
        // Get file information from Uri
        val fileName = getFileName(uri) ?: "${UUID.randomUUID()}.jpg"
        val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"

        // Step 1: Get presigned upload URL
        val presignRequest = PresignUploadRequest(
            contentType = mimeType,
            fileName = fileName,
            fileType = fileType
        )

        val presignResult = fileRepository.presignUpload(presignRequest)

        when (presignResult) {
            is ApiResult.Success -> {
                val fileUploadResponse = presignResult.data

                // Step 2: Read file content from Uri
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes() ?: return ApiResult.Error(
                    message = "Failed to read file content"
                )
                inputStream.close()

                // Step 3: Upload file to presigned URL
                // Use the exact contentType from the presigned response
                val contentTypeToUse = fileUploadResponse.originalFileContentType
                val requestBody = bytes.toRequestBody(null) // No MediaType in body, let header control it
                val uploadResult = fileRepository.uploadFile(
                    url = fileUploadResponse.uploadUrl,
                    contentType = contentTypeToUse,
                    body = requestBody
                )

                return when (uploadResult) {
                    is ApiResult.Success -> {
                        // Return the file upload response with all metadata
                        ApiResult.Success(data = fileUploadResponse)
                    }
                    is ApiResult.Error -> {
                        ApiResult.Error(message = uploadResult.message)
                    }
                }
            }

            is ApiResult.Error -> {
                return ApiResult.Error(message = presignResult.message)
            }
        }
    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index != -1) {
                        result = it.getString(index)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result
    }
}