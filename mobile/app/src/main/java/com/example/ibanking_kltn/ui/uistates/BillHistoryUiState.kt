package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.SortOption
import com.example.ibanking_kltn.data.dtos.BillStatus

data class BillHistoryUiState(
    val screenState: StateType = StateType.NONE,

    val isShowFilter: Boolean = false,

    val selectedStatus: BillStatus? = null,
    val selectedSort: SortOption = SortOption.NEWEST,


)
