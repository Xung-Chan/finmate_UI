package com.example.ibanking_kltn.ui.uistates

import android.os.Parcelable
import com.example.ibanking_kltn.data.dtos.PaymentAccount
import com.example.ibanking_kltn.data.dtos.ServiceType
import com.example.ibanking_kltn.data.dtos.responses.PrepareTransactionResponse
import kotlinx.parcelize.Parcelize


@Parcelize
sealed class ConfirmContent(
    open val amount: Long,
    open val service: ServiceType,
    open val isVerified: Boolean,

    ) : Parcelable {
    @Parcelize
    data class TRANSFER(
        override val amount: Long,
        override val service: ServiceType,
        override val isVerified: Boolean,
        val toWalletNumber: String,
        val toMerchantName: String,
        val description: String = "",
        val expenseType: String? = null,
    ) : ConfirmContent(
        amount = amount,
        service = service,
        isVerified = isVerified
    )

    @Parcelize
    data class BILL_PAYMENT(
        override val amount: Long,
        override val service: ServiceType,
        override val isVerified: Boolean,
        val billCode: String,
        val toWalletNumber: String,
        val toMerchantName: String,
        val description: String = "",
    ) : ConfirmContent(
        amount = amount,
        service = service,
        isVerified = isVerified

    )

    @Parcelize
    data class BILL_REPAYMENT(
        override val amount: Long,
        override val service: ServiceType,
        override val isVerified: Boolean = false,
        val billCode: String,
        val term: String,
        val dueDate: String,
        val totalDept: Long,
        val afterRepayDept: Long,
    ) : ConfirmContent(
        amount = amount,
        service = service,
        isVerified = isVerified
    )

}

data class ConfirmUiState(
    val screenState: StateType = StateType.NONE,
    val confirmState: StateType = StateType.NONE,
    val isOtpShow: Boolean = false,

    val availableAccount: List<PaymentAccount> = emptyList(),
    val accountType: PaymentAccount? = null,
    val balance: Long = 0L,

//    val service: ServiceType? = null,

    val confirmContent: ConfirmContent? = null,
//    val toMerchantName: String = "",
//    val toWalletNumber: String = "",
//    val billCode:String?="",
//    val term : String? = "",

    val otp: String = "",
    val prepareResponse: PrepareTransactionResponse? = null

)
