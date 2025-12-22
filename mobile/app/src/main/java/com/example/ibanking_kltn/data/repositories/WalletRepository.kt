package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.WalletApi
import com.example.ibanking_kltn.data.dtos.responses.WalletResponse
import com.example.ibanking_soa.data.utils.ApiResult
import jakarta.inject.Inject

class WalletRepository @Inject constructor(
    private val walletApi: WalletApi

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


}