package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.responses.AllExpenseTypeResponse
import com.example.ibanking_kltn.data.dtos.responses.PrepareTransactionResponse

data class TransferUiState(
    val screenState: StateType = StateType.NONE,
    val confirmState: StateType= StateType.NONE,
    val isOtpShow: Boolean = false,

    val balance: Long = 0L,
    val accountType: String = "",
    val expenseType: String = "",
    val allExpenseTypeResponse: AllExpenseTypeResponse= AllExpenseTypeResponse(),
    val availableAccount: List<String> = listOf(),

    val amount: Long = 0L,
    val description: String = "",

    val toWalletNumber: String = "",
    val toMerchantName: String = "",

    val otp: String="",
    val prepareTransactionResponse: PrepareTransactionResponse? = null,

    )
