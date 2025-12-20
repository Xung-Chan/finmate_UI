package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.AccountType
import com.example.ibanking_kltn.data.dtos.ServiceType
import com.example.ibanking_kltn.data.dtos.SortOption
import com.example.ibanking_kltn.data.dtos.TransactionStatus
import java.time.LocalDate

data class TransactionHistoryUiState(
    val screenState: StateType = StateType.NONE,

    val isShowFilter: Boolean = false,

    val fromDate: String = LocalDate.now().minusMonths(1).toString(),
    val toDate: String = LocalDate.now().toString(),
    val accountType: AccountType? = null,
    val type: ServiceType? = null,
    val selectedStatus: TransactionStatus? = null,
    val selectedSort: SortOption = SortOption.NEWEST
)
