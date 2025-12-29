package com.example.ibanking_kltn.ui.uistates

import android.net.Uri
import com.example.ibanking_kltn.data.dtos.responses.UserInfoResponse

data class MyProfileUiState(
    val screenState: StateType = StateType.NONE,
    val userInfo : UserInfoResponse?=null,
    val selectedImageUri: Uri?=null,
)
