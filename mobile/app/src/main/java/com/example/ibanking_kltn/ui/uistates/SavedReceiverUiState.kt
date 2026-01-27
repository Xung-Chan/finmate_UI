package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.SavedReceiverInfo

data class SavedReceiverUiState(
    val screenState: StateType = StateType.NONE,
    val memorableName: String = "",
    val toWalletNumber: String = "",
    val toMerchantName: String = "",
    val keyword: String = "",
    val savedReceivers: List<SavedReceiverInfo> = emptyList(),
    val selectedSavedReceiver : SavedReceiverInfo? = null
)
