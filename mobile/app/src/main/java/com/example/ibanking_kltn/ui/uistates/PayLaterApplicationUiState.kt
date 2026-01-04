package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.PayLaterApplicationType

data class PayLaterApplicationUiState(
    val screenState: StateType = StateType.NONE,

    val applicationType: PayLaterApplicationType = PayLaterApplicationType.ACTIVATION,
    val requestLimit: Long = 10000000L,
    val reason: String? = null

)
