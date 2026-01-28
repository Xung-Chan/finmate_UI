package com.example.ibanking_kltn.ui.screens


import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.data.dtos.MoneyFlowType
import com.example.ibanking_kltn.ui.event.AnalyticEvent
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Blue5
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.Green1
import com.example.ibanking_kltn.ui.theme.Orange1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.AnalyticUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.CustomBarChart
import com.example.ibanking_kltn.utils.CustomPieChart
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.ProgressBarWithLabel
import com.example.ibanking_kltn.utils.RetryCompose
import com.example.ibanking_kltn.utils.SkeletonBox
import com.example.ibanking_kltn.utils.colorFromLabel
import com.example.ibanking_kltn.utils.customClick
import com.example.ibanking_kltn.utils.formatterVND
import com.kizitonwose.calendar.core.yearMonth
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.Pie
import java.time.LocalDate
import java.time.YearMonth


@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticScreen(
    uiState: AnalyticUiState,
    userComponent: @Composable () -> Unit,
    navigationBar: @Composable () -> Unit,
    onEvent: (AnalyticEvent) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val bottomBarHeight = 100.dp
    val scrollState = rememberScrollState(0)

    var selectedStatisticType by remember {
        mutableStateOf(StatisticType.DISTRIBUTION)
    }
    LoadingScaffold(
        isLoading = uiState.state == StateType.LOADING
    ) {
        Scaffold(
            modifier = Modifier.systemBarsPadding(),
        ) { paddingValues ->
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)

            ) {
                when (uiState.initState) {
                    is StateType.FAILED -> {
                        RetryCompose(
                            onRetry = {
                                onEvent(
                                    AnalyticEvent.RetryLoadData
                                )
                            },
                        )
                    }

                    StateType.LOADING -> {
                        AnalyticScreenSkeleton(
                            screenHeight = screenHeight,
                            bottomBarHeight = bottomBarHeight,
                            scrollState = scrollState
                        )
                    }

                    else -> {
                        if (uiState.trendStatistic == null || uiState.distributionStatistic == null) {
                            RetryCompose(
                                onRetry = {
                                    onEvent(
                                        AnalyticEvent.RetryLoadData
                                    )
                                },
                            )
                        } else {

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = Blue1)
                                    .verticalScroll(scrollState)
                            ) {
                                //user infor row
                                userComponent()
                                //Main container
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier
                                        .defaultMinSize(
                                            minHeight = screenHeight
                                        )
                                        .fillMaxSize()
                                        .background(
                                            color = White3,
                                            shape = RoundedCornerShape(
                                                topStart = 20.dp,
                                                topEnd = 20.dp
                                            )
                                        )
                                        .padding(30.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(
                                            10.dp,
                                            Alignment.End
                                        ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        when (selectedStatisticType) {
                                            StatisticType.TREND -> {
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
                                                        text = "Xu hướng",
                                                        style = AppTypography.bodyMedium,
                                                        color = Blue1
                                                    )
                                                }
                                                Row(
                                                    modifier = Modifier
                                                        .customClick {
                                                            selectedStatisticType =
                                                                StatisticType.DISTRIBUTION
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

                                            StatisticType.DISTRIBUTION -> {
                                                Row(
                                                    modifier = Modifier
                                                        .customClick {
                                                            selectedStatisticType =
                                                                StatisticType.TREND
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
                                                        text = "Phân bố",
                                                        style = AppTypography.bodyMedium,
                                                        color = Blue1
                                                    )
                                                }
                                            }

                                        }
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .customClick {
                                                    onEvent(AnalyticEvent.MinusMonth)
                                                }
                                                .padding(5.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.arrow_left),
                                                contentDescription = null,
                                                tint = Black1,
                                                modifier = Modifier.size(25.dp)
                                            )
                                        }
                                        Row(
                                            modifier = Modifier
                                                .weight(1f),
                                            horizontalArrangement = Arrangement.spacedBy(
                                                10.dp,
                                                Alignment.CenterHorizontally
                                            ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.calendar),
                                                contentDescription = null,
                                                tint = Black1,
                                                modifier = Modifier.size(25.dp)
                                            )
                                            Text(
                                                text = if (uiState.selectedTime == LocalDate.now()) "Tháng này" else "${uiState.selectedTime.month.value}/${uiState.selectedTime.year}",
                                                style = AppTypography.bodyMedium,
                                                color = Black1
                                            )

                                        }

                                        Row(
                                            modifier = Modifier
                                                .customClick {
                                                    if (uiState.selectedTime.yearMonth < LocalDate.now().yearMonth) {
                                                        onEvent(
                                                            AnalyticEvent.PlusMonth
                                                        )
                                                    }
                                                }
                                                .padding(5.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.arrow_right),
                                                contentDescription = null,
                                                tint = if (uiState.selectedTime.yearMonth < LocalDate.now().yearMonth) Black1 else Gray2,
                                                modifier = Modifier.size(25.dp)
                                            )
                                        }
                                    }
                                    if (selectedStatisticType == StatisticType.TREND) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .border(
                                                        width = 1.dp,
                                                        color = if (uiState.selectedFlowType == MoneyFlowType.OUTGOING) Blue5 else Black1,
                                                        shape = RoundedCornerShape(10.dp)
                                                    )
                                                    .customClick {
                                                        onEvent(
                                                            AnalyticEvent.ChangeMoneyFlowType(
                                                                MoneyFlowType.OUTGOING
                                                            )
                                                        )
                                                    }
                                                    .padding(vertical = 10.dp),
                                                horizontalArrangement = Arrangement.spacedBy(
                                                    10.dp,
                                                    Alignment.CenterHorizontally
                                                ),
                                                verticalAlignment = Alignment.CenterVertically

                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.outgoing_money),
                                                    contentDescription = null,
                                                    tint = if (uiState.selectedFlowType == MoneyFlowType.OUTGOING) Blue1 else Black1,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Text(
                                                    text = "Chi tiêu",
                                                    style = AppTypography.bodyMedium,
                                                    color = if (uiState.selectedFlowType == MoneyFlowType.OUTGOING) Blue1 else Black1
                                                )
                                            }
                                            Row(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .border(
                                                        width = 1.dp,
                                                        color = if (uiState.selectedFlowType == MoneyFlowType.INCOMING) Blue5 else Black1,
                                                        shape = RoundedCornerShape(10.dp)
                                                    )
                                                    .customClick {
                                                        onEvent(
                                                            AnalyticEvent.ChangeMoneyFlowType(
                                                                MoneyFlowType.INCOMING
                                                            )
                                                        )
                                                    }
                                                    .padding(vertical = 10.dp),
                                                horizontalArrangement = Arrangement.spacedBy(
                                                    10.dp,
                                                    Alignment.CenterHorizontally
                                                ),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.incoming_money),
                                                    contentDescription = null,
                                                    tint = if (uiState.selectedFlowType == MoneyFlowType.INCOMING) Blue1 else Black1,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Text(
                                                    text = "Thu nhập",
                                                    style = AppTypography.bodyMedium,
                                                    color = if (uiState.selectedFlowType == MoneyFlowType.INCOMING) Blue1 else Black1
                                                )
                                            }


                                        }
                                    }
                                    when (selectedStatisticType) {

                                        StatisticType.TREND -> {
                                            CustomBarChart(
                                                data = uiState.trendStatistic.filter { item ->
                                                    val itemMonth = YearMonth.parse(item.date)
                                                    val current = YearMonth.of(
                                                        uiState.selectedTime.year,
                                                        uiState.selectedTime.monthValue
                                                    )
                                                    val validMonth = listOf(
                                                        current.minusMonths(1),
                                                        current,
                                                        current.plusMonths(1)
                                                    )
                                                    itemMonth in validMonth && itemMonth.year == uiState.selectedTime.year
                                                }.map { trendStatisticResponseItem ->
                                                    Bars(
                                                        label = trendStatisticResponseItem.date,
                                                        values = listOf(
                                                            Bars.Data(
                                                                label = when (uiState.selectedFlowType) {
                                                                    MoneyFlowType.OUTGOING -> "Chi tiêu"
                                                                    MoneyFlowType.INCOMING -> "Thu nhập"
                                                                },
                                                                value = trendStatisticResponseItem.totalValue.toDouble(),
                                                                color = Brush.linearGradient(
                                                                    colors = if (
                                                                        YearMonth.parse(
                                                                            trendStatisticResponseItem.date
                                                                        ) == YearMonth.of(
                                                                            uiState.selectedTime.year,
                                                                            uiState.selectedTime.monthValue
                                                                        )
                                                                    ) {
                                                                        listOf(
                                                                            Blue5, Blue5,
                                                                        )
                                                                    } else {
                                                                        listOf(
                                                                            Blue1, Blue1
                                                                        )

                                                                    }
                                                                ),
                                                            ),
                                                        )
                                                    )
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()

                                            )
                                        }

                                        StatisticType.DISTRIBUTION -> {
                                            CustomPieChart(
                                                data = uiState.distributionStatistic.distributions.filter { it.totalValue > 0 }
                                                    .map { distribution ->
                                                        Pie(
                                                            label = distribution.expenseName,
                                                            data = distribution.totalValue.toDouble(),
                                                            color = colorFromLabel(distribution.expenseName)
                                                        )

                                                    },
                                                modifier = Modifier.fillMaxWidth()
                                            )

                                            val distributions =
                                                uiState.distributionStatistic.distributions.filter { it.totalValue > 0 }
                                            if (distributions.isNotEmpty()) {
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

                                                    distributions.forEach { distribution ->
                                                        val percentage =
                                                            distribution.totalValue.toFloat() / uiState.totalValue.toFloat()
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
                                                                            distribution.expenseName
                                                                        ),
                                                                        shape = RoundedCornerShape(3.dp)
                                                                    )
                                                            )
                                                            Column(modifier = Modifier.weight(1f)) {
                                                                Row(modifier = Modifier.fillMaxWidth()) {
                                                                    Text(
                                                                        text = distribution.expenseName,
                                                                        style = AppTypography.bodyMedium,
                                                                        color = Black1
                                                                    )
                                                                    Text(
                                                                        text = formatterVND(
                                                                            distribution.totalValue
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
                                    if (uiState.analyzeResponse == null) {
                                        Row(
                                            modifier = Modifier
                                                .background(
                                                    color = Blue5.copy(
                                                        alpha = 0.2f
                                                    ),
                                                    shape = RoundedCornerShape(
                                                        bottomStart = 10.dp,
                                                        bottomEnd = 10.dp
                                                    )
                                                )
                                                .customClick {
                                                    if (uiState.analyzeState != StateType.LOADING)
                                                        onEvent(AnalyticEvent.Analyze)
                                                }
                                                .padding(10.dp),
                                            horizontalArrangement = Arrangement.spacedBy(
                                                10.dp,
                                            ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .border(
                                                        width = 1.dp,
                                                        color = Blue5,
                                                        shape = RoundedCornerShape(5.dp)
                                                    )
                                                    .padding(5.dp)
                                            ) {
                                                Icon(
                                                    painter = painterResource(R.drawable.ai),
                                                    contentDescription = null,
                                                    tint = Blue5,
                                                    modifier = Modifier.size(25.dp)
                                                )
                                            }
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "Phân tích chi tiêu bằng AI",
                                                    style = AppTypography.bodyMedium,
                                                    color = Black1
                                                )
                                                Text(
                                                    text = if (uiState.analyzeState == StateType.LOADING) "AI đang phân tích..." else "Để FinMate giúp bạn phân tích chi tiêu",
                                                    style = AppTypography.bodySmall,
                                                    color = Gray1
                                                )
                                            }
                                            if (uiState.analyzeState == StateType.LOADING) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(25.dp),
                                                    color = Blue5,
                                                    strokeWidth = 3.dp
                                                )
                                            }
                                        }

                                    } else {
                                        Text(
                                            text = "Phân tích chi tiêu của bạn",
                                            style = AppTypography.bodyMedium,
                                            color = Gray1
                                        )
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            AnalyzeRow(
                                                backgroundColor = Blue5
                                            ) {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    modifier = Modifier.weight(0.3f)
                                                ) {

                                                    Icon(
                                                        painter = painterResource(R.drawable.trend),
                                                        contentDescription = null,
                                                        tint = Blue5,
                                                        modifier = Modifier.size(25.dp)
                                                    )
                                                    Text(
                                                        text = "Xu hướng",
                                                        style = AppTypography.bodySmall,
                                                        color = Blue5,
                                                    )
                                                }
                                                Text(
                                                    text = uiState.analyzeResponse.xu_huong,
                                                    style = AppTypography.bodyMedium,
                                                    color = Blue5,
                                                    modifier = Modifier.weight(0.7f),
                                                    textAlign = TextAlign.Justify
                                                )
                                            }
                                            AnalyzeRow(
                                                backgroundColor = Green1
                                            ) {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    modifier = Modifier.weight(0.3f),

                                                    ) {
                                                    Icon(
                                                        painter = painterResource(R.drawable.percent),
                                                        contentDescription = null,
                                                        tint = Green1,
                                                        modifier = Modifier.size(25.dp)
                                                    )
                                                    Text(
                                                        text = "Tỉ trọng",
                                                        style = AppTypography.bodySmall,
                                                        color = Green1,
                                                    )
                                                }
                                                Text(
                                                    text = uiState.analyzeResponse.ty_trong,
                                                    style = AppTypography.bodyMedium,
                                                    color = Green1,
                                                    modifier = Modifier.weight(0.7f),
                                                    textAlign = TextAlign.Justify
                                                )
                                            }
                                            AnalyzeRow(
                                                backgroundColor = Orange1
                                            ) {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    modifier = Modifier.weight(0.3f),

                                                    ) {
                                                    Icon(
                                                        painter = painterResource(R.drawable.warning),
                                                        contentDescription = null,
                                                        tint = Orange1,
                                                        modifier = Modifier.size(25.dp)
                                                    )
                                                    Text(
                                                        text = "Cảnh báo",
                                                        style = AppTypography.bodySmall,
                                                        color = Orange1,
                                                    )
                                                }
                                                Text(
                                                    text = uiState.analyzeResponse.canh_bao,
                                                    style = AppTypography.bodyMedium,
                                                    color = Orange1,
                                                    modifier = Modifier.weight(0.7f),
                                                    textAlign = TextAlign.Justify
                                                )
                                            }
                                        }
                                    }
                                    if (selectedStatisticType == StatisticType.DISTRIBUTION) {
                                        Spacer(modifier = Modifier.height(bottomBarHeight * 5 / 8))
                                    }

                                }


                            }
                        }
                    }
                }
                navigationBar()
            }
        }
    }
}

