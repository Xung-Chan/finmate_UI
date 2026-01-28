package com.example.ibanking_kltn.ui.event

import com.example.ibanking_kltn.data.dtos.SavedReceiverInfo
import com.example.ibanking_kltn.ui.uistates.ConfirmContent
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class TransferEvent {
    object DoneWalletNumber : TransferEvent()
    object ChangeSaveReceiver : TransferEvent()
    object ConfirmTransfer : TransferEvent()
    data class ExpenseTypeChange(val expenseType: String) : TransferEvent()
    data class ToWalletNumberChange(val walletNumber: String) : TransferEvent()
    data class AmountChange(val amount: String) : TransferEvent()
    data class ContentChange(val content: String) : TransferEvent()
    data class SelectSavedReceiver(val savedReceiver: SavedReceiverInfo) : TransferEvent()
    data class SaveReceiver(val savedReceiver: SavedReceiverInfo) : TransferEvent()
}

sealed class TransferEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : TransferEffect()
    data class NavigateToConfirmScreen(val confirmContent: ConfirmContent.TRANSFER) :
        TransferEffect()
}