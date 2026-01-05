package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.SortOption
import com.example.ibanking_kltn.data.dtos.responses.BillingCycleResonse

data class BillingCycleUiState(
    val screenState: StateType = StateType.NONE,
    val sortBy : SortOption= SortOption.NEWEST,

    val selectedBillingCycle: BillingCycleResonse? = null
)
