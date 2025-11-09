package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.requests.PrepareTransferRequest
import com.example.ibanking_kltn.data.dtos.responses.PrepareTransactionResponse

data class ConfirmUiState(
    val screenState: StateType = StateType.NONE,
    val confirmState: StateType= StateType.NONE,
    val isOtpShow: Boolean = false,

    val service :String="",
    val prepareTransactionRequest: PrepareTransferRequest?=null,
    val expenseType: String="",
    val toMerchantName: String="",

    val otp: String="",
    val prepareTransactionResponse: PrepareTransactionResponse? = null,

    )
