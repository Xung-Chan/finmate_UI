package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.dtos.definitions.SpendingRecordType
import com.example.ibanking_kltn.dtos.responses.SpendingCategoryDetailResponse
import com.example.ibanking_kltn.dtos.responses.SpendingRecordResponse
import com.example.ibanking_kltn.dtos.responses.SpendingSnapshotDetailResponse
import com.example.ibanking_kltn.ui.event.SpendingDetailEvent
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Blue3
import com.example.ibanking_kltn.ui.theme.Blue5
import com.example.ibanking_kltn.ui.theme.Blue6
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.Green1
import com.example.ibanking_kltn.ui.theme.Green2
import com.example.ibanking_kltn.ui.theme.Green3
import com.example.ibanking_kltn.ui.theme.Red3
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.ChartType
import com.example.ibanking_kltn.ui.uistates.SpendingDetailState
import com.example.ibanking_kltn.ui.uistates.SpendingDetailTab
import com.example.ibanking_kltn.ui.uistates.SpendingDetailUiState
import com.example.ibanking_kltn.utils.CustomBarChart
import com.example.ibanking_kltn.utils.CustomPieChart
import com.example.ibanking_kltn.utils.ProgressBarWithLabel
import com.example.ibanking_kltn.utils.RetryCompose
import com.example.ibanking_kltn.utils.colorFromLabel
import com.example.ibanking_kltn.utils.customClick
import com.example.ibanking_kltn.utils.formatterDateTimeString
import com.example.ibanking_kltn.utils.formatterVND
import com.example.ibanking_kltn.utils.shimmerEffect
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.Pie
import kotlinx.coroutines.flow.flowOf
import java.math.BigDecimal
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendingSnapshotDetail(
    uiState: SpendingDetailUiState,
    records: LazyPagingItems<SpendingRecordResponse>,
    onBackClick: () -> Unit,
    onEvent: (SpendingDetailEvent) -> Unit
) {
    val scrollState = rememberScrollState(0)
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true   // mở full, không dừng ở nửa
    )
    var showSheet by remember {
        mutableStateOf(false)
    }

    when (uiState.screenState) {
        SpendingDetailState.INIT -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(20.dp)
                                    .background(
                                        color = Gray2.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .shimmerEffect()
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { onBackClick() }) {
                                Icon(
                                    Icons.Default.ArrowBackIosNew,
                                    contentDescription = null,
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            titleContentColor = Black1,
                            containerColor = White3
                        ),
                    )
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = White3,
                    ) {
                        NavigationBarItem(
                            selected = uiState.selectedTab == SpendingDetailTab.OVERVIEW,
                            onClick = { },
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.analytic),
                                    contentDescription = null
                                )
                            },
                            label = { Text("Tổng quan") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Blue3,
                                selectedTextColor = Blue3,
                                indicatorColor = Blue5.copy(alpha = 0.1f)
                            ),
                        )
                        NavigationBarItem(
                            selected = uiState.selectedTab == SpendingDetailTab.HISTORY,
                            onClick = { },
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.history),
                                    contentDescription = null
                                )
                            },
                            label = { Text("Lịch sử giao dịch") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Blue3,
                                selectedTextColor = Blue3,
                                indicatorColor = Blue5.copy(alpha = 0.1f)
                            ),
                        )
                    }
                },
                modifier = Modifier.systemBarsPadding(),
                containerColor = White3
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SpendingHeaderSkeleton()

                        when (uiState.selectedTab) {
                            SpendingDetailTab.OVERVIEW -> OverviewSkeleton()
                            SpendingDetailTab.HISTORY -> TransactionHistorySkeleton()
                        }
                    }
                }
            }
        }

        SpendingDetailState.INIT_FAILED -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Trở lại")
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                onBackClick()
                            }) {
                                Icon(
                                    Icons.Default.ArrowBackIosNew, contentDescription = null,
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            titleContentColor = Black1,
                            containerColor = White3
                        ),
                    )
                }) { paddingValue ->
                Column(
                    modifier = Modifier.padding(paddingValue)
                ) {
                    RetryCompose(
                        onRetry = {
                            onEvent(
                                SpendingDetailEvent.RetryLoadData
                            )
                        },
                    )
                }

            }
        }

        else -> {
            if (uiState.spendingSnapshot == null) {
                return
            }


            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = uiState.spendingSnapshot.snapshotName)
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                onBackClick()
                            }) {
                                Icon(
                                    Icons.Default.ArrowBackIosNew, contentDescription = null,
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            titleContentColor = Black1,
                            containerColor = White3
                        ),
                    )
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = White3,
                    ) {
                        NavigationBarItem(
                            selected = uiState.selectedTab == SpendingDetailTab.OVERVIEW,
                            onClick = {
                                if (uiState.selectedTab != SpendingDetailTab.OVERVIEW)
                                    onEvent(
                                        SpendingDetailEvent.ChangeTab
                                    )

                            },
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.analytic),
                                    contentDescription = null
                                )
                            },
                            label = { Text("Tổng quan") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Blue3,
                                selectedTextColor = Blue3,
                                indicatorColor = Blue5.copy(
                                    alpha = 0.1f
                                )
                            ),
                        )
                        NavigationBarItem(
                            selected = uiState.selectedTab == SpendingDetailTab.HISTORY,
                            onClick = {
                                if (uiState.selectedTab != SpendingDetailTab.HISTORY)
                                    onEvent(
                                        SpendingDetailEvent.ChangeTab
                                    )
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.history),
                                    contentDescription = null
                                )
                            },
                            label = { Text("Lịch sử giao dịch") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Blue3,
                                selectedTextColor = Blue3,
                                indicatorColor = Blue5.copy(
                                    alpha = 0.1f
                                )
                            ),
                        )
                    }
                },
                modifier = Modifier.systemBarsPadding(),
                containerColor = White3
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .then(
                            if (uiState.selectedTab == SpendingDetailTab.OVERVIEW)
                                Modifier.verticalScroll(scrollState)
                            else
                                Modifier
                        )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 30.dp,
                                    shape = RoundedCornerShape(20.dp),
                                    ambientColor = Black1.copy(alpha = 0.25f),
                                    spotColor = Black1.copy(alpha = 0.25f)
                                )
                                .background(color = White1, shape = RoundedCornerShape(20.dp))
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Mức chi tiêu",
                                color = Gray1,
                                style = AppTypography.bodySmall
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            color = Black1,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                    ) {
                                        append("${formatterVND(uiState.spendingSnapshot.usedAmount.toLong())}đ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 13.sp,
                                            color = Gray1
                                        )

                                    ) {
                                        append(
                                            "/${formatterVND(uiState.spendingSnapshot.budgetAmount.toLong())}đ"
                                        )
                                    }
                                }
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            color = Blue6.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .customClick(
                                            onClick = {
                                                onEvent(
                                                    SpendingDetailEvent.AddTransaction
                                                )
                                            },
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .padding(vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(
                                        5.dp,
                                        alignment = Alignment.CenterHorizontally
                                    )
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.add_regular),
                                        contentDescription = null,
                                        tint = Blue1,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Thêm giao dịch",
                                        style = AppTypography.bodySmall,
                                        color = Blue1
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            color = Blue6.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .customClick(
                                            onClick = {
                                                showSheet = true

                                            },
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .padding(vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(
                                        5.dp,
                                        alignment = Alignment.CenterHorizontally
                                    )
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.layer),
                                        contentDescription = null,
                                        tint = Blue1,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Danh mục",
                                        style = AppTypography.bodySmall,
                                        color = Blue1
                                    )
                                }


                            }
                        }
                        //overview
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        color = Green3.copy(
                                            alpha = 0.2f
                                        ),
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .padding(vertical = 10.dp, horizontal = 20.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.incoming_money),
                                        contentDescription = null,
                                        tint = Gray1,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Ngân sách",
                                        style = AppTypography.labelMedium,
                                        color = Gray1
                                    )
                                }
                                Text(
                                    text = formatterVND(uiState.spendingSnapshot.budgetAmount.toLong()) + "đ",
                                    style = AppTypography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Black1
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        color = Red3.copy(
                                            alpha = 0.2f
                                        ),
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .padding(vertical = 10.dp, horizontal = 20.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.outgoing_money),
                                        contentDescription = null,
                                        tint = Gray1,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Đã dùng",
                                        style = AppTypography.labelMedium,
                                        color = Gray1
                                    )
                                }
                                Text(
                                    text = formatterVND(uiState.spendingSnapshot.usedAmount.toLong()) + "đ",
                                    style = AppTypography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Black1
                                )
                            }
                        }
                        Column(
                            modifier = if (uiState.selectedTab == SpendingDetailTab.HISTORY)
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            else
                                Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {


                            when (uiState.selectedTab) {

                                SpendingDetailTab.OVERVIEW -> Overview(
                                    uiState = uiState,
                                    onEvent = onEvent
                                )

                                SpendingDetailTab.HISTORY -> TransactionHistory(
                                    uiState = uiState,
                                    records = records,
                                )

                            }
                        }
                    }
                }


            }
            // Show categories dialog
            if (showSheet) {
                ModalBottomSheet(
                    containerColor = White3,
                    onDismissRequest = { showSheet = false },
                    sheetState = sheetState
                ) {
                    CategoryDialogContent()
                }
            }
        }
    }

}

