package com.example.ibanking_kltn.ui.screens.pay_later.application.history

import com.example.ibanking_kltn.dtos.definitions.PayLaterApplicationStatus
import com.example.ibanking_kltn.dtos.definitions.PayLaterApplicationType
import com.example.ibanking_kltn.ui.uistates.StateType
import java.time.LocalDate

data class PayLaterApplicationHistoryUiState(
    val screenState: StateType = StateType.NONE,

    val fromDate: LocalDate = LocalDate.now().minusMonths(1),
    val toDate: LocalDate= LocalDate.now(),
    val selectedStatus: PayLaterApplicationStatus? = null,
    val selectedType: PayLaterApplicationType? = null,
)
