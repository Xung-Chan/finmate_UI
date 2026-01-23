package com.example.ibanking_kltn.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.data.dtos.ServiceCategory
import com.example.ibanking_kltn.ui.event.HomeEvent
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Blue5
import com.example.ibanking_kltn.ui.theme.Blue6
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.HomeUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.RetryCompose
import com.example.ibanking_kltn.utils.SkeletonBox
import com.example.ibanking_kltn.utils.formatterVND


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeUiState: HomeUiState = HomeUiState(),
    userComponent: @Composable () -> Unit,
    navigationBar: @Composable () -> Unit,
    onNavigateServiceList: () -> Unit,
    onEvent: (HomeEvent) -> Unit
) {


    val bottomBarHeight = 100.dp
    val scrollState = rememberScrollState(0)
    LoadingScaffold(
        isLoading = homeUiState.state is StateType.LOADING,
    ) {
        Scaffold(
            modifier = Modifier.systemBarsPadding()
        ) { paddingValues ->
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)

            ) {
                when(homeUiState.initState){
                    is StateType.FAILED -> {
                        RetryCompose(
                            onRetry = {
                                onEvent(
                                    HomeEvent.RetryLoadUserInfo

                                )
                            },
                        )
                    }
                    StateType.LOADING -> {
                        HomeScreenSkeleton(
                            bottomBarHeight = bottomBarHeight
                        )
                    }
                    else -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Blue1)
                                .verticalScroll(scrollState)
                        ) {
                            //user info row
                            userComponent()
                            //Main container
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        color = White3,
                                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                                    )
                                    .padding(30.dp)
                            ) {
                                //card
                                //top card
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = Blue5,
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .clip(RoundedCornerShape(20.dp))
                                ) {
                                    Canvas(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        drawCircle(
                                            color = Blue1,
                                            radius = 600f,
                                            center = Offset(50f, 500f)
                                        )
                                        drawCircle(
                                            color = Blue6,
                                            radius = 250f,
                                            center = Offset(size.width, 0f)
                                        )
                                    }

                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = homeUiState.myWallet?.merchantName?:"",
                                                color = White1,
                                                style = AppTypography.headlineMedium
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = homeUiState.myWallet?.walletNumber?:"",
                                                color = White1,
                                                style = AppTypography.bodyMedium
                                            )
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            if (homeUiState.isBalanceShow) {

                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                )
                                                {
                                                    Text(
                                                        text = "${formatterVND(homeUiState.myWallet?.balance?.toLong()?:0L)} VND",
                                                        color = White1,
                                                        style = AppTypography.titleMedium,
                                                    )
                                                }
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.End,
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .padding(5.dp)
                                                        .shadow(
                                                            elevation = 30.dp, shape = CircleShape
                                                        )
                                                        .clickable {
                                                            onEvent(
                                                                HomeEvent.ChangeVisibilityBalance
                                                            )
                                                        }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Visibility,
                                                        contentDescription = null,
                                                        tint = White1,
                                                    )
                                                }

                                            } else {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                )
                                                {
                                                    Text(
                                                        text = "********* VND",
                                                        color = White1,
                                                        style = AppTypography.titleMedium,
                                                    )

                                                }
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.End,
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .padding(5.dp)
                                                        .shadow(
                                                            elevation = 30.dp, shape = CircleShape
                                                        )
                                                        .clickable { onEvent(HomeEvent.ChangeVisibilityBalance) }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.VisibilityOff,
                                                        contentDescription = null,
                                                        tint = White1,
                                                    )
                                                }

                                            }

                                        }


                                    }
                                }

                                //frequently
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(15.dp)
                                ) {
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = stringResource(R.string.Home_Section1),
                                            color = Black1,
                                            style = AppTypography.bodyMedium

                                        )
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        for (it in homeUiState.recentServices) {
                                            val serviceCategory = ServiceCategory.valueOf(it.service)
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier
                                                    .width(70.dp)
                                                    .clickable {
                                                        onEvent(
                                                            HomeEvent.ClickService(
                                                                serviceCategory
                                                            )
                                                        )
                                                    }
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.Center,
                                                    modifier = Modifier
                                                        .size(70.dp)
                                                        .background(
                                                            color = Color(serviceCategory.color).copy(
                                                                alpha = 0.08f
                                                            ),
                                                            shape = RoundedCornerShape(10)
                                                        )
                                                ) {
                                                    Icon(
                                                        painter = painterResource(id = serviceCategory.icon),
                                                        tint = Color(serviceCategory.color),
                                                        contentDescription = null
                                                    )
                                                }

                                                Row(modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        text = ServiceCategory.valueOf(it.service).serviceName,
                                                        color = Black1,
                                                        style = AppTypography.bodyMedium,
                                                        textAlign = TextAlign.Center
                                                    )
                                                }

                                            }
                                        }

                                    }

                                }
                                //service
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(modifier = Modifier.weight(1f)) {

                                            Text(
                                                text = stringResource(R.string.Home_Section2),
                                                color = Black1,
                                                style = AppTypography.bodyMedium
                                            )
                                        }
                                        Row(
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            TextButton(onClick = {
                                                onNavigateServiceList()
                                            }) {
                                                Text(
                                                    text = "Xem tất cả",
                                                    color = Blue1,
                                                    style = AppTypography.bodyMedium
                                                )
                                            }
                                        }
                                    }

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(
                                            10.dp, Alignment.CenterVertically
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)

                                    ) {
                                        for (i in 0 until homeUiState.favoriteServices.size step 2) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(IntrinsicSize.Min),
                                                horizontalArrangement = Arrangement.spacedBy(
                                                    10.dp,
                                                    Alignment.CenterHorizontally
                                                ),

                                                ) {

                                                for (j in i until i + 2) {
                                                    val serviceItem =
                                                        homeUiState.favoriteServices.getOrNull(j)
                                                            ?: continue
                                                    val serviceCategory =
                                                        ServiceCategory.valueOf(serviceItem.service)
                                                    Column(
                                                        verticalArrangement = Arrangement.spacedBy(10.dp),
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .fillMaxHeight()
                                                            .shadow(
                                                                elevation = 30.dp,
                                                                shape = RoundedCornerShape(20),
                                                                clip = true,
                                                                ambientColor = Black1.copy(alpha = 0.9f),
                                                                spotColor = Black1.copy(alpha = 0.9f),
                                                            )
                                                            .clickable {
                                                                onEvent(
                                                                    HomeEvent.ClickService(
                                                                        serviceCategory
                                                                    )
                                                                )
                                                            }
                                                            .background(
                                                                color = White1,
                                                                shape = RoundedCornerShape(16)
                                                            )
                                                            .padding(15.dp)
                                                    ) {
                                                        Column(
                                                            verticalArrangement = Arrangement.spacedBy(
                                                                15.dp
                                                            )
                                                        ) {
                                                            Icon(
                                                                painter = painterResource(
                                                                    serviceCategory.icon
                                                                ),
                                                                tint = Color(serviceCategory.color),
                                                                contentDescription = null
                                                            )
                                                            Text(
                                                                text = ServiceCategory.valueOf(
                                                                    homeUiState.favoriteServices[j].service
                                                                ).serviceName,
                                                                style = AppTypography.bodyMedium
                                                            )

                                                        }
                                                    }
                                                }

                                            }

                                        }


                                    }

                                }
                                Spacer(modifier = Modifier.height(bottomBarHeight * 5 / 8))

                            }
                        }
                    }
                }
                navigationBar()

            }
        }
    }


}
@Composable
fun HomeScreenSkeleton(
    bottomBarHeight: Dp
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        HomeUserHeaderSkeleton()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = White3,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            WalletCardSkeleton()

            RecentServicesSkeleton()

            FavoriteServicesSkeleton()

            Spacer(modifier = Modifier.height(bottomBarHeight * 5 / 8))
        }
    }
}

