package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.responses.WalletResponse

data class HomeUiState(
    val state: StateType = StateType.NONE,
    val myWallet: WalletResponse? = null,
    val isBalanceShow: Boolean = false,
)

