package com.example.ibanking_kltn.ui.uistates

import android.net.Uri
import com.example.ibanking_kltn.data.dtos.responses.UserInfoResponse

data class MyProfileUiState(
    val screenState: StateType = StateType.NONE,
    val initialedUserInfo : Boolean = false,
    val initialVerification: Boolean = false,

    val userInfo : UserInfoResponse?=null,
    val isVerified: Boolean =false,
    val toWalletNumber:String?=null,
    val selectedImageUri: Uri?=null,
)