enum class StatisticType {
    TREND,
    DISTRIBUTION
}


@Composable
private fun AnalyzeRow(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()

            .border(
                width = 1.dp,
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = backgroundColor.copy(
                    alpha = 0.2f
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(
            10.dp,
            Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically

    ) {
        content()
    }

}

@Composable
fun AnalyticScreenSkeleton(
    screenHeight: Dp,
    bottomBarHeight: Dp,
    scrollState: ScrollState
) {
    Column(
        modifier = Modifier
            .background(Blue1)
            .verticalScroll(scrollState)
    ) {

        // User info skeleton
        UserInfoSkeleton()

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .defaultMinSize(minHeight = screenHeight)
                .fillMaxSize()
                .background(
                    color = White3,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .padding(30.dp)
        ) {

            StatisticTypeSwitchSkeleton()

            CalendarRowSkeleton()

            FlowTypeSwitchSkeleton()

            ChartSkeleton()

            DistributionSkeleton()

            AnalyzeSkeleton()

        }

        Spacer(modifier = Modifier.height(bottomBarHeight * 5 / 8))
    }
}

@Composable
private fun UserInfoSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SkeletonBox(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.width(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SkeletonBox(
                Modifier
                    .width(120.dp)
                    .height(14.dp)
            )
            SkeletonBox(
                Modifier
                    .width(180.dp)
                    .height(14.dp)
            )
        }
    }
}

@Composable
private fun StatisticTypeSwitchSkeleton() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SkeletonBox(
            modifier = Modifier
                .width(120.dp)
                .height(35.dp),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@Composable
private fun CalendarRowSkeleton() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SkeletonBox(Modifier.size(30.dp), shape = CircleShape)

        SkeletonBox(
            modifier = Modifier
                .width(160.dp)
                .height(20.dp),
            shape = RoundedCornerShape(6.dp)
        )

        SkeletonBox(Modifier.size(30.dp), shape = CircleShape)
    }
}

