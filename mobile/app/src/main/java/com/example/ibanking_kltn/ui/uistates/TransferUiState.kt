package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.SavedReceiverInfo
import com.example.ibanking_kltn.data.dtos.TransactionStatus
import com.example.ibanking_kltn.data.dtos.responses.AllExpenseTypeResponse

data class TransferUiState(
    val screenState: StateType = StateType.NONE,
    val confirmState: StateType = StateType.NONE,
    val isOtpShow: Boolean = false,

    val expenseType: String = "",
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

data class TransactionResultUiState(
    val screenState: StateType = StateType.NONE,
    val service: String = "",
    val amount: Long = 0L,
    val status: TransactionStatus = TransactionStatus.COMPLETED,
)
