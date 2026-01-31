package com.example.ibanking_kltn.ui.event

import com.example.ibanking_kltn.dtos.definitions.PaymentAccount
import com.example.ibanking_kltn.ui.uistates.ConfirmContent
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class ConfirmEvent {
    data class Init(val confirmContent: ConfirmContent) : ConfirmEvent()
    data class OtpChange(val otp: String) : ConfirmEvent()
    data class SelectAccountType(val account: PaymentAccount) : ConfirmEvent()
    object OtpDismiss : ConfirmEvent()
    object ConfirmClick : ConfirmEvent()

}

sealed class ConfirmEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : ConfirmEffect()
    data class PaymentSuccess(val transactionId:String) : ConfirmEffect()
}