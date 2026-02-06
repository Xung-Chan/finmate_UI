package com.example.ibanking_kltn.ui.screens.pay_later.billing_cycle

import com.example.ibanking_kltn.dtos.responses.BillingCycleResonse
import com.example.ibanking_kltn.ui.screens.confirm_transaction.ConfirmContent
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class BillingCycleEvent {
    object ChangeSortingOption : BillingCycleEvent()
    data class SelectBillingCycle(val billingCycle: BillingCycleResonse?) : BillingCycleEvent()
    data class RepayBill(
        val billingCycle: BillingCycleResonse,
        val payAmount: Long,
        val totalDept: Long
    ) : BillingCycleEvent()
}

sealed class BillingCycleEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : BillingCycleEffect()
    data class NavigateToConfirmScreen(val confirmContent: ConfirmContent.BILL_REPAYMENT) :
        BillingCycleEffect()
}