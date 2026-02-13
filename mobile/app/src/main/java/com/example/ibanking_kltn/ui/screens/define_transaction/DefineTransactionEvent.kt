package com.example.ibanking_kltn.ui.screens.define_transaction

import android.net.Uri
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState

sealed class DefineTransactionEvent {
    data class UpdateTransactionId(val transactionId: String) : DefineTransactionEvent()
    data class UpdateDestinationAccountNumber(val accountNumber: String) : DefineTransactionEvent()
    data class UpdateDestinationAccountName(val accountName: String) : DefineTransactionEvent()
    data class UpdateAmount(val amount: String) : DefineTransactionEvent()
    data class UpdateDescription(val description: String) : DefineTransactionEvent()
    data class UpdateDateTime(val dateTime: String) : DefineTransactionEvent()

    data class SelectImage(val uri: Uri) : DefineTransactionEvent()
    object ClearImage : DefineTransactionEvent()

    object SubmitTransaction : DefineTransactionEvent()
    object ClearForm : DefineTransactionEvent()
}

sealed class DefineTransactionEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : DefineTransactionEffect()
    object DefineSuccess : DefineTransactionEffect()
}