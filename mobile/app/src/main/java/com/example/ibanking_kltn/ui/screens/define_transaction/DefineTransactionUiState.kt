package com.example.ibanking_kltn.ui.screens.define_transaction

import android.net.Uri
import com.example.ibanking_kltn.ui.uistates.StateType
import java.util.UUID

data class DefineTransactionUiState(
    val screenState: StateType = StateType.NONE,
    // Transaction fields
    val transactionId: String = UUID.randomUUID().toString(),
    val toAccountNumber: String = "",
    val toMerchantName: String = "",
    val amount: Long = 0L,
    val description: String = "",
    val transactionDateTime: String = "",

    // Image upload
    val selectedImageUri: Uri? = null,
    val isProcessingImage: Boolean = false,

    ) {
    val isEnableContinue: Boolean
        get() = toAccountNumber.isNotBlank()
                && toMerchantName.isNotBlank()
                && amount > 0L
}

