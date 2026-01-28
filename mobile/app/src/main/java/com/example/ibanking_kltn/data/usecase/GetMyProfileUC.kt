package com.example.ibanking_kltn.data.usecase

import com.example.ibanking_kltn.data.dtos.responses.UserInfoResponse
import com.example.ibanking_kltn.data.repositories.AuthRepository
import com.example.ibanking_kltn.data.session.UserSession
import com.example.ibanking_kltn.ui.uistates.UserSessionUS
import com.example.ibanking_soa.data.utils.ApiResult
import jakarta.inject.Inject

class GetMyProfileUC @Inject constructor(
    private val authRepository: AuthRepository,
    private val userSession: UserSession
) {

    suspend operator fun invoke(
    ): ApiResult<UserInfoResponse> {
        var message = ""
        repeat(3) {
            val apiResult = authRepository.getMyProfile()
            when (apiResult) {
                is ApiResult.Success -> {
                    userSession.setUser(
                        user = userSession.user.value?.copy(
                            profile = apiResult.data
                        ) ?: UserSessionUS(
                            profile = apiResult.data
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