package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.data.dtos.responses.PayLaterResponse
import retrofit2.Response
import retrofit2.http.GET


interface PayLaterApi {
    @GET("/api/pay-later/me")
    suspend fun getMyPaylater(
    ): Response<PayLaterResponse>

//
//    @GET("/api/wallets/")
//    suspend fun getInfoByWalletNumber(
//        @Query(value = "walletNumber") walletNumber : String
//    ): Response<WalletResponse>

}