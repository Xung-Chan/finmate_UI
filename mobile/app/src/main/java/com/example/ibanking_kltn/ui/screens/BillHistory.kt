package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.data.dtos.BillStatus
import com.example.ibanking_kltn.data.dtos.SortOption
import com.example.ibanking_kltn.data.dtos.responses.BillResponse
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue3
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray3
import com.example.ibanking_kltn.ui.theme.Green1
import com.example.ibanking_kltn.ui.theme.Orange1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.BillHistoryUiState
import com.example.ibanking_kltn.utils.BillFilterDialog
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.formatterDateString
import com.example.ibanking_kltn.utils.formatterVND
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillHistoryScreen(
    uiState: BillHistoryUiState,
    bills: LazyPagingItems<BillResponse>,
    onResetAll: () -> Unit,
    onApply: (selectedStatus: BillStatus?, selectedSort: SortOption) -> Unit,
    onErrorLoading: (String) -> Unit,
    onClickFilter: () -> Unit,
    onViewDetail: (BillResponse) -> Unit,
    onBackClick: () -> Unit,
) {
    val refreshState = rememberPullToRefreshState()
    var selectedStatus by remember {
        mutableStateOf<BillStatus?>(null)
    }
    var selectedSort by remember {
        mutableStateOf(SortOption.NEWEST)
    }
    LoadingScaffold(
        isLoading = false,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Lịch sử hóa đơn")
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
                            IconButton(
                                onClick = {
                                    onClickFilter()
                                },
                            ) {
                                Box(
                                    contentAlignment = Alignment.TopEnd,
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.filter),
                                        contentDescription = null,
                                        modifier = Modifier.size(40.dp)
                                    )

                                }

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
                        isRefreshing = bills.loadState.refresh is LoadState.Loading,
                        state = refreshState,
                        onRefresh = {
                            bills.refresh()
                        },
                        indicator = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.TopCenter
                            ) {

                                PullToRefreshDefaults.Indicator(
                                    state = refreshState,
                                    isRefreshing = bills.loadState.refresh is LoadState.Loading,
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
                            if (bills.itemCount == 0) {
                                item {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "Không có hóa đơn nào",
                                            style = AppTypography.bodyMedium,
                                            color = Gray1,
                                        )
                                    }
                                }
                            }
                            items(
                                count = bills.itemCount,
                            ) { item ->
                                val bill = bills[item]
                                if (bill == null) {
                                    return@items
                                }
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
                                        .clickable {
                                            onViewDetail(bill)
                                        }
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
                                                text = bill.qrIdentifier,
                                                style = AppTypography.bodyMedium,
                                                color = Black1,

                                                )
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Text(
                                                text = formatterDateString( LocalDateTime.parse(bill.dueDate).toLocalDate()),
                                                style = AppTypography.bodySmall,
                                                color = Gray1,
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
                                                text = "Trạng thái",
                                                style = AppTypography.bodySmall,
                                                color = Gray1,

                                                )
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Text(
                                                text =bill.billStatus.status,
                                                style = AppTypography.bodySmall,
                                                color = when (bill.billStatus) {
                                                    BillStatus.ACTIVE -> Orange1
                                                    BillStatus.PAID -> Green1
                                                    else -> Gray1
                                                },
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
                                                text = "Số tiền",
                                                style = AppTypography.bodySmall,
                                                color = Gray1,

                                                )
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Text(
                                                text = "${formatterVND(bill.amount)} VND",
                                                style = AppTypography.bodySmall,
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
                                                text = "Mô tả",
                                                style = AppTypography.bodySmall,
                                                color = Gray1,

                                                )
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Text(
                                                text = bill.description,
                                                style = AppTypography.bodySmall,
                                                color = Black1,
                                            )
                                        }
                                    }
                                }

                            }
                            when (val state = bills.loadState.append) {
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
            if (uiState.isShowFilter) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Gray3.copy(alpha = 0.5f),
                        )
                        .padding(20.dp)
                        .pointerInput(Unit) {}) {
                    BillFilterDialog(
                        selectedStatus = selectedStatus,
                        selectedSort = selectedSort,
                        onSelectStatus = {
                            selectedStatus =
                                BillStatus.entries.first { billStatus -> billStatus.status == it }
                        },
                        onSelectSort = {
                            selectedSort =
                                SortOption.entries.first { sortOption -> sortOption.sortBy == it }
                        },
                        onResetStatusFilter = {
                            selectedStatus = null
                        },
                        onResetSortFilter = {
                            selectedSort = SortOption.NEWEST
                        },
                        onResetAll = {
                            selectedStatus = null
                            selectedSort = SortOption.NEWEST
                            onResetAll()
                        },
                        onApply = {
                            onApply(
                                selectedStatus,
                                selectedSort
                            )
                        },
                        onDismiss = {
                            selectedStatus = uiState.selectedStatus
                            selectedSort = uiState.selectedSort
                            onClickFilter()
                        })
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
//fun BillHistoryPreview() {
//    BillHistoryScreen(
//        uiState = BillHistoryUiState(
//            isShowFilter = false, filterCount = 2
//        ),
//        onSelectStatus = {},
//        onSelectSort = {},
//        onResetStatusFilter = {},
//        onResetSortFilter = {},
//        onResetAll = {},
//        onApply = {},
//        bills =
//    )
//}
