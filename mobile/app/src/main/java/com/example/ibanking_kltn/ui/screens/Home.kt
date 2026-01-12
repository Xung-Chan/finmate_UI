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
import com.example.ibanking_kltn.data.dtos.ServiceItem
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Blue5
import com.example.ibanking_kltn.ui.theme.Blue6
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.HomeUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.RetryCompose
import com.example.ibanking_kltn.utils.formatterVND


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeUiState: HomeUiState = HomeUiState(),
//    appUiState: AppUiState,
    userComponent: @Composable () -> Unit,
    navigationBar: @Composable () -> Unit,
    onChangeVisibleBalance: () -> Unit,
    onNavigateTo: Map<String, () -> Unit>,
    onNavigateServiceList: () -> Unit,
    onRetry: () -> Unit,
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
                    .background(color = Blue1)
                    .padding(paddingValues)

            ) {
                if (!homeUiState.initialedUserInfo || !homeUiState.initialedUserWallet) {
                    HomeLoadingScreen()
                } else if (homeUiState.myWallet == null || homeUiState.myProfile == null) {
                    RetryCompose(
                        onRetry = {
                            onRetry()
                        },
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        //user infor row
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(10.dp)
//                        ) {
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.spacedBy(
//                                    space = 10.dp,
//                                    alignment = Alignment.Start
//                                ),
//
//                                modifier = Modifier.weight(1f)
//                            ) {
//                                Column(
//                                    modifier = Modifier
//                                        .size(50.dp)
//                                        .background(
//                                            color = White1,
//                                            shape = CircleShape
//                                        ),
//                                    horizontalAlignment = Alignment.CenterHorizontally,
//                                    verticalArrangement = Arrangement.Center
//                                ) {
//                                    if (appUiState.avatarUrl == null) {
//                                        DefaultImageProfile(
//                                            modifier = Modifier
//                                                .size(100.dp),
//                                            name =appUiState.fullName
//                                        )
//                                    } else {
//
//                                        AsyncImage(
//                                            model = appUiState.avatarUrl,
//                                            contentDescription = "Avatar",
//                                            modifier = Modifier
//                                                .size(100.dp)
//                                                .clip(CircleShape),
//                                            contentScale = ContentScale.Crop
//                                        )
//                                    }
//
//                                }
//                                Column(
//                                    horizontalAlignment = Alignment.Start,
//                                    verticalArrangement = Arrangement.Center,
//                                ) {
//                                    Text(
//                                        text = "Hôm nay, ${formatterDateString(LocalDate.now())}",
//                                        color = White1,
//                                        style = AppTypography.bodySmall
//                                    )
//                                    Text(
//                                        text = "Xin chào, ${appUiState.fullName}!",
//                                        color = White1,
//                                        style = AppTypography.bodyMedium
//                                    )
//                                }
//                            }
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.End
//                            ) {
//                                IconButton(
//                                    onClick = {},
//                                    modifier = Modifier
//                                        .size(40.dp)
//                                        .align(
//                                            Alignment.CenterVertically
//                                        )
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.Default.Notifications,
//                                        contentDescription = null,
//                                        tint = White1,
//                                        modifier = Modifier.size(35.dp)
//                                    )
//                                }
//                                IconButton(
//                                    onClick = {},
//                                    modifier = Modifier
//                                        .size(40.dp)
//                                        .align(
//                                            Alignment.CenterVertically
//                                        )
//                                ) {
//                                    Icon(
//                                        painter = painterResource(R.drawable.question),
//                                        contentDescription = null,
//                                        tint = White1,
//                                        modifier = Modifier.size(35.dp)
//                                    )
//                                }
//                            }
//                        }
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
                                    .background(color = Blue5, shape = RoundedCornerShape(20.dp))
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
                                            text = homeUiState.myWallet.merchantName,
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
                                            text = homeUiState.myWallet.walletNumber,
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
                                                    text = "${formatterVND(homeUiState.myWallet.balance.toLong())} VND",
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
                                                    .clickable { onChangeVisibleBalance() }
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
                                                    .clickable { onChangeVisibleBalance() }
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
                                                    onNavigateTo[it.service]?.invoke()
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
                                                            onNavigateTo[serviceItem.service]?.invoke()
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
                navigationBar()

            }
        }
    }


}

@Composable
fun HomeLoadingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        HomeHeaderSkeleton()
        HomeContentSkeleton()
    }
}

@Composable
private fun HomeHeaderSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Gray1.copy(alpha = 0.3f))
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.weight(1f)
        ) {
            SkeletonLine(width = 120.dp)
            SkeletonLine(width = 180.dp)
        }
    }
}
@Composable
private fun HomeContentSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                White3,
                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        WalletCardSkeleton()
        ServicesSkeleton()
    }
}
@Composable
private fun WalletCardSkeleton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Blue5.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SkeletonLine(width = 140.dp)
            SkeletonLine(width = 200.dp)
            SkeletonLine(width = 160.dp, height = 24.dp)
        }
    }
}
@Composable
private fun ServicesSkeleton() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SkeletonLine(width = 120.dp)

        repeat(2) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Gray1.copy(alpha = 0.2f))
                    )
                }
            }
        }
    }
}
@Composable
private fun SkeletonLine(
    width: Dp,
    height: Dp = 16.dp
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(6.dp))
            .background(Gray1.copy(alpha = 0.3f))
    )
}





@Preview(
    showSystemUi = true,
    showBackground = true

)
@Composable
fun HomePreview() {
    val mocList = listOf(
        ServiceItem(
            service = ServiceCategory.MONEY_TRANSFER.name,
            lastUsed = System.currentTimeMillis(),
        ),
        ServiceItem(
            service = ServiceCategory.DEPOSIT.name,
            lastUsed = System.currentTimeMillis(),
        ),
        ServiceItem(
            service = ServiceCategory.BILL_HISTORY.name,
            lastUsed = System.currentTimeMillis(),
        ),
        ServiceItem(
            service = ServiceCategory.BILL_PAYMENT.name,
            lastUsed = System.currentTimeMillis(),
        ),
    )
    HomeScreen(
        homeUiState = HomeUiState(
            favoriteServices = mocList,
            recentServices = mocList,
        ),
        onChangeVisibleBalance = {},
        navigationBar = {},
        onNavigateTo = mapOf(
            ServiceCategory.MONEY_TRANSFER.name to {},
            ServiceCategory.DEPOSIT.name to {},
            ServiceCategory.BILL_PAYMENT.name to {},
            ServiceCategory.BILL_HISTORY.name to {},
        ),
        onNavigateServiceList = {},
        onRetry = {},
//        appUiState = AppUiState()
        userComponent = {}
    )
}