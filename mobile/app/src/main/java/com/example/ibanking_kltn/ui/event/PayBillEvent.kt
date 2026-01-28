package com.example.ibanking_kltn.ui.event

import com.example.ibanking_kltn.ui.uistates.ConfirmContent
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class PayBillEvent {
    data class ChangeBillCode(val billCode: String) : PayBillEvent()
    object CheckingBill: PayBillEvent()
    object ConfirmPayBill: PayBillEvent()

}

sealed class PayBillEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : PayBillEffect()
    data class NavigateToConfirmScreen(val confirmContent: ConfirmContent.BILL_PAYMENT) : PayBillEffect()
}