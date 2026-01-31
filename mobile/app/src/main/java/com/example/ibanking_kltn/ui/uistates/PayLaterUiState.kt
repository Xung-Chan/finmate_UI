package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.dtos.responses.PayLaterResponse
import com.example.ibanking_kltn.dtos.responses.UserInfoResponse

data class PayLaterUiState(
    val screenState: StateType = StateType.NONE,
    val initialedUserInfo: Boolean = false,
    val initialedPayLaterInfo: Boolean = false,

    val userInfo : UserInfoResponse?=null,
    val payLaterInfo: PayLaterResponse?=null
)
