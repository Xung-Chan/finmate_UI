package com.example.ibanking_kltn.data.repositories

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.ibanking_kltn.data.api.WalletApi
import com.example.ibanking_kltn.data.dtos.requests.WalletVerificationRequest
import com.example.ibanking_kltn.data.dtos.responses.WalletResponse
import com.example.ibanking_kltn.data.dtos.responses.WalletVerificationResponse
import com.example.ibanking_kltn.ui.exception.safeApiCall
import com.example.ibanking_kltn.utils.createMultipartFromUri
import com.example.ibanking_soa.data.utils.ApiResult
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class WalletRepository @Inject constructor(
    private val walletApi: WalletApi,
    @ApplicationContext private val context: Context
) {
    suspend fun getMyWalletInfor(): ApiResult<WalletResponse> {
       return safeApiCall(
           apiCall = { walletApi.getMyWallet() }
       )
        // return ApiResult.Success(
        //     data = WalletResponse(
        //         walletNumber = "1234567890",
        //         id = "ID",
        //         mail = "sbakjsbask",
        //         balance = 100000000.0,
        //         merchantName = "Nguyen Van A",
        //         status = "ACTIVE",
        //         username = "avsjhsbasa",
        //         verified = true
        //     )
        // )
    }


    suspend fun getInfoByWalletNumber(walletNumber: String): ApiResult<WalletResponse> {
       return safeApiCall(
           apiCall = { walletApi.getInfoByWalletNumber(walletNumber = walletNumber) }
       )
        // return ApiResult.Success(
        //     data = WalletResponse(
        //         walletNumber = "1234567890",
        //         id = "ID",
        //         mail = "sbakjsbask",
        //         balance = 10000.0,
        //         merchantName = "Nguyen Van A",
        //         status = "ACTIVE",
        //         username = "avsjhsbasa",
        //         verified = true

        //     )
        // )

    }

    suspend fun createWalletVerification(
        request: WalletVerificationRequest,
        documentUris: List<Uri>
    ): ApiResult<WalletVerificationResponse> {

        try {
            val gson = Gson()
            val jsonString = gson.toJson(request)
            val dataRequestBody = jsonString.toRequestBody("application/json".toMediaTypeOrNull())
            val documentParts = documentUris.mapIndexed { index, uri ->
                createMultipartFromUri(
                    uri = uri,
                    partName = "documents",
                    context = context
                )
            }

           return safeApiCall(
               apiCall = {
                   walletApi.createVerificationRequest(
                       data = dataRequestBody,
                       documents = documentParts
                   )
               }
           )
            // delay(1000L)
            // return ApiResult.Success(
            //     data = WalletVerificationResponse(
            //         businessAddress = "address",
            //         businessCode = "code",
            //         businessName = "name",
            //         contactEmail = "email",
            //         contactPhone = "phone",
            //         createdAt = "date",
            //         id = "id",
            //         invoiceDisplayName = "displayname",
            //         processedAt = "18/06/2024",
            //         processedBy = "admin",
            //         representativeIdNumber = "idnumber",
            //         representativeIdType = "idtype",
            //         representativeName = "repname",
            //         status = "PENDING",
            //         verifiedDocuments = listOf(),
            //         walletNumber = "1234567890"
            //     )
            // )

        } catch (e: Exception) {
            Log.e("WalletVerificationRepo", "Error uploading", e)
            return ApiResult.Error("Exception: ${e.message}")
        }


    }



}