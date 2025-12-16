package com.example.ibanking_kltn.ui.uistates

data class BillHistoryUiState(
    val screenState: StateType = StateType.NONE,

    val isShowFilter: Boolean = false,
    val selectedStatus: String = "",
    val selectedSort: String = "",

    val filterCount:Int=0,

)
