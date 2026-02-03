package com.example.ibanking_kltn.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.dtos.definitions.AppGraph
import com.example.ibanking_kltn.dtos.definitions.Screens
import com.example.ibanking_kltn.dtos.definitions.SpendingRecordType
import com.example.ibanking_kltn.dtos.responses.SpendingCategoryDetailResponse
import com.example.ibanking_kltn.dtos.responses.SpendingRecordResponse
import com.example.ibanking_kltn.dtos.responses.SpendingSnapshotDetailResponse
import com.example.ibanking_kltn.ui.screens.SpendingSnapshotDetail
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.uistates.SpendingDetailState
import com.example.ibanking_kltn.ui.uistates.SpendingDetailTab
import com.example.ibanking_kltn.ui.uistates.SpendingDetailUiState
import kotlinx.coroutines.flow.flowOf
import java.math.BigDecimal
import java.time.LocalDateTime

fun NavGraphBuilder.spendingGraph(
    navController: NavController,
    onShowSnackBar: (snakeBarUiState: SnackBarUiState) -> Unit,
) {
    this.navigation(
        startDestination = Screens.SpendingSnapshotDetail.name,
        route = AppGraph.SpendingGraph.name,
    ) {
        composable(route = Screens.SpendingSnapshotDetail.name){
            val pagingData = PagingData.from(
                listOf(
                    SpendingRecordResponse(
                        id = "rec_001",
                        snapshotId = "snap_20260202",
                        transactionId = "txn_889900",
                        amount = BigDecimal("150000.00"),
                        description = "Thanh toán tiền điện",
                        destinationAccountName = "EVN HCMC",
                        destinationAccountNumber = "19001001",
                        recordType = SpendingRecordType.EXPENSE,
                        categoryCode = "UTIL",
                        categoryName = "Tiện ích",
                        categoryIcon = R.drawable.airplane_service.toString(),
                        categoryTextColor = "#FFFFFF",
                        categoryBackgroundColor = "#FF9800",
                        occurredAt = LocalDateTime.now().toString()
                    )
                )
            )

            SpendingSnapshotDetail(
                uiState = SpendingDetailUiState(
                    screenState = SpendingDetailState.NONE,
                    spendingSnapshot = SpendingSnapshotDetailResponse(
                        id = "1",
                        snapshotName = "Chi tiêu tháng 6",
                        budgetAmount = BigDecimal(5000000),
                        usedAmount = BigDecimal(3500000),
                        monthlySpending = "8/2025",
                        spendingCategories = listOf(
                            SpendingCategoryDetailResponse(
                                categoryName = "Ăn uống",
                                categoryCode = "food",
                                categoryIcon = "",
                                textColor = "TODO()",
                                backgroundColor = "TODO()",
                                budgetAmount = BigDecimal(5000000),
                                usedAmount = BigDecimal(3500000),
                            )
                        ),
                    ),
                    selectedTab = SpendingDetailTab.HISTORY
                ),
                records = flowOf(
                    pagingData
                ).collectAsLazyPagingItems()

            )
        }
    }
}