@Preview
@Composable
private fun CategoryDialogContent(
) {
    val allCategories: List<SpendingCategoryDetailResponse> = listOf(
        SpendingCategoryDetailResponse(
            categoryName = "Ăn uống",
            categoryCode = "food",
            categoryIcon = R.drawable.airplane_service.toString(),
            textColor = "TODO()",
            backgroundColor = "TODO()",
            budgetAmount = BigDecimal(5000000),
            usedAmount = BigDecimal(3500000),
        ),
        SpendingCategoryDetailResponse(
            categoryName = "Đi lại",
            budgetAmount = BigDecimal(5000000),
            usedAmount = BigDecimal(3500000),
            categoryCode = "transport",
            categoryIcon = R.drawable.airplane_service.toString(),
            textColor = "TODO()",
            backgroundColor = "TODO()",
        ),

        )
    val spendingCategories: List<SpendingCategoryDetailResponse> = listOf(
        SpendingCategoryDetailResponse(
            categoryName = "Đi lại",
            budgetAmount = BigDecimal(5000000),
            usedAmount = BigDecimal(3500000),
            categoryCode = "transport",
            categoryIcon = R.drawable.airplane_service.toString(),
            textColor = "TODO()",
            backgroundColor = "TODO()",
        ),
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Không có danh mục bạn cần?",
                style = AppTypography.bodySmall,
                modifier = Modifier.weight(1f)
            )
            Row(
                modifier = Modifier
                    .background(
                        color = Blue6.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .customClick(
                        onClick = {
                            //todo
                        },
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(
                        vertical = 10.dp,
                        horizontal = 5.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    5.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.add_regular),
                    contentDescription = null,
                    tint = Blue1,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Thêm danh mục",
                    style = AppTypography.bodySmall,
                    color = Blue1
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = allCategories) { category ->
                val isExisting = spendingCategories.contains(category)
                if (isExisting) {
                    ServiceComponent(
                        icon = category.categoryIcon.toInt(),
                        serviceName = category.categoryName,
                        color = colorFromLabel(category.categoryName),
                        actionIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ok_status_bold),
                                contentDescription = null,
                                tint = Green1,
                                modifier = Modifier.size(15.dp)
                            )
                        },
                        onClick = {//todo
                        }
                    )
                } else {
                    ServiceComponent(
                        icon = category.categoryIcon.toInt(),
                        serviceName = category.categoryName,
                        color = colorFromLabel(category.categoryName),
                        actionIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.add_bold),
                                contentDescription = null,
                                tint = Gray1,
                                modifier = Modifier.size(15.dp)
                            )
                        },
                        onClick = {
                            //todo
                        },
                    )
                }
            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
                .background(
                    color = Blue5,
                    shape = RoundedCornerShape(10.dp)
                )
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(10.dp),
                    ambientColor = Color.Transparent,
                    spotColor = Color.Transparent
                )
                .clickable {
                    //todo
                }
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Xác nhận",
                style = AppTypography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = White1
            )
        }

    }
}

