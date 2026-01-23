package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.responses.UserInfoResponse

data class MyProfileUiState(
    val screenState: StateType = StateType.NONE,
    val initState: StateType= StateType.NONE,

    val userInfo : UserInfoResponse?=null,
    val myWalletNumber: String?=null,
    val isVerified: Boolean =false,
    val toWalletNumber:String?=null,
)
