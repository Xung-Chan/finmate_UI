package com.example.ibanking_kltn.ui.uistates

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

    val otp: String = "",
)

data class TransactionResultUiState(
    val screenState: StateType = StateType.NONE,
    val service: String="",
    val amount: Long= 0L,
    val toMerchantName: String= "",
    val status: TransactionStatus= TransactionStatus.COMPLETED,
)
