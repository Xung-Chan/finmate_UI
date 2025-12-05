package com.example.ibanking_kltn.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Blue2
import com.example.ibanking_kltn.ui.theme.Blue5
import com.example.ibanking_kltn.ui.theme.Blue6
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.Green1
import com.example.ibanking_kltn.ui.theme.Orange2
import com.example.ibanking_kltn.ui.theme.Red3
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.HomeUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.formatterDateString
import com.example.ibanking_kltn.utils.formatterVND
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeUiState: HomeUiState = HomeUiState(),
    onChangeVisibleBalance: () -> Unit,
    onNavigateToTransferScreen: () -> Unit,
//    onNavigateToSettingScreen: () -> Unit,
    onNavigateToPayBillScreen: () -> Unit,
    navigationBar: @Composable () -> Unit
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    //user infor row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                space = 10.dp,
                                alignment = Alignment.Start
                            ),

                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(
                                        color = White1,
                                        shape = CircleShape
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    tint = Black1,
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp)
                                )

                            }
                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    text = formatterDateString(LocalDate.now()),
                                    color = White1,
                                    style = CustomTypography.titleMedium
                                )
                                Text(
                                    text = "Xin chào, ${homeUiState.myWallet?.username ?: "Unknown User"}!",
                                    color = White1,
                                    style = CustomTypography.titleMedium
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = {},
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(
                                        Alignment.CenterVertically
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = White1,
                                    modifier = Modifier.size(35.dp)
                                )
                            }
                            IconButton(
                                onClick = {},
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(
                                        Alignment.CenterVertically
                                    )
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.question),
                                    contentDescription = null,
                                    tint = White1,
                                    modifier = Modifier.size(35.dp)
                                )
                            }
                        }
                    }
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
                        Box() {
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
                                            text = homeUiState.myWallet?.merchantName
                                                ?: "Unknown Merchant",
                                            color = White1,
                                            style = CustomTypography.headlineMedium
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = homeUiState.myWallet?.walletNumber
                                                ?: "**** **** **** ****",
                                            color = White1,
                                            style = CustomTypography.titleMedium
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
                                                    text = "${formatterVND(homeUiState.myWallet?.balance?.toLong() ?: 0L)} VND",
                                                    color = White1,
                                                    style = CustomTypography.titleLarge,
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
                                                    style = CustomTypography.titleLarge,
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
                                    style = CustomTypography.titleMedium

                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.width(70.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .size(70.dp)
                                            .background(
                                                color = Green1.copy(alpha = 0.08f),
                                                shape = RoundedCornerShape(10)
                                            )
                                    ) {
                                        Icon(
                                            Icons.Default.Call,
                                            tint = Green1,
                                            contentDescription = null
                                        )
                                    }

                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            "Mobile Recharg",
                                            color = Black1,
                                            style = CustomTypography.titleSmall,
                                            textAlign = TextAlign.Center
                                        )
                                    }

                                }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .width(70.dp)
                                        .clickable {
                                            onNavigateToPayBillScreen()
                                        }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .size(70.dp)
                                            .background(
                                                color = Red3.copy(alpha = 0.08f),
                                                shape = RoundedCornerShape(10)
                                            )
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.bill),
                                            tint = Red3,
                                            contentDescription = null
                                        )
                                    }

                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = stringResource(R.string.PayBill_Title),
                                            color = Black1,
                                            style = CustomTypography.titleSmall,
                                            textAlign = TextAlign.Center
                                        )
                                    }

                                }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .width(70.dp)
                                        .clickable {
                                            onNavigateToTransferScreen()
                                        }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .size(70.dp)
                                            .background(
                                                color = Orange2.copy(alpha = 0.08f),
                                                shape = RoundedCornerShape(10)
                                            )
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.transfer),
                                            tint = Orange2,
                                            contentDescription = null
                                        )
                                    }

                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = stringResource(R.string.Transfer_Title),
                                            color = Black1,
                                            style = CustomTypography.titleSmall,
                                            textAlign = TextAlign.Center
                                        )
                                    }

                                }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.width(70.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .size(70.dp)
                                            .background(
                                                color = Blue2.copy(alpha = 0.08f),
                                                shape = RoundedCornerShape(10)
                                            )
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.requestmoney),
                                            tint = Blue2,
                                            contentDescription = null
                                        )
                                    }

                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            "Mobile Recharg",
                                            color = Black1,
                                            style = CustomTypography.titleSmall,
                                            textAlign = TextAlign.Center
                                        )
                                    }

                                }

                            }

                        }
                        //service
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(R.string.Home_Section2),
                                    color = Black1,
                                    style = CustomTypography.titleMedium
                                )
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
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        10.dp,
                                        Alignment.CenterHorizontally
                                    )
                                ) {
                                    //Feat Cell
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .shadow(
                                                elevation = 30.dp,
                                                shape = RoundedCornerShape(20),
                                                clip = true,
                                                ambientColor = Black1.copy(alpha = 0.9f),
                                                spotColor = Black1.copy(alpha = 0.9f),
                                            )
                                            .background(
                                                color = White1,
                                                shape = RoundedCornerShape(16)
                                            )
                                            .padding(15.dp)
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                                            Icon(Icons.Default.Call, contentDescription = null)
                                            Text(
                                                text = "Feat Cell",
                                                style = CustomTypography.titleSmall
                                            )

                                        }
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .shadow(
                                                elevation = 30.dp,
                                                shape = RoundedCornerShape(20),
                                                clip = true,
                                                ambientColor = Black1.copy(alpha = 0.9f),
                                                spotColor = Black1.copy(alpha = 0.9f),
                                            )
                                            .background(
                                                color = White1,
                                                shape = RoundedCornerShape(16)
                                            )
                                            .padding(15.dp)
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                                            Icon(Icons.Default.Call, contentDescription = null)
                                            Text(
                                                text = "Feat Cell",
                                                style = CustomTypography.titleSmall
                                            )

                                        }
                                    }

                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        10.dp,
                                        Alignment.CenterHorizontally
                                    )
                                ) {
                                    //Feat Cell
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .shadow(
                                                elevation = 30.dp,
                                                shape = RoundedCornerShape(20),
                                                clip = true,
                                                ambientColor = Black1.copy(alpha = 0.9f),
                                                spotColor = Black1.copy(alpha = 0.9f),
                                            )
                                            .background(
                                                color = White1,
                                                shape = RoundedCornerShape(16)
                                            )
                                            .padding(15.dp)
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                                            Icon(Icons.Default.Call, contentDescription = null)
                                            Text(
                                                text = "Feat Cell",
                                                style = CustomTypography.titleSmall
                                            )

                                        }
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .shadow(
                                                elevation = 30.dp,
                                                shape = RoundedCornerShape(20),
                                                clip = true,
                                                ambientColor = Black1.copy(alpha = 0.9f),
                                                spotColor = Black1.copy(alpha = 0.9f),
                                            )
                                            .background(
                                                color = White1,
                                                shape = RoundedCornerShape(16)
                                            )
                                            .padding(15.dp)
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                                            Icon(Icons.Default.Call, contentDescription = null)
                                            Text(
                                                text = "Feat Cell",
                                                style = CustomTypography.titleSmall
                                            )

                                        }
                                    }

                                }

                            }
                        }
                        Spacer(modifier = Modifier.height(bottomBarHeight * 5 / 8))

                    }
                }
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(bottomBarHeight)
//                        .background(color = Color.Transparent)
//                ) {
//                    //nav
//                    Column(
//                        verticalArrangement = Arrangement.Bottom,
//                        modifier = Modifier
//                            .height(bottomBarHeight)
//                            .fillMaxWidth()
////                        .background(White3)
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(
//                                75.dp,
//                                alignment = Alignment.CenterHorizontally
//                            ),
//                            modifier = Modifier
//
//                                .height(bottomBarHeight * 5 / 8)
//                                .fillMaxWidth()
//                                .shadow(
//                                    elevation = 30.dp,
//                                    shape = RoundedCornerShape(topStart = 0.2f, topEnd = 0.2f),
//                                    ambientColor = Black1.copy(alpha = 0.9f),
//                                    spotColor = Black1.copy(alpha = 0.9f),
//                                )
//                                .background(
//                                    color = White1,
//                                    shape = RoundedCornerShape(topStart = 0.2f, topEnd = 0.2f)
//                                )
//
//                        )
//                        {
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.spacedBy(
//                                    30.dp,
//                                    alignment = Alignment.CenterHorizontally
//                                ),
//                                modifier = Modifier
//                                    .weight(1f)
//                                    .padding(10.dp)
//                            ) {
//                                IconButton(onClick = {}) {
//                                    Icon(
//                                        painter = painterResource(R.drawable.category),
//                                        contentDescription = null,
//                                        tint = Blue3,
//                                        modifier = Modifier.size(35.dp)
//                                    )
//                                }
//
//                                IconButton(onClick = {}) {
//                                    Icon(
//                                        painter = painterResource(R.drawable.wallet),
//                                        contentDescription = null,
//                                        tint = Gray1,
//                                        modifier = Modifier.size(35.dp)
//                                    )
//                                }
//
//                            }
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.spacedBy(
//                                    30.dp,
//                                    alignment = Alignment.CenterHorizontally
//                                ),
//                                modifier = Modifier
//                                    .weight(1f)
//                                    .padding(10.dp)
//                            ) {
//                                IconButton(onClick = {}) {
//                                    Icon(
//                                        painter = painterResource(R.drawable.analytic),
//                                        contentDescription = null,
//                                        tint = Gray1,
//                                        modifier = Modifier.size(35.dp)
//                                    )
//                                }
//
//                                IconButton(onClick = {
//                                    onNavigateToSettingScreen()
//                                }) {
//                                    Icon(
//                                        painter = painterResource(R.drawable.profile),
//                                        contentDescription = null,
//                                        tint = Gray1,
//                                        modifier = Modifier.size(35.dp)
//                                    )
//                                }
//                            }
//                        }
//                    }
//
//                    Column(
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier
//                            .height(bottomBarHeight)
//                            .fillMaxWidth()
//                    ) {
//                        IconButton(
//                            onClick = {},
//                            modifier = Modifier
//                                .shadow(
//                                    elevation = 20.dp,
//                                    shape = RoundedCornerShape(40)
//                                )
//                                .border(
//                                    width = 5.dp, color = White1,
//                                    shape = RoundedCornerShape(40)
//                                )
//                                .background(color = Blue3, shape = RoundedCornerShape(40))
//                                .padding(5.dp)
//                        ) {
//                            Icon(
//                                painter = painterResource(R.drawable.qr),
//                                contentDescription = null,
//                                tint = White1,
//                                modifier = Modifier.size(35.dp)
//                            )
//                        }
//                    }
//                }

                navigationBar()

            }
        }
    }


}

//@Preview(
//    showSystemUi = true,
//    showBackground = true
//
//)
//@Composable
//fun HomePreview() {
//    HomeScreen(
//        homeUiState = HomeUiState(),
//        onChangeVisibleBalance = {},
//        onNavigateToSettingScreen = {},
//        onNavigateToTransferScreen = {},
//        onNavigateToPayBillScreen = {}
//    )
//}