package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.dtos.definitions.PayLaterApplicationStatus
import com.example.ibanking_kltn.dtos.definitions.PayLaterApplicationType
import java.time.LocalDate

data class PayLaterApplicationHistoryUiState(
    val screenState: StateType = StateType.NONE,

    val fromDate: LocalDate = LocalDate.now().minusMonths(1),
    val toDate: LocalDate= LocalDate.now(),
    val selectedStatus: PayLaterApplicationStatus? = null,
    val selectedType: PayLaterApplicationType? = null,
)
