package com.example.ibanking_kltn.ui.event

import com.example.ibanking_kltn.dtos.responses.BillingCycleResonse
import com.example.ibanking_kltn.ui.uistates.ConfirmContent
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