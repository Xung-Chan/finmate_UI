package com.example.ibanking_kltn.ui.screens.bill.pay_bill

import com.example.ibanking_kltn.ui.screens.confirm_transaction.ConfirmContent
import com.example.ibanking_kltn.ui.uistates.StateType


data class BillUiState(
    val screenState: StateType = StateType.NONE,
    val confirmState: StateType = StateType.NONE,
    val checkingState: StateType = StateType.NONE,


    val billCode: String = "",
    val toMerchantName: String = "",
    val toWalletNumber: String = "",
    val amount: Long = 0L,
    val description: String = "",
    val dueDate: String = "",

    val confirmContent: ConfirmContent.BILL_PAYMENT? = null
)
