package com.example.ibanking_kltn.ui.screens.pay_later.billing_cycle


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.dtos.definitions.BillingCycleStatus
import com.example.ibanking_kltn.dtos.definitions.SortOption
import com.example.ibanking_kltn.dtos.responses.BillingCycleResonse
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Blue3
import com.example.ibanking_kltn.ui.theme.Blue5
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray3
import com.example.ibanking_kltn.ui.theme.Green1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.DashedDivider
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.customClick
import com.example.ibanking_kltn.utils.formatterDateString
import com.example.ibanking_kltn.utils.formatterVND
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingCycleScreen(
    uiState: BillingCycleUiState,
    billingCycles: LazyPagingItems<BillingCycleResonse>,
    onBackClick: () -> Unit,
    onErrorLoading: (String) -> Unit,
    onEvent:(BillingCycleEvent)->Unit
) {
    val refreshState = rememberPullToRefreshState()
    LoadingScaffold(
        isLoading = uiState.screenState is StateType.LOADING,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Thanh toán nợ")
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                onBackClick()
                            }) {
                                Icon(
                                    Icons.Default.ArrowBackIosNew,
                                    contentDescription = null,
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        },

                        actions = {
                            Row(
                                modifier = Modifier
                                    .customClick {
                                       onEvent(
                                             BillingCycleEvent.ChangeSortingOption
                                       )
                                    }
                                    .padding(5.dp)
                            ) {
                                Icon(
                                    painter = painterResource(if (uiState.sortBy == SortOption.NEWEST) R.drawable.sort_time_desc else R.drawable.sort_time_asc),
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            titleContentColor = Black1, containerColor = White3
                        ),
                    )
                },

                modifier = Modifier.systemBarsPadding(), containerColor = White3
            ) { paddingValues ->
                Column(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    PullToRefreshBox(
                        isRefreshing = billingCycles.loadState.refresh is LoadState.Loading,
                        state = refreshState,
                        onRefresh = {
                            billingCycles.refresh()
                        },
                        indicator = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.TopCenter
                            ) {

                                PullToRefreshDefaults.Indicator(
                                    state = refreshState,
                                    isRefreshing = billingCycles.loadState.refresh is LoadState.Loading,
                                    containerColor = White1,
                                    color = Blue3,
                                )
                            }

                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)

                    ) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,

                            ) {

                            if (
                                billingCycles.itemCount == 0
                            ) {
                                item {
                                    Text(
                                        text = "Chưa có chu kỳ thanh toán nào.",
                                        style = AppTypography.bodySmall,
                                        color = Gray1,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            items(
                                count = billingCycles.itemCount,
                            ) { item ->
                                val billingCycle = billingCycles[item]
                                if (billingCycle == null) {
                                    return@items
                                }

                                val totalDebt =
                                    (billingCycle.totalSpent + billingCycle.totalInterest - billingCycle.paidPrincipal - billingCycle.paidInterest).toLong()
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .shadow(
                                            elevation = 30.dp,
                                            shape = RoundedCornerShape(20.dp),
                                            ambientColor = Black1.copy(alpha = 0.25f),
                                            spotColor = Black1.copy(alpha = 0.25f)
                                        )

                                        .background(
                                            color = White1, shape = RoundedCornerShape(20.dp)
                                        )
                                        .padding(20.dp)

                                ) {

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${
                                                    formatterDateString(
                                                        LocalDate.parse(
                                                            billingCycle.startDate
                                                        )
                                                    )
                                                } - ${
                                                    formatterDateString(
                                                        LocalDate.parse(
                                                            billingCycle.endDate
                                                        )
                                                    )
                                                }",
                                                style = AppTypography.bodyMedium,
                                                color = Black1,
                                            )
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            TextButton(
                                                onClick = {
                                                    onEvent(BillingCycleEvent.SelectBillingCycle(billingCycle))
                                                }
                                            ) {
                                                Text(
                                                    text = "Chi tiết",
                                                    style = AppTypography.bodyMedium,
                                                    color = Blue1,
                                                )
                                            }
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Hạn thanh toán",
                                                style = AppTypography.bodyMedium,
                                                color = Gray1,

                                                )
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Text(
                                                text = formatterDateString(
                                                    LocalDate.parse(
                                                        billingCycle.dueDate
                                                    )
                                                ),
                                                style = AppTypography.bodyMedium,
                                                color = Black1,
                                            )
                                        }
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Tổng nợ",
                                                style = AppTypography.bodyMedium,
                                                color = Gray1,

                                                )
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Text(
                                                text = formatterVND(totalDebt),
                                                style = AppTypography.bodyLarge,
                                                color = Black1,
                                            )
                                        }
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        DashedDivider(
                                            color = Gray1,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )
                                    }
                                    if (billingCycle.status != BillingCycleStatus.PAID) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .border(
                                                        width = 1.dp,
                                                        color = Blue5,
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                    .customClick(
                                                        shape = RoundedCornerShape(12.dp)
                                                    ) {
                                                        onEvent(
                                                            BillingCycleEvent.RepayBill(
                                                                billingCycle,
                                                                totalDebt,
                                                                totalDebt)
                                                        )
                                                    }
                                                    .padding(vertical = 5.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "Trả toàn bộ",
                                                    style = AppTypography.bodySmall,
                                                    color = Blue1,
                                                )
                                                Text(
                                                    text = formatterVND(totalDebt),
                                                    style = AppTypography.bodyMedium.copy(
                                                        fontWeight = FontWeight.SemiBold
                                                    ),
                                                    color = Blue1,
                                                )

                                            }
                                            Column(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .border(
                                                        width = 1.dp,
                                                        color = Blue5,
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                    .customClick(
                                                        shape = RoundedCornerShape(12.dp)
                                                    ) {
                                                        onEvent(
                                                            BillingCycleEvent.RepayBill(
                                                                billingCycle,
                                                                billingCycle.minimumPayment.toLong(),
                                                                totalDebt)
                                                        )
                                                    }
                                                    .padding(vertical = 5.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "Trả tối thiểu",
                                                    style = AppTypography.bodySmall,
                                                    color = Blue1,
                                                )
                                                Text(
                                                    text = formatterVND(billingCycle.minimumPayment.toLong()),
                                                    style = AppTypography.bodyMedium.copy(
                                                        fontWeight = FontWeight.SemiBold
                                                    ),
                                                    color = Blue1,
                                                )

                                            }
                                        }
                                    } else {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .border(
                                                    width = 1.dp,
                                                    color = Green1,
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .padding(
                                                    vertical = 10.dp
                                                ),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Đã trả đủ",
                                                style = AppTypography.bodyMedium,
                                                color = Green1,
                                            )
                                        }
                                    }

                                }

                            }
                            when (val state = billingCycles.loadState.append) {
                                is LoadState.NotLoading -> Unit
                                is LoadState.Loading -> {
                                    item {
                                        CircularProgressIndicator(
                                            color = Blue3, modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }

                                is LoadState.Error -> {
                                    //                                onErrorLoading("Tải dữ liệu không thành công. Vui lòng thử lại sau.")
                                    onErrorLoading(
                                        state.error.message
                                            ?: "Tải dữ liệu không thành công. Vui lòng thử lại sau."
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        uiState.selectedBillingCycle?.let {
            val totalDebt =
                (uiState.selectedBillingCycle.totalSpent + uiState.selectedBillingCycle.totalInterest - uiState.selectedBillingCycle.paidPrincipal - uiState.selectedBillingCycle.paidInterest).toLong()

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Gray3.copy(alpha = 0.5f),
                    )
                    .padding(20.dp)
                    .pointerInput(Unit) {}) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = White1, shape = RoundedCornerShape(12.dp)
                        )
                        .padding(5.dp)
                ) {

                    Box(
                        contentAlignment = Alignment.TopEnd,
                    ) {

                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = White1, shape = RoundedCornerShape(20.dp)
                                )
                                .padding(20.dp)

                        ) {

                            Row(
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${
                                            formatterDateString(
                                                LocalDate.parse(
                                                    uiState.selectedBillingCycle.startDate
                                                )
                                            )
                                        } - ${
                                            formatterDateString(
                                                LocalDate.parse(
                                                    uiState.selectedBillingCycle.endDate
                                                )
                                            )
                                        }",
                                        style = AppTypography.bodyMedium,
                                        color = Black1,
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .customClick(
                                            shape = RoundedCornerShape(15.dp)
                                        ) {
                                            onEvent(BillingCycleEvent.SelectBillingCycle(null))
                                        }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = "Đóng",
                                        style = AppTypography.bodyMedium,
                                        color = Blue1,
                                    )

                                }

                            }


                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Hạn thanh toán",
                                        style = AppTypography.bodyMedium,
                                        color = Gray1,

                                        )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = formatterDateString(
                                            LocalDate.parse(
                                                uiState.selectedBillingCycle.dueDate
                                            )
                                        ),
                                        style = AppTypography.bodyMedium,
                                        color = Black1,
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Tổng nợ tiêu dùng",
                                        style = AppTypography.bodyMedium,
                                        color = Gray1,

                                        )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = formatterVND(it.totalSpent.toLong()) + " VNĐ",
                                        style = AppTypography.bodyLarge,
                                        color = Black1,
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Tổng lãi",
                                        style = AppTypography.bodyMedium,
                                        color = Gray1,

                                        )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = formatterVND(it.totalInterest.toLong()) + " VNĐ",
                                        style = AppTypography.bodyLarge,
                                        color = Black1,
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Đã trả",
                                        style = AppTypography.bodyMedium,
                                        color = Gray1,

                                        )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = formatterVND((it.paidInterest + it.paidPrincipal).toLong()) + " VNĐ",
                                        style = AppTypography.bodyLarge,
                                        color = Black1,
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Lãi suất",
                                        style = AppTypography.bodyMedium,
                                        color = Gray1,

                                        )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "${it.lateInterestRate}%/tháng",
                                        style = AppTypography.bodyLarge,
                                        color = Black1,
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                DashedDivider(
                                    color = Gray1,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Tổng nợ",
                                        style = AppTypography.bodyMedium,
                                        color = Gray1,

                                        )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = formatterVND(totalDebt),
                                        style = AppTypography.bodyLarge,
                                        color = Black1,
                                    )
                                }
                            }
                            if (it.status != BillingCycleStatus.PAID) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .border(
                                                width = 1.dp,
                                                color = Blue5,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .customClick(
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                onEvent(
                                                    BillingCycleEvent.RepayBill(
                                                        uiState.selectedBillingCycle,
                                                        totalDebt,
                                                        totalDebt)
                                                )
                                            }
                                            .padding(vertical = 5.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Trả toàn bộ",
                                            style = AppTypography.bodySmall,
                                            color = Blue1,
                                        )
                                        Text(
                                            text = formatterVND(totalDebt),
                                            style = AppTypography.bodyMedium.copy(
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            color = Blue1,
                                        )

                                    }
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .border(
                                                width = 1.dp,
                                                color = Blue5,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .customClick(
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                onEvent(
                                                    BillingCycleEvent.RepayBill(
                                                        uiState.selectedBillingCycle,
                                                        uiState.selectedBillingCycle.minimumPayment.toLong(),
                                                        totalDebt)
                                                )
                                            }
                                            .padding(vertical = 5.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Trả tối thiểu",
                                            style = AppTypography.bodySmall,
                                            color = Blue1,
                                        )
                                        Text(
                                            text = formatterVND(uiState.selectedBillingCycle.minimumPayment.toLong()),
                                            style = AppTypography.bodyMedium.copy(
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            color = Blue1,
                                        )

                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            width = 1.dp,
                                            color = Green1,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(
                                            vertical = 10.dp
                                        ),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Đã trả đủ",
                                        style = AppTypography.bodyMedium,
                                        color = Green1,
                                    )
                                }
                            }
                        }
                    }


                }

            }
        }
    }
}

