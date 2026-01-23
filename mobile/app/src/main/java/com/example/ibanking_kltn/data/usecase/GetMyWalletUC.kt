package com.example.ibanking_kltn.data.usecase

import com.example.ibanking_kltn.data.dtos.responses.WalletResponse
import com.example.ibanking_kltn.data.repositories.WalletRepository
import com.example.ibanking_kltn.data.session.UserSession
import com.example.ibanking_kltn.ui.uistates.UserSessionUS
import com.example.ibanking_soa.data.utils.ApiResult
import jakarta.inject.Inject

class GetMyWalletUC @Inject constructor(
    private val walletRepository: WalletRepository,
    private val userSession: UserSession
) {

    suspend operator fun invoke(
    ): ApiResult<WalletResponse> {
        var message = ""
        repeat(3) {
            val apiResult = walletRepository.getMyWalletInfor()
            when (apiResult) {
                is ApiResult.Success -> {
                    userSession.setUser(
                        user = userSession.user.value?.copy(
                            wallet = apiResult.data
                        ) ?: UserSessionUS(
                            wallet = apiResult.data
                        )
                    )
                    return apiResult
                }

                is ApiResult.Error -> {
                    message = apiResult.message
                }
            }

        }
        return ApiResult.Error(message = message)
    }
}