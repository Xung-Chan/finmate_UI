package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.ServiceType
import com.example.ibanking_kltn.data.dtos.responses.PrepareTransactionResponse

data class ConfirmUiState(
    val screenState: StateType = StateType.NONE,
    val confirmState: StateType = StateType.NONE,
    val isOtpShow: Boolean = false,

    val service: ServiceType? = null,
    val expenseType: String? = null,
    val toMerchantName: String = "",
    val accountType: String = "",
    val amount: Long = 0L,
    val description: String = "",
    val toWalletNumber: String = "",
    val billCode:String?="",

    val otp: String = "",
    val prepareResponse: PrepareTransactionResponse? = null,

    )
