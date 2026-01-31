package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.dtos.responses.UserInfoResponse
import com.example.ibanking_kltn.dtos.responses.WalletResponse

data class UserSessionUS(
    val wallet: WalletResponse? = null,
    val profile: UserInfoResponse? = null,
    val fcmToken : String? = null
)