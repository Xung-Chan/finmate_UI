package com.example.ibanking_kltn.ui.screens.bill.detail

import com.example.ibanking_kltn.dtos.responses.BillResponse
import com.example.ibanking_kltn.ui.uistates.StateType

data class BillDetailUiState(
    val screenState: StateType = StateType.NONE,

    val bill: BillResponse?=null,

    )
