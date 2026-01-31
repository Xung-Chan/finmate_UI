package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.dtos.responses.WalletResponse
import com.example.ibanking_kltn.dtos.responses.WalletVerificationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface WalletApi {
    @GET("/api/wallets/me")
    suspend fun getMyWallet(
    ): Response<WalletResponse>


    @GET("/api/wallets/")
    suspend fun getInfoByWalletNumber(
        @Query(value = "walletNumber") walletNumber: String
    ): Response<WalletResponse>


    @Multipart
    @POST("/api/wallet-verifications/")
    suspend fun createVerificationRequest(
        @Part("data") data: RequestBody,
        @Part documents: List<MultipartBody.Part>
    ): Response<WalletVerificationResponse>


    @GET("/api/wallet-verifications/wallet/me")
    suspend fun getMyVerificationStatus(
    ): Response<WalletVerificationResponse>

    @GET("/api/wallet-verifications/me")
    suspend fun getMyVerificationRequest(
    ): Response<List<WalletVerificationResponse>>
}
