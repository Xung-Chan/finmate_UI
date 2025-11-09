package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.data.dtos.responses.WalletResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WalletApi {
    @GET("/api/wallets/me")
    suspend fun getMyWallet(
    ): Response<WalletResponse>


    @GET("/api/wallets/")
    suspend fun getInfoByWalletNumber(
        @Query(value = "walletNumber") walletNumber : String
    ): Response<WalletResponse>

}