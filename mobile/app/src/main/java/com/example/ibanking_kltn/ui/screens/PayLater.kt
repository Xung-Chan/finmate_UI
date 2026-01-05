package com.example.ibanking_kltn.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.data.dtos.PayLaterAccountStatus
import com.example.ibanking_kltn.data.dtos.responses.PayLaterResponse
import com.example.ibanking_kltn.data.dtos.responses.UserInfoResponse
import com.example.ibanking_kltn.data.dtos.responses.UserStatusEnum
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Blue5
import com.example.ibanking_kltn.ui.theme.Blue6
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.PayLaterUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.InformationLine
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.RetryCompose
import com.example.ibanking_kltn.utils.SkeletonBox
import com.example.ibanking_kltn.utils.customClick
import com.example.ibanking_kltn.utils.formatterDateString
import com.example.ibanking_kltn.utils.formatterVND
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayLaterScreen(
    uiState: PayLaterUiState,
    onBackClick: () -> Unit,
    onActivePaylater: () -> Unit,
    onLockPaylater: () -> Unit,
    onUnlockPaylater: () -> Unit,
    onNavigateToApplicationHistory: () -> Unit,
    onNavigateToBillingCycleHistory: () -> Unit,
    onRetry: () -> Unit,
) {
    val scrollState = rememberScrollState(0)
    var isShowBalance by remember {
        mutableStateOf(false)
    }
    val availableCredit = uiState.payLaterInfo?.let {
        (it.creditLimit - it.usedCredit).toLong()
    } ?: 0L
    LoadingScaffold(
        isLoading = uiState.screenState is StateType.LOADING,
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Thông tin của tôi", style = AppTypography.titleMedium)
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
            modifier = Modifier.systemBarsPadding(),
            containerColor = White3
        ) { paddingValues ->
            if (!uiState.initialedUserInfo || !uiState.initialedPayLaterInfo) {
                PayLaterScreenSkeleton(paddingValues)

            } else if (uiState.userInfo == null || uiState.payLaterInfo == null) {
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
                        .padding(paddingValues)
                        .padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
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
                        if (uiState.payLaterInfo.status == PayLaterAccountStatus.ACTIVE) {
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
                                        text = uiState.userInfo.fullName,
                                        color = White1,
                                        style = AppTypography.titleMedium
                                    )
                                }

                                Spacer(modifier = Modifier.height(50.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    if (isShowBalance) {

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        )
                                        {
                                            Text(
                                                text = "${formatterVND(availableCredit)} VND",
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
                                                    isShowBalance = false
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
                                                .clickable { isShowBalance = true }
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
                        else if (uiState.payLaterInfo.status == PayLaterAccountStatus.SUSPENDED) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 30.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.lock),
                                    contentDescription = null,
                                    tint = White1,
                                    modifier = Modifier.size(25.dp)
                                )
                                Text(
                                    text = "Bạn chưa kích hoạt ví trả sau",
                                    style = AppTypography.bodyMedium,
                                    color = White1
                                )
                                Row(
                                    modifier = Modifier
                                        .background(
                                            color = White1,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .customClick(shape = RoundedCornerShape(10.dp)) {
                                            if (uiState.payLaterInfo.status == PayLaterAccountStatus.PENDING) {
                                                onActivePaylater()
                                            } else if (uiState.payLaterInfo.status == PayLaterAccountStatus.SUSPENDED) {
                                                onUnlockPaylater()
                                            }
                                        }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = "Mở khóa",
                                        color = Black1,
                                        style = AppTypography.bodyMedium
                                    )
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 30.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.lock),
                                    contentDescription = null,
                                    tint = White1,
                                    modifier = Modifier.size(25.dp)
                                )
                                Text(
                                    text = "Bạn chưa kích hoạt ví trả sau",
                                    style = AppTypography.bodyMedium,
                                    color = White1
                                )
                                Row(
                                    modifier = Modifier
                                        .background(
                                            color = White1,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .customClick(shape = RoundedCornerShape(10.dp)) {
                                            if (uiState.payLaterInfo.status == PayLaterAccountStatus.PENDING) {
                                                onActivePaylater()
                                            } else if (uiState.payLaterInfo.status == PayLaterAccountStatus.SUSPENDED) {
                                                onUnlockPaylater()
                                            }
                                        }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = "Mở ngay",
                                        color = Black1,
                                        style = AppTypography.bodyMedium
                                    )
                                }
                            }
                        }
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
                            .background(color = White1, shape = RoundedCornerShape(20.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Thông tin tài khoản",
                            style = AppTypography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Black1

                        )
                        if (uiState.payLaterInfo.status != PayLaterAccountStatus.PENDING) {

                            Column(
                                verticalArrangement = Arrangement.spacedBy(15.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            ) {
                                InformationLine(
                                    title = "Tổng hạng mức",
                                    trailing = {
                                        Text(
                                            text = formatterVND(uiState.payLaterInfo.creditLimit.toLong()),
                                            style = AppTypography.bodyMedium,
                                            color = it,
                                            textAlign = TextAlign.End
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                )
                                InformationLine(
                                    title = "Số dư khả dụng",
                                    trailing = {
                                        Text(
                                            text = formatterVND(availableCredit),
                                            style = AppTypography.bodyMedium,
                                            color = it,
                                            textAlign = TextAlign.End
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                )
                                InformationLine(
                                    title = "Ngày đến hạn",
                                    trailing = {
                                        Text(
                                            text = uiState.payLaterInfo.nextDueDate,
                                            style = AppTypography.bodyMedium,
                                            color = it,
                                            textAlign = TextAlign.End
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                )
                                InformationLine(
                                    title = "Lãi suất",
                                    trailing = {
                                        Text(
                                            text = uiState.payLaterInfo.interestRate.toString(),
                                            style = AppTypography.bodyMedium,
                                            color = it,
                                            textAlign = TextAlign.End
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                )

                            }
                        } else {
                            Text(
                                text = "Ví trả sau của bạn chưa được kích hoạt",
                                style = AppTypography.bodySmall,
                                color = Gray1,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                textAlign = TextAlign.Center
                            )
                        }

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
                            .background(color = White1, shape = RoundedCornerShape(20.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Thông tin cá nhân",
                            style = AppTypography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Black1

                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(15.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            InformationLine(
                                title = "Họ và tên",
                                trailing = {
                                    Text(
                                        text = uiState.userInfo.fullName,
                                        style = AppTypography.bodyMedium,
                                        color = it,
                                        textAlign = TextAlign.End
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            InformationLine(
                                title = "Ngày sinh",
                                trailing = {
                                    Text(
                                        text = formatterDateString(LocalDate.parse(uiState.userInfo.dateOfBirth)),
                                        style = AppTypography.bodyMedium,
                                        color = it,
                                        textAlign = TextAlign.End
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            InformationLine(
                                title = "Sô điện thoại",
                                trailing = {
                                    Text(
                                        text = uiState.userInfo.phoneNumber,
                                        style = AppTypography.bodyMedium,
                                        color = it,
                                        textAlign = TextAlign.End
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            InformationLine(
                                title = "Địa chỉ",
                                trailing = {
                                    Text(
                                        text = uiState.userInfo.address,
                                        style = AppTypography.bodyMedium,
                                        color = it,
                                        textAlign = TextAlign.End

                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }

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
                            .background(color = White1, shape = RoundedCornerShape(20.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Tiện ích",
                            style = AppTypography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Black1

                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(15.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            InformationLine(
                                title = "Lịch sử đăng ký",
                                color = Black1,
                                leading = {
                                    Icon(
                                        painter = painterResource(R.drawable.history),
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp),
                                        tint = it
                                    )
                                },
                                trailing = {
                                    Icon(

                                        imageVector = Icons.Default.ArrowForwardIos,
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp), tint = it

                                    )
                                },
                                enable = true,
                                onClick = {
                                    onNavigateToApplicationHistory()
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )

                            if (uiState.payLaterInfo.status == PayLaterAccountStatus.ACTIVE) {
                                InformationLine(
                                    title = "Tạm khóa ví trả sau",
                                    color = Black1,
                                    leading = {
                                        Icon(
                                            painter = painterResource(R.drawable.lock),
                                            contentDescription = null,
                                            modifier = Modifier.size(25.dp),
                                            tint = it
                                        )
                                    },
                                    trailing = {
                                        Icon(

                                            imageVector = Icons.Default.ArrowForwardIos,
                                            contentDescription = null,
                                            modifier = Modifier.size(25.dp), tint = it

                                        )
                                    },
                                    enable = true,
                                    onClick = onLockPaylater,
                                    modifier = Modifier.fillMaxWidth(),
                                )


                            }
                            if (uiState.payLaterInfo.status != PayLaterAccountStatus.PENDING) {
                                InformationLine(
                                    title = "Thanh toán nợ dư",
                                    color = Black1,
                                    leading = {
                                        Icon(
                                            painter = painterResource(R.drawable.billing_circle),
                                            contentDescription = null,
                                            modifier = Modifier.size(25.dp),
                                            tint = it
                                        )
                                    },
                                    trailing = {
                                        Icon(

                                            imageVector = Icons.Default.ArrowForwardIos,
                                            contentDescription = null,
                                            modifier = Modifier.size(25.dp), tint = it

                                        )
                                    },
                                    enable = true,
                                    onClick = {
                                        onNavigateToBillingCycleHistory()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                )

                            }

                        }

                    }
                }

            }
        }


    }

}

@Composable
fun PayLaterHeaderSkeleton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(color = Blue5, shape = RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SkeletonBox(
                modifier = Modifier
                    .width(180.dp)
                    .height(20.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            SkeletonBox(
                modifier = Modifier
                    .width(220.dp)
                    .height(28.dp)
            )
        }
    }
}

@Composable
fun InfoCardSkeleton(lines: Int = 4) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 30.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SkeletonBox(
            modifier = Modifier
                .width(160.dp)
                .height(18.dp)
        )

        repeat(lines) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SkeletonBox(
                    modifier = Modifier
                        .width(120.dp)
                        .height(14.dp)
                )
                SkeletonBox(
                    modifier = Modifier
                        .width(100.dp)
                        .height(14.dp)
                )
            }
        }
    }
}

@Composable
fun UtilitySkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(30.dp, RoundedCornerShape(20.dp))
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SkeletonBox(
            modifier = Modifier
                .width(100.dp)
                .height(18.dp)
        )

        repeat(3) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                SkeletonBox(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                SkeletonBox(
                    modifier = Modifier
                        .height(14.dp)
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
fun PayLaterScreenSkeleton(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        PayLaterHeaderSkeleton()

        InfoCardSkeleton(lines = 4)
        InfoCardSkeleton(lines = 4)
        UtilitySkeleton()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PayLaterPreview() {
    PayLaterScreen(
        uiState = PayLaterUiState(
            userInfo = UserInfoResponse(
                id = "1",
                username = "john_doe",
                fullName = "John Doe",
                dateOfBirth = "1990-01-01",
                mail = "abc@gmail.com",
                status = UserStatusEnum.ACTIVE,
                phoneNumber = "0123456789",
                avatarUrl = "https://placehold.jp/150x150.png",
                gender = "Male",
                address = "123 Main St, City, Country",
                cardId = "123456789012",
            ),
            payLaterInfo = PayLaterResponse(
                approvedAt = "2023-01-01T00:00:00Z",
                approvedBy = "admin",
                availableCredit = 5000000.0,
                creditLimit = 10000000.0,
                id = "pl_123456",
                interestRate = 5.5,
                nextBillingDate = "2023-12-01",
                nextDueDate = "2023-12-15",
                payLaterAccountNumber = "PL123456789",
                status = PayLaterAccountStatus.ACTIVE,
                usedCredit = 5000000.0,
                username = "john_doe",
                walletNumber = "WAL123456789"
            )
        ),


        onBackClick = {},
        onRetry = {},
        onActivePaylater = {},
        onLockPaylater = {},
        onUnlockPaylater = { },
        onNavigateToApplicationHistory = {},
        onNavigateToBillingCycleHistory = {}
    )
}