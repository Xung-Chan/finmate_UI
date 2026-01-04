package com.example.ibanking_kltn.ui.screens


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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.data.dtos.PayLaterApplicationStatus
import com.example.ibanking_kltn.data.dtos.PayLaterApplicationType
import com.example.ibanking_kltn.data.dtos.responses.PayLaterApplicationResponse
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Blue3
import com.example.ibanking_kltn.ui.theme.Blue5
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray3
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.PayLaterApplicationHistoryUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.DashedDivider
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.PayLaterApplicationHistoryFilterDialog
import com.example.ibanking_kltn.utils.customClick
import com.example.ibanking_kltn.utils.formatterDateString
import com.example.ibanking_kltn.utils.formatterVND
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingCycleScreen(
    uiState: PayLaterApplicationHistoryUiState,
    applications: LazyPagingItems<PayLaterApplicationResponse>,
    onBackClick: () -> Unit,
    onErrorLoading: (String) -> Unit,
    onApply: (
        selectedStatus: PayLaterApplicationStatus?,
        selectedType: PayLaterApplicationType?,
        fromDate: LocalDate,
        toDate: LocalDate,
    ) -> Unit

) {
    val refreshState = rememberPullToRefreshState()
    var isShowFilter by remember {
        mutableStateOf(false)
    }
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
                            IconButton(
                                onClick = {
                                    isShowFilter = !isShowFilter
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
                        isRefreshing = applications.loadState.refresh is LoadState.Loading,
                        state = refreshState,
                        onRefresh = {
                            applications.refresh()
                        },
                        indicator = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.TopCenter
                            ) {

                                PullToRefreshDefaults.Indicator(
                                    state = refreshState,
                                    isRefreshing = applications.loadState.refresh is LoadState.Loading,
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

                            items(
                                count = applications.itemCount,
                            ) { item ->
                                val application = applications[item]
                                if (application == null) {
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
//                                        .clickable {
//                                            //todo
////                                            onViewDetail(bill)
//                                        }
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
                                                text = "${formatterDateString(LocalDate.parse("2025-01-01"))} - ${formatterDateString(LocalDate.parse("2024-12-01"))}",
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
                                                        //todo view detail
                                                }
                                            ) {
                                                Text(
                                                    text ="Chi tiết",
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
                                                        application.appliedAt
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
                                                text = formatterVND(1000000L),
                                                style = AppTypography.bodyLarge,
                                                color =Black1,
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
                                                ){
                                                    //todo payment action
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
                                                text = formatterVND(1000000L),
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
                                                ){
                                                    //todo payment action
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
                                                text = formatterVND(100000L),
                                                style = AppTypography.bodyMedium.copy(
                                                    fontWeight = FontWeight.SemiBold
                                                ),
                                                color = Blue1,
                                            )

                                        }
                                    }

                                }

                            }
                            when (val state = applications.loadState.append) {
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
            if (isShowFilter) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Gray3.copy(alpha = 0.5f),
                        )
                        .padding(20.dp)
                        .pointerInput(Unit) {}) {
                    PayLaterApplicationHistoryFilterDialog(
                        currentStatus = uiState.selectedStatus,
                        currentType = uiState.selectedType,
                        currentFromDate = uiState.fromDate,
                        currentToDate = uiState.toDate,
                        onApply = { selectedStatus, selectedType, fromDate, toDate ->
                            onApply(
                                selectedStatus,
                                selectedType,
                                fromDate,
                                toDate
                            )
                            isShowFilter = false
                        },
                        onDismiss = {
                            isShowFilter = false
                        }
                    )
                }

            }
        }

    }
}

@Preview(
    showBackground = true, showSystemUi = true

)
@Composable
fun BillingCyclePreview() {

    val pagingData = PagingData.from(
        listOf(
            PayLaterApplicationResponse(
                id = "DL123456",
                type = PayLaterApplicationType.SUSPEND_REQUEST,
                requestedCreditLimit = 5000000.0,
                status = PayLaterApplicationStatus.PENDING,
                appliedAt = "2024-08-15",
                username = "john_doe",
                approvedLimit = null,
                reason = null,
                rejectionReason = null,
                approvedBy = null,
                processedAt = null,
            ),

            PayLaterApplicationResponse(
                id = "DL12345a6",
                type = PayLaterApplicationType.SUSPEND_REQUEST,
                requestedCreditLimit = 5000000.0,
                status = PayLaterApplicationStatus.APPROVED,
                appliedAt = "2024-08-15",
                username = "john_doe",
                approvedLimit = null,
                reason = null,
                rejectionReason = null,
                approvedBy = null,
                processedAt = null,
            ),

            )
    )
    BillingCycleScreen (
        uiState = PayLaterApplicationHistoryUiState(),
        applications = flowOf(pagingData).collectAsLazyPagingItems(),
        onBackClick = {},
        onErrorLoading = {},
        onApply = { _, _, _, _ -> }
    )
}
