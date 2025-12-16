package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.responses.BillResponse

data class BillDetailUiState(
    val screenState: StateType = StateType.NONE,

    val bill: BillResponse?=null,

)
