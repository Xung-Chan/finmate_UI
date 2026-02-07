package com.example.ibanking_kltn.ui.screens.saved_receiver

import com.example.ibanking_kltn.dtos.definitions.SavedReceiverInfo
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class SavedReceiverEvent {
    data class ChangeKeyword(val keyword: String) : SavedReceiverEvent()
    data class ChangeToWalletNumber(val walletNumber: String) : SavedReceiverEvent()
    data class ChangeMemorableName(val memorableName: String) : SavedReceiverEvent()
    data class DeleteSavedReceiver(val walletNumber: String): SavedReceiverEvent()
    data class SelectSavedReceiver(val savedReceiver: SavedReceiverInfo): SavedReceiverEvent()
    data class SaveReceiver(val savedReceiver: SavedReceiverInfo) : SavedReceiverEvent()
    object ClearAddDialog: SavedReceiverEvent()
    object Search: SavedReceiverEvent()
    object DoneWalletNumber: SavedReceiverEvent()
    object AddSavedReceiver: SavedReceiverEvent()
}

sealed class SavedReceiverEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : SavedReceiverEffect()
}