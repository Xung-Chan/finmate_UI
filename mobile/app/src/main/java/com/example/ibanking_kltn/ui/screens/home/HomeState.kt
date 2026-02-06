package com.example.ibanking_kltn.ui.screens.home

import com.example.ibanking_kltn.dtos.definitions.ServiceItem
import com.example.ibanking_kltn.dtos.responses.UserInfoResponse
import com.example.ibanking_kltn.dtos.responses.WalletResponse
import com.example.ibanking_kltn.ui.uistates.StateType

data class HomeUiState(
    val state: StateType = StateType.NONE,
    val initState: StateType = StateType.NONE,

    val myWallet: WalletResponse? = null,
    val myProfile: UserInfoResponse? = null,
    val favoriteServices: List<ServiceItem> = emptyList(),
    val recentServices: List<ServiceItem> = emptyList(),
    val isBalanceShow: Boolean = false,
)

