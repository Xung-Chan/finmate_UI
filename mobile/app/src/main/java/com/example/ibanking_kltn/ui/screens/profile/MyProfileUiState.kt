package com.example.ibanking_kltn.ui.screens.profile

import com.example.ibanking_kltn.dtos.responses.UserInfoResponse
import com.example.ibanking_kltn.ui.uistates.StateType

data class MyProfileUiState(
    val screenState: StateType = StateType.NONE,
    val initState: StateType = StateType.NONE,

    val userInfo : UserInfoResponse?=null,
    val myWalletNumber: String?=null,
    val isVerified: Boolean =false,
    val toWalletNumber:String?=null,
)
