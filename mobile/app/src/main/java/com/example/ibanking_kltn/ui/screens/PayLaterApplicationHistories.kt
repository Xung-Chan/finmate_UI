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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.dtos.definitions.PayLaterApplicationStatus
import com.example.ibanking_kltn.dtos.definitions.PayLaterApplicationType
import com.example.ibanking_kltn.dtos.responses.PayLaterApplicationResponse
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue3
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.Gray3
import com.example.ibanking_kltn.ui.theme.Green1
import com.example.ibanking_kltn.ui.theme.Orange1
import com.example.ibanking_kltn.ui.theme.Red1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.PayLaterApplicationHistoryUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.PayLaterApplicationHistoryFilterDialog
import com.example.ibanking_kltn.utils.customClick
import com.example.ibanking_kltn.utils.formatterDateString
import com.example.ibanking_kltn.utils.formatterVND
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayLaterApplicationHistoryScreen(
    uiState: PayLaterApplicationHistoryUiState,
    applications: LazyPagingItems<PayLaterApplicationResponse>,
    onBackClick: () -> Unit,
    onErrorLoading: (String) -> Unit,
    onApply: (
        selectedStatus: PayLaterApplicationStatus?,
        selectedType: PayLaterApplicationType?,
        fromDate: LocalDate,
        toDate: LocalDate,
    ) -> Unit,

    ) {
    val refreshState = rememberPullToRefreshState()
    var isShowFilter by remember {
        mutableStateOf(false)
    }
    var selectedApplication by remember {
        mutableStateOf<PayLaterApplicationResponse?>(null)
    }

    LoadingScaffold(
        isLoading = uiState.screenState is StateType.LOADING,
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Lịch sử đăng ký")
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
                        if(
                            applications.itemCount ==0
                        ){
                            item {
                                Text(
                                    text = "Không tìm thấy đơn nào.",
                                    style = AppTypography.bodySmall,
                                    color = Gray1,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
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
                                    .clickable {

                                        selectedApplication = application
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
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Loại đơn",
                                            style = AppTypography.bodyMedium,
                                            color = Gray1,

                                            )
                                    }
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = application.type.typeName,
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
                                            text = "Ngày đăng ký",
                                            style = AppTypography.bodyMedium,
                                            color = Gray1,

                                            )
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = formatterDateString(LocalDateTime.parse(application.appliedAt).toLocalDate()),
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
                                            text = "Trạng thái",
                                            style = AppTypography.bodyMedium,
                                            color = Gray1,

                                            )
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = when (application.status) {
                                                PayLaterApplicationStatus.APPROVED -> "Đã duyệt"
                                                PayLaterApplicationStatus.PENDING -> "Đang chờ xử lý"
                                                PayLaterApplicationStatus.REJECTED -> "Bị từ chối"
                                                PayLaterApplicationStatus.CANCELED -> "Đã hủy"
                                            },
                                            style = AppTypography.bodyMedium,
                                            color = when (application.status) {
                                                PayLaterApplicationStatus.APPROVED -> Green1
                                                PayLaterApplicationStatus.PENDING -> Orange1
                                                PayLaterApplicationStatus.REJECTED -> Red1
                                                PayLaterApplicationStatus.CANCELED -> Gray1
                                            },
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
        selectedApplication?.let {
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
                                .padding(  20.dp)

                        ) {

                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {

                                Row(

                                    modifier = Modifier
                                        .background(
                                            color = Gray2, shape = RoundedCornerShape(20.dp)
                                        )
                                        .customClick(
                                            shape = RoundedCornerShape(20.dp)
                                        ) {
                                            selectedApplication = null
                                        }
                                        .padding(horizontal = 10.dp,vertical = 5.dp)
                                ) {
                                    Text(
                                        text = "Đóng",
                                        style = AppTypography.bodyMedium,
                                        color = White1,
                                    )

                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),

                                ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Mã đơn đăng ký",
                                        style = AppTypography.bodyMedium,
                                        color = Gray1,

                                        )
                                }
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = selectedApplication!!.id,
                                        style = AppTypography.bodyMedium,
                                        color = Black1,
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Ngày đăng ký",
                                        style = AppTypography.bodyMedium,
                                        color = Gray1,

                                        )
                                }
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = formatterDateString(
                                            LocalDateTime.parse(
                                                selectedApplication!!.appliedAt
                                            ).toLocalDate()
                                        ),
                                        style = AppTypography.bodyMedium,
                                        color = Black1,
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Loại đơn đăng ký",
                                        style = AppTypography.bodyMedium,
                                        color = Gray1,

                                        )
                                }
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = selectedApplication!!.type.typeName,
                                        style = AppTypography.bodyMedium,
                                        color = Black1,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.End

                                    )
                                }
                            }

                            if (selectedApplication!!.type == PayLaterApplicationType.LIMIT_ADJUSTMENT) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Hạn mức yêu cầu",
                                            style = AppTypography.bodyMedium,
                                            color = Gray1,

                                            )
                                    }
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = formatterVND(selectedApplication!!.requestedCreditLimit.toLong()),
                                            style = AppTypography.bodyMedium,
                                            color = Black1,
                                        )
                                    }
                                }
                            }
                            if (selectedApplication!!.approvedLimit != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Hạn mức chấp nhận",
                                            style = AppTypography.bodyMedium,
                                            color = Gray1,

                                            )
                                    }
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = formatterVND(selectedApplication!!.approvedLimit!!.toLong()),
                                            style = AppTypography.bodyMedium,
                                            color = Black1,
                                        )
                                    }
                                }

                            }
                            if (selectedApplication!!.reason != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    Row(
                                        modifier = Modifier.weight(0.4f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Lý do",
                                            style = AppTypography.bodyMedium,
                                            color = Gray1,

                                            )
                                    }
                                    Row(
                                        modifier = Modifier.weight(0.6f),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = selectedApplication!!.reason!!,
                                            style = AppTypography.bodyMedium,
                                            color = Black1,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.End
                                        )
                                    }
                                }

                            }
                            if (selectedApplication!!.rejectionReason != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    Row(
                                        modifier = Modifier.weight(0.4f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Lý do từ chối",
                                            style = AppTypography.bodyMedium,
                                            color = Gray1,

                                            )
                                    }
                                    Row(
                                        modifier = Modifier.weight(0.6f),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = selectedApplication!!.rejectionReason!!,
                                            style = AppTypography.bodyMedium,
                                            color = Black1,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.End
                                        )
                                    }
                                }

                            }

                            if (selectedApplication!!.status != PayLaterApplicationStatus.PENDING) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Thời gian xử lý",
                                            style = AppTypography.bodyMedium,
                                            color = Gray1,

                                            )
                                    }
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = formatterDateString(
                                                LocalDateTime.parse(
                                                    selectedApplication!!.processedAt!!
                                                ).toLocalDate()
                                            ),
                                            style = AppTypography.bodyMedium,
                                            color = Black1,
                                        )
                                    }
                                }

                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Trạng thái",
                                        style = AppTypography.bodyMedium,
                                        color = Gray1,

                                        )
                                }
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = when (selectedApplication!!.status) {
                                            PayLaterApplicationStatus.APPROVED -> "Đã duyệt"
                                            PayLaterApplicationStatus.PENDING -> "Đang chờ xử lý"
                                            PayLaterApplicationStatus.REJECTED -> "Bị từ chối"
                                            PayLaterApplicationStatus.CANCELED -> "Đã hủy"
                                        },
                                        style = AppTypography.bodyMedium,
                                        color = when (selectedApplication!!.status) {
                                            PayLaterApplicationStatus.APPROVED -> Green1
                                            PayLaterApplicationStatus.PENDING -> Orange1
                                            PayLaterApplicationStatus.REJECTED -> Red1
                                            PayLaterApplicationStatus.CANCELED -> Gray1
                                        },
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

@Preview(
    showBackground = true, showSystemUi = true

)
@Composable
fun PayLaterApplicationPreview() {

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
    PayLaterApplicationHistoryScreen(
        uiState = PayLaterApplicationHistoryUiState(),
        applications = flowOf(pagingData).collectAsLazyPagingItems(),
        onBackClick = {},
        onErrorLoading = {},
        onApply = { _, _, _, _ -> }
    )
}
