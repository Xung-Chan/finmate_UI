package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.WalletApi
import com.example.ibanking_kltn.data.dtos.responses.WalletResponse
import com.example.ibanking_kltn.ui.exception.safeApiCall
import com.example.ibanking_soa.data.utils.ApiResult
import jakarta.inject.Inject

class WalletRepository @Inject constructor(
    private val walletApi: WalletApi

) {
    suspend fun getMyWalletInfor(): ApiResult<WalletResponse> {
        return safeApiCall(
            apiCall = { walletApi.getMyWallet() }
        )
    }


    suspend fun getInfoByWalletNumber(walletNumber: String): ApiResult<WalletResponse> {
        return safeApiCall(
            apiCall = { walletApi.getInfoByWalletNumber(walletNumber = walletNumber) }
        )
    }


}