//@Preview(
//    showBackground = true, showSystemUi = true
//
//)
//@Composable
//fun BillingCyclePreview() {
//
//    val pagingData = PagingData.from(
//        listOf(
//            BillingCycleResonse(
//                code = "BC202401",
//                startDate = "2024-01-01",
//                endDate = "2024-01-31",
//                dueDate = "2024-02-15",
//                totalSpent = 5_000_000.0,
//                paidPrincipal = 2_000_000.0,
//                minimumPayment = 500_000.0,
//                totalInterest = 200_000.0,
//                paidInterest = 100_000.0,
//                lateInterestRate = 0.05,
//                penaltyFee = null,
//                penaltyApplied = false,
//                status = com.example.ibanking_kltn.data.dtos.BillingCycleStatus.PAID
//            ),
//            BillingCycleResonse(
//                code = "BC202402",
//                startDate = "2024-02-01",
//                endDate = "2024-02-29",
//                dueDate = "2024-03-15",
//                totalSpent = 3_000_000.0,
//                paidPrincipal = 1_000_000.0,
//                minimumPayment = 300_000.0,
//                totalInterest = 150_000.0,
//                paidInterest = 50_000.0,
//                lateInterestRate = 0.05,
//                penaltyFee = 50_000.0,
//                penaltyApplied = true,
//                status = com.example.ibanking_kltn.data.dtos.BillingCycleStatus.OVERDUE
//            ),
//        )
//    )
//    BillingCycleScreen(
//        uiState = BillingCycleUiState(),
//        billingCycles = flowOf(pagingData).collectAsLazyPagingItems(),
//        onBackClick = {},
//        onErrorLoading = {},
//        onChangeSortOption = {},
//        onSelectBillingCycle = {},
//        onConfirmPayment = {
//
//        } as (BillingCycleResonse, Long, Long) -> Unit,
//
//        )
//}
