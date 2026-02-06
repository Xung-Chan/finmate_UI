package com.example.ibanking_kltn.ui.screens.saved_receiver

import com.example.ibanking_kltn.dtos.definitions.SavedReceiverInfo
import com.example.ibanking_kltn.ui.uistates.StateType

data class SavedReceiverUiState(
    val screenState: StateType = StateType.NONE,
    val memorableName: String = "",
    val toWalletNumber: String = "",
    val toMerchantName: String = "",
    val keyword: String = "",
    val savedReceivers: List<SavedReceiverInfo> = emptyList(),
    val selectedSavedReceiver : SavedReceiverInfo? = null
)