@Composable
private fun FlowTypeSwitchSkeleton() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SkeletonBox(
            modifier = Modifier
                .weight(1f)
                .height(45.dp),
            shape = RoundedCornerShape(10.dp)
        )
        SkeletonBox(
            modifier = Modifier
                .weight(1f)
                .height(45.dp),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@Composable
private fun ChartSkeleton() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SkeletonBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
private fun DistributionSkeleton() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        SkeletonBox(
            modifier = Modifier
                .width(200.dp)
                .height(16.dp)
        )

        repeat(4) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SkeletonBox(
                    modifier = Modifier
                        .size(15.dp),
                    shape = RoundedCornerShape(3.dp)
                )
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    SkeletonBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                    )
                    Spacer(Modifier.height(6.dp))
                    SkeletonBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        shape = RoundedCornerShape(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AnalyzeSkeleton() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        SkeletonBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(10.dp)
        )

        repeat(3) {
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                shape = RoundedCornerShape(10.dp)
            )
        }
    }
}


@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun AnalyticPreview() {
//    AnalyticScreen(
//        uiState = AnalyticUiState(
//            trendStatistic = TrendStatisticResponse().apply {
//                addAll(
//                    listOf(
////                        TrendStatisticResponseItem(
////                            date = "2025-01",
////                            totalTransactions = 0,
////                            totalValue = 100000L
////                        ),
////                        TrendStatisticResponseItem(
////                            date = "2025-02",
////                            totalTransactions = 0,
////                            totalValue = 100000L
////                        ),
//                    )
//                )
//            },
//            distributionStatistic = DistributionStatisticResponse(
//                analyticId = "1",
//                distributions = listOf(
////                    Distribution(
////                        label = "Ăn uống",
////                        expenseName = "Ăn uống",
////                        expenseTag = "an_uong",
////                        totalTransactions = 10,
////                        totalValue = 500000L
////                    ),
////                    Distribution(
////                        label = "Sức khỏe",
////                        expenseName = "Sức khỏe",
////                        expenseTag = "suc_khoe",
////                        totalTransactions = 10,
////                        totalValue = 5000000L
////                    ),
//                )
//            ),
//            initialedDistributionStatistic = true,
//            initialedTrendStatistic = true,
//
//            ),
//        navigationBar = {},
//        onRetry = {},
//        onMinusMonth = {},
//        onPlusMonth = {},
//        onAnalyze = {},
//        onChangeMoneyFlowType = {},
//    )

}