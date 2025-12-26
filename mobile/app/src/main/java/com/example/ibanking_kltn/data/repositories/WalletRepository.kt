package com.example.ibanking_kltn.data.repositories

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.example.ibanking_kltn.data.api.WalletApi
import com.example.ibanking_kltn.data.dtos.requests.WalletVerificationRequest
import com.example.ibanking_kltn.data.dtos.responses.WalletResponse
import com.example.ibanking_kltn.data.dtos.responses.WalletVerificationResponse
import com.example.ibanking_soa.data.utils.ApiResult
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class WalletRepository @Inject constructor(
    private val walletApi: WalletApi,
    @ApplicationContext private val context: Context
) {
    suspend fun getMyWalletInfor(): ApiResult<WalletResponse> {
//        return safeApiCall(
//            apiCall = { walletApi.getMyWallet() }
//        )
        return ApiResult.Success(
            data = WalletResponse(
                walletNumber = "1234567890",
                id = "ID",
                mail = "sbakjsbask",
                balance = 100000000.0,
                merchantName = "Nguyen Van A",
                status = "ACTIVE",
                username = "avsjhsbasa",
                verified = true
            )
        )
    }


    suspend fun getInfoByWalletNumber(walletNumber: String): ApiResult<WalletResponse> {
//        return safeApiCall(
//            apiCall = { walletApi.getInfoByWalletNumber(walletNumber = walletNumber) }
//        )
        return ApiResult.Success(
            data = WalletResponse(
                walletNumber = "1234567890",
                id = "ID",
                mail = "sbakjsbask",
                balance = 10000.0,
                merchantName = "Nguyen Van A",
                status = "ACTIVE",
                username = "avsjhsbasa",
                verified = true

            )
        )

    }

    suspend fun createWalletVerification(
        request: WalletVerificationRequest,
        documentUris: List<Uri>
    ): ApiResult<WalletVerificationResponse> {

        try {
            val gson = Gson()
            val jsonString = gson.toJson(request)

            // 2. Tạo RequestBody cho data field
            val dataRequestBody = jsonString.toRequestBody("application/json".toMediaTypeOrNull())

            // 3. Convert URIs sang MultipartBody.Part
            val documentParts = documentUris.mapIndexed { index, uri ->
                createMultipartFromUri(uri, "documents", index)
            }

//            return safeApiCall(
//                apiCall = {
//                    walletApi.createVerificationRequest(
//                        data = dataRequestBody,
//                        documents = documentParts
//                    )
//                }
//            )
            delay(1000L)
            return ApiResult.Success(
                data = WalletVerificationResponse(
                    businessAddress = "address",
                    businessCode = "code",
                    businessName = "name",
                    contactEmail = "email",
                    contactPhone = "phone",
                    createdAt = "date",
                    id = "id",
                    invoiceDisplayName = "displayname",
                    processedAt = "18/06/2024",
                    processedBy = "admin",
                    representativeIdNumber = "idnumber",
                    representativeIdType = "idtype",
                    representativeName = "repname",
                    status = "PENDING",
                    verifiedDocuments = listOf(),
                    walletNumber = "1234567890"
                )
            )

        } catch (e: Exception) {
            Log.e("WalletVerificationRepo", "Error uploading", e)
            return ApiResult.Error("Exception: ${e.message}")
        }


    }

    private fun createMultipartFromUri(
        uri: Uri,
        partName: String,
        index: Int
    ): MultipartBody.Part {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes() ?: ByteArray(0)
        inputStream?.close()

        val fileName = getFileName(uri) ?: "document_$index.jpg"
        val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            partName,
            fileName,
            requestBody
        )
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