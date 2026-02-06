package com.example.ibanking_kltn.ui.screens.transfer

import com.example.ibanking_kltn.dtos.definitions.SavedReceiverInfo
import com.example.ibanking_kltn.dtos.responses.AllExpenseTypeResponse
import com.example.ibanking_kltn.dtos.responses.ExpenseType
import com.example.ibanking_kltn.ui.screens.confirm_transaction.ConfirmContent
import com.example.ibanking_kltn.ui.uistates.StateType

data class TransferUiState(
    val screenState: StateType = StateType.NONE,
    val confirmState: StateType = StateType.NONE,
    val isOtpShow: Boolean = false,

    val expenseType: ExpenseType? =null,
    val allExpenseTypeResponse: AllExpenseTypeResponse = AllExpenseTypeResponse(),


    val amount: Long = 0L,
    val description: String = "",
    val isSaveReceiver: Boolean = false,

    val toWalletNumber: String = "",
    val toMerchantName: String = "",
    val isVerified: Boolean = false,
    val savedReceivers: List<SavedReceiverInfo> = listOf(),

    val otp: String = "",

    val confirmContent: ConfirmContent? = null
){
    val isEnableContinue: Boolean
        get() = toWalletNumber.isNotBlank()
                && toMerchantName.isNotBlank()
                && amount > 0L
}

