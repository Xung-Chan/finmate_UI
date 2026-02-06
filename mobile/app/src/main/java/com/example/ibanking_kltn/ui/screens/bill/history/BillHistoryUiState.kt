package com.example.ibanking_kltn.ui.screens.bill.history

import com.example.ibanking_kltn.dtos.definitions.SortOption
import com.example.ibanking_kltn.dtos.definitions.BillStatus
import com.example.ibanking_kltn.ui.uistates.StateType

data class BillHistoryUiState(
    val screenState: StateType = StateType.NONE,

    val isShowFilter: Boolean = false,

    val selectedStatus: BillStatus? = null,
    val selectedSort: SortOption = SortOption.NEWEST,


    )
