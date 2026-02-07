package com.example.ibanking_kltn.ui.screens.transaction_history

import com.example.ibanking_kltn.dtos.definitions.AccountType
import com.example.ibanking_kltn.dtos.definitions.ServiceType
import com.example.ibanking_kltn.dtos.definitions.SortOption
import com.example.ibanking_kltn.dtos.definitions.TransactionStatus
import com.example.ibanking_kltn.ui.uistates.StateType
import java.time.LocalDate

data class TransactionHistoryUiState(
    val screenState: StateType = StateType.NONE,

    val isShowFilter: Boolean = false,

    val fromDate: LocalDate = LocalDate.now().minusMonths(1),
    val toDate: LocalDate = LocalDate.now(),
    val accountType: AccountType = AccountType.WALLET,
    val type: ServiceType? = null,
    val selectedStatus: TransactionStatus? = null,
    val selectedSort: SortOption = SortOption.NEWEST,
    val myWalletNumber: String = ""
)
