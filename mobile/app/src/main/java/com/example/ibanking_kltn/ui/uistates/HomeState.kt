package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.ServiceItem
import com.example.ibanking_kltn.data.dtos.responses.UserInfoResponse
import com.example.ibanking_kltn.data.dtos.responses.WalletResponse

data class HomeUiState(
    val state: StateType = StateType.NONE,
    val initState: StateType = StateType.NONE,

    val myWallet: WalletResponse? = null,
    val myProfile: UserInfoResponse? = null,
    val favoriteServices: List<ServiceItem> = emptyList(),
    val recentServices: List<ServiceItem> = emptyList(),
    val isBalanceShow: Boolean = false,
)

