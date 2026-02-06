package com.example.ibanking_kltn.ui.screens.pay_later.billing_cycle

import com.example.ibanking_kltn.dtos.definitions.SortOption
import com.example.ibanking_kltn.dtos.responses.BillingCycleResonse
import com.example.ibanking_kltn.ui.uistates.StateType

data class BillingCycleUiState(
    val screenState: StateType = StateType.NONE,
    val sortBy : SortOption= SortOption.NEWEST,

    val selectedBillingCycle: BillingCycleResonse? = null
)