@Composable
fun HomeUserHeaderSkeleton() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        SkeletonBox(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            shape = CircleShape
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            SkeletonBox(
                modifier = Modifier
                    .width(120.dp)
                    .height(14.dp)
            )
            SkeletonBox(
                modifier = Modifier
                    .width(80.dp)
                    .height(12.dp)
            )
        }
    }
}

@Composable
fun WalletCardSkeleton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Blue5)
            .padding(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SkeletonBox(
                modifier = Modifier
                    .width(160.dp)
                    .height(20.dp)
            )
            SkeletonBox(
                modifier = Modifier
                    .width(200.dp)
                    .height(16.dp)
            )
            SkeletonBox(
                modifier = Modifier
                    .width(140.dp)
                    .height(22.dp)
            )
        }
    }
}

@Composable
fun RecentServicesSkeleton() {
    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {

        SkeletonBox(
            modifier = Modifier
                .width(180.dp)
                .height(18.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(4) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    SkeletonBox(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Spacer(Modifier.height(6.dp))
                    SkeletonBox(
                        modifier = Modifier
                            .width(60.dp)
                            .height(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteServicesSkeleton() {
    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            SkeletonBox(
                modifier = Modifier
                    .width(160.dp)
                    .height(18.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            SkeletonBox(
                modifier = Modifier
                    .width(80.dp)
                    .height(16.dp)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(2) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(2) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .height(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(White1)
                                .padding(15.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SkeletonBox(
                                modifier = Modifier.size(30.dp)
                            )
                            SkeletonBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(
    showSystemUi = true,
    showBackground = true

)
@Composable
fun HomePreview() {
//    val mocList = listOf(
//        ServiceItem(
//            service = ServiceCategory.MONEY_TRANSFER.name,
//            lastUsed = System.currentTimeMillis(),
//        ),
//        ServiceItem(
//            service = ServiceCategory.DEPOSIT.name,
//            lastUsed = System.currentTimeMillis(),
//        ),
//        ServiceItem(
//            service = ServiceCategory.BILL_HISTORY.name,
//            lastUsed = System.currentTimeMillis(),
//        ),
//        ServiceItem(
//            service = ServiceCategory.BILL_PAYMENT.name,
//            lastUsed = System.currentTimeMillis(),
//        ),
//    )
    HomeScreen(
        homeUiState = HomeUiState(
            initState = StateType.LOADING
        ),
        navigationBar = {},
        onNavigateServiceList = {},
        userComponent = {},
        onEvent = {},
    )
}