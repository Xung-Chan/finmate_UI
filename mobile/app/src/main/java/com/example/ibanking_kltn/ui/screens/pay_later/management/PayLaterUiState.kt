package com.example.ibanking_kltn.ui.screens.pay_later.management

import com.example.ibanking_kltn.dtos.responses.PayLaterResponse
import com.example.ibanking_kltn.dtos.responses.UserInfoResponse
import com.example.ibanking_kltn.ui.uistates.StateType

data class PayLaterUiState(
    val screenState: StateType = StateType.NONE,
    val initialedUserInfo: Boolean = false,
    val initialedPayLaterInfo: Boolean = false,

    val userInfo : UserInfoResponse?=null,
    val payLaterInfo: PayLaterResponse?=null
)