@Composable
private fun SpendingHeaderSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 30.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Black1.copy(alpha = 0.25f),
                spotColor = Black1.copy(alpha = 0.25f)
            )
            .background(color = White1, shape = RoundedCornerShape(20.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .width(90.dp)
                .height(14.dp)
                .background(
                    color = Gray2.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(4.dp)
                )
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(18.dp)
                .background(
                    color = Gray2.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(4.dp)
                )
                .shimmerEffect()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(2) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .background(
                            color = Gray2.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
private fun OverviewSkeleton() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Tab skeleton
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                10.dp,
                Alignment.End
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 120.dp, height = 40.dp)
                    .background(
                        color = Gray2.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .shimmerEffect()
            )
        }

        // Overview cards skeleton
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(2) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Gray2.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(vertical = 10.dp, horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(12.dp)
                            .background(
                                color = Gray2.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .shimmerEffect()
                    )
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(16.dp)
                            .background(
                                color = Gray2.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .shimmerEffect()
                    )
                }
            }
        }

        // Chart skeleton
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    color = Gray2.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(15.dp)
                )
                .shimmerEffect()
        )

        // Category list skeleton
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(14.dp)
                    .background(
                        color = Gray2.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .shimmerEffect()
            )

            repeat(3) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .background(
                                color = Gray2.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(3.dp)
                            )
                            .shimmerEffect()
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(14.dp)
                                    .background(
                                        color = Gray2.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .shimmerEffect()
                            )
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(14.dp)
                                    .background(
                                        color = Gray2.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .shimmerEffect()
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .background(
                                    color = Gray2.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .shimmerEffect()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Overview(
    uiState: SpendingDetailUiState,
    onEvent: (SpendingDetailEvent) -> Unit
) {
    if (uiState.spendingSnapshot == null) {
        return
    }


    //tab
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            10.dp,
            Alignment.End
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (uiState.chartType) {
            ChartType.BAR -> {
                Row(
                    modifier = Modifier
                        .background(
                            color = Gray2.copy(
                                alpha = 0.3f
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(5.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(
                                color = Blue5.copy(
                                    alpha = 0.2f
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(5.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.bar_chart),
                            contentDescription = null,
                            tint = Blue1,
                            modifier = Modifier.size(25.dp)
                        )
                        Text(
                            text = "Chi tiết",
                            style = AppTypography.bodyMedium,
                            color = Blue1
                        )
                    }
                    Row(
                        modifier = Modifier
                            .customClick {
                                onEvent(
                                    SpendingDetailEvent.ChangeChartType
                                )
                            }
                            .padding(5.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.pie_chart),
                            contentDescription = null,
                            tint = Gray1,
                            modifier = Modifier
                                .size(25.dp)
                        )
                    }

                }
            }

            ChartType.PIE -> {
                Row(
                    modifier = Modifier
                        .background(
                            color = Gray2.copy(
                                alpha = 0.3f
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .customClick {
                                onEvent(
                                    SpendingDetailEvent.ChangeChartType
                                )
                            }
                            .padding(5.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.bar_chart),
                            contentDescription = null,
                            tint = Gray1,
                            modifier = Modifier.size(25.dp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(
                                color = Blue5.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(5.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.pie_chart),
                            contentDescription = null,
                            tint = Blue1,
                            modifier = Modifier.size(25.dp)
                        )
                        Text(
                            text = "Ngân sách",
                            style = AppTypography.bodyMedium,
                            color = Blue1
                        )
                    }
                }
            }

        }
    }

    //chart
    when (
        uiState.chartType
    ) {
        ChartType.BAR -> {
            CustomBarChart(
                data = uiState.spendingSnapshot.spendingCategories
                    .map { category ->
                        Bars(
                            label = category.categoryName,
                            values = listOf(
                                Bars.Data(
                                    label = "Ngân sách",
                                    value = category.budgetAmount.toDouble(),
                                    color = Brush.linearGradient(
                                        colors =
                                            listOf(
                                                Green2, Green2
                                            )

                                    ),
                                ),
                                Bars.Data(
                                    label = "Đã dùng",
                                    value = category.usedAmount.toDouble(),
                                    color = Brush.linearGradient(
                                        colors =
                                            listOf(
                                                Blue5, Blue5
                                            )

                                    ),
                                ),
                            )
                        )
                    },
                modifier = Modifier.fillMaxWidth()
            )
        }

        ChartType.PIE -> {
            CustomPieChart(
                data = uiState.spendingSnapshot.spendingCategories
                    .map { category ->
                        Pie(
                            label = category.categoryName,
                            data = category.budgetAmount.toDouble(),
                            color = colorFromLabel(category.categoryName)
                        )

                    },
                modifier = Modifier.fillMaxWidth()
            )

            val categories =
                uiState.spendingSnapshot.spendingCategories
            if (categories.isNotEmpty()) {
                Text(
                    text = "Phân tích chi tiêu theo danh mục",
                    style = AppTypography.bodyMedium,
                    color = Gray1
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    categories.forEach { category ->
                        val percentage =
                            category.budgetAmount.toFloat() / uiState.spendingSnapshot.budgetAmount.toFloat()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                10.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(15.dp)
                                    .background(
                                        color = colorFromLabel(
                                            category.categoryName
                                        ),
                                        shape = RoundedCornerShape(3.dp)
                                    )
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = category.categoryName,
                                        style = AppTypography.bodyMedium,
                                        color = Black1
                                    )
                                    Text(
                                        text = formatterVND(
                                            category.budgetAmount.toLong()
                                        ),
                                        style = AppTypography.bodyMedium,
                                        color = Black1,
                                        modifier = Modifier.weight(
                                            1f
                                        ),
                                        textAlign = TextAlign.End
                                    )
                                }
                                ProgressBarWithLabel(
                                    modifier = Modifier.fillMaxWidth(),
                                    progress = percentage
                                )

                            }
                        }

                    }

                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionHistorySkeleton() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Budget overview skeleton
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(2) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Gray2.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(vertical = 10.dp, horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(12.dp)
                            .background(
                                color = Gray2.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .shimmerEffect()
                    )
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(16.dp)
                            .background(
                                color = Gray2.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .shimmerEffect()
                    )
                }
            }
        }

        // Transaction cards skeleton
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp)
        ) {
            repeat(5) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 30.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Black1.copy(alpha = 0.25f),
                            spotColor = Black1.copy(alpha = 0.25f)
                        )
                        .background(color = White1, shape = RoundedCornerShape(20.dp))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Title and date skeleton
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .width(150.dp)
                                .height(16.dp)
                                .background(
                                    color = Gray2.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .shimmerEffect()
                        )
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(14.dp)
                                .background(
                                    color = Gray2.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .shimmerEffect()
                        )
                    }

                    // Category and amount skeleton
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(24.dp)
                                .background(
                                    color = Gray2.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .shimmerEffect()
                        )
                        Box(
                            modifier = Modifier
                                .width(90.dp)
                                .height(14.dp)
                                .background(
                                    color = Gray2.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .shimmerEffect()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionHistory(
    records: LazyPagingItems<SpendingRecordResponse>,
    uiState: SpendingDetailUiState,
) {
    val refreshState = rememberPullToRefreshState()
    if (
        uiState.spendingSnapshot == null
    ) {
        return
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        

        PullToRefreshBox(
            state = refreshState,
            indicator = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {

                    PullToRefreshDefaults.Indicator(
                        state = refreshState,
                        isRefreshing = uiState.screenState == SpendingDetailState.REFRESHING,
                        containerColor = White1,
                        color = Blue3,
                    )
                }

            },

            isRefreshing = uiState.screenState == SpendingDetailState.REFRESHING,
            onRefresh = {
                records.refresh()
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 10.dp),
            ) {
                for (i in 0 until records.itemCount) {
                    val record = records[i] ?: continue
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 30.dp,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = Black1.copy(alpha = 0.25f),
                                spotColor = Black1.copy(alpha = 0.25f)
                            )
                            .background(color = White1, shape = RoundedCornerShape(20.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = record.description,
                                style = AppTypography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Black1,
                                modifier = Modifier.weight(1f),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                            Text(
                                text = formatterDateTimeString(LocalDateTime.parse(record.occurredAt)),
                                style = AppTypography.bodySmall,
                                color = Gray1
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (record.categoryName != null && record.categoryIcon != null) {

                                Row(
                                    modifier = Modifier
                                        .background(
                                            color = colorFromLabel(record.categoryName).copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .padding(5.dp),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        5.dp
                                    )
                                ) {
                                    Icon(
                                        painter = painterResource(id = record.categoryIcon.toInt()),
                                        contentDescription = null,
                                        tint = colorFromLabel(record.categoryName),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = record.categoryName,
                                        style = AppTypography.labelMedium,
                                        color = Black1
                                    )
                                }
                            } else {
                                val color = colorFromLabel("Không xác định")
                                Row(
                                    modifier = Modifier.background(
                                        color = color.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.unknown),
                                        contentDescription = null,
                                        tint = color,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Không xác định",
                                        style = AppTypography.labelMedium,
                                        color = Black1
                                    )
                                }
                            }

                            Text(
                                text = "-" + formatterVND(record.amount.toLong()),
                                style = AppTypography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Black1,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
                when (val state = records.loadState.append) {
                    is LoadState.NotLoading -> Unit
                    is LoadState.Loading -> {

                        CircularProgressIndicator(
                            color = Blue3, modifier = Modifier.size(30.dp)
                        )
                    }

                    is LoadState.Error -> {
                        //todo
                    }
                }
            }
        }
    }
}


//@Preview(
//    showBackground = true,
//    showSystemUi = true
//)
@Composable
fun SpendingFundDetailPreview() {
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
            selectedTab = SpendingDetailTab.OVERVIEW
        ),
        records = flowOf(
            pagingData
        ).collectAsLazyPagingItems(),
        onBackClick = {},
        onEvent = {},
    )
}

