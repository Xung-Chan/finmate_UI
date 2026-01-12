package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.PaymentAccount
import com.example.ibanking_kltn.data.dtos.ServiceType
import com.example.ibanking_kltn.data.dtos.responses.PrepareTransactionResponse

sealed class ConfirmContent{
    data class TRANSFER(
        val toWalletNumber: String ,
        val toMerchantName: String,
        val description: String = "",
        val expenseType: String? = null,
        ) : ConfirmContent()
    data class BILL_PAYMENT(
        val billCode:String,
        val toWalletNumber: String ,
        val toMerchantName: String,
        val description: String = "",
        ) : ConfirmContent()
    data class BILL_REPAYMENT(
        val billCode:String,
        val term : String,
        val dueDate: String,
        val totalDept: Long,
        val afterRepayDept: Long,
    ) : ConfirmContent()

}

data class ConfirmUiState(
    val screenState: StateType = StateType.NONE,
    val confirmState: StateType = StateType.NONE,
    val isOtpShow: Boolean = false,

    val availableAccount: List<PaymentAccount> = emptyList(),
    val accountType: PaymentAccount? = null,
    val balance: Long = 0L,

    val service: ServiceType? = null,
    val amount: Long = 0L,

    val confirmContent : ConfirmContent? = null,
//    val toMerchantName: String = "",
//    val toWalletNumber: String = "",
//    val billCode:String?="",
//    val term : String? = "",

    val otp: String = "",
    val prepareResponse: PrepareTransactionResponse? = null

    )
