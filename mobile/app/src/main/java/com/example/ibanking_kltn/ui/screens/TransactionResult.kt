package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.data.dtos.TransactionStatus
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Green1
import com.example.ibanking_kltn.ui.theme.Red1
import com.example.ibanking_kltn.ui.theme.WarningColor
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.TransactionResultUiState
import com.example.ibanking_kltn.utils.CustomTextButton
import com.example.ibanking_kltn.utils.formatterVND

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionResultScreen(
    uiState: TransactionResultUiState,
    onBackToHomeClick: () -> Unit,
    onContactClick: () -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            TextButton(
                onClick = {
                    onContactClick()
                }
            ) {
                Row(
                    modifier = Modifier
                        .background(
                            color = White1,
                            shape = RoundedCornerShape(percent = 30)
                        )
                        .border(
                            width = 2.dp,
                            color = Blue1,
                            shape = RoundedCornerShape(percent = 30)
                        )
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.support),
                        contentDescription = null,
                        tint = Blue1,
                        modifier = Modifier.size(30.dp)

                    )
                    Text(
                        text = "Liên hệ hỗ trợ",
                        style = CustomTypography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Blue1
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.Home))
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = null,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Black1, containerColor = White3
                ),
            )
        }, modifier = Modifier.systemBarsPadding(), containerColor = White3
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.transfer_successfully),
                    contentDescription = null,
                    modifier = Modifier
                        .height(170.dp)
                        .fillMaxWidth()
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                when (uiState.status) {
                    TransactionStatus.COMPLETED -> {
                        Icon(
                            painter = painterResource(id = R.drawable.completed),
                            contentDescription = null,
                            tint = Green1,
                            modifier = Modifier.size(70.dp)
                        )
                    }

                    TransactionStatus.PENDING -> {
                        Icon(
                            painter = painterResource(id = R.drawable.processing),
                            contentDescription = null,
                            tint = WarningColor,
                            modifier = Modifier.size(70.dp)
                        )
                    }

                    else -> {
                        Icon(
                            painter = painterResource(id = R.drawable.error),
                            contentDescription = null,
                            tint = Red1,
                            modifier = Modifier.size(70.dp)
                        )
                    }
                }
                Text(
                    text = when (uiState.status) {
                        TransactionStatus.COMPLETED -> {
                            "Giao dịch thành công"
                        }

                        TransactionStatus.PENDING -> {
                            "Giao dich đang xử lý"
                        }

                        TransactionStatus.FAILED -> {
                            "Giao dịch thất bại"
                        }

                        TransactionStatus.CANCELED -> {
                            "Đã hủy"
                        }
                    }, style = CustomTypography.titleLarge, color = Blue1
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row {
                        Text(
                            text = "Dịch vụ: ", style = CustomTypography.bodyMedium,
                            color = Gray1
                        )
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = uiState.service,
                                style = CustomTypography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Black1
                            )

                        }
                    }

                    Row {
                        Text(
                            text = "Tên người thụ hưởng: ", style = CustomTypography.bodyMedium,
                            color = Gray1
                        )
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = uiState.toMerchantName,
                                style = CustomTypography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Black1
                            )

                        }
                    }
                    Row {
                        Text(
                            text = "Số tiền: ", style = CustomTypography.bodyMedium,
                            color = Gray1
                        )
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "${formatterVND(uiState.amount)} VND",
                                style = CustomTypography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = when (uiState.status) {

                                    TransactionStatus.COMPLETED -> {
                                        Green1
                                    }

                                    TransactionStatus.PENDING -> {
                                        WarningColor
                                    }

                                    else -> {

                                        Red1
                                    }
                                }

                            )

                        }
                    }
                    Row {
                        Text(
                            text = "Trạng thái: ", style = CustomTypography.bodyMedium,
                            color = Gray1
                        )
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = when (uiState.status) {
                                    TransactionStatus.COMPLETED -> {
                                        "Thành công"
                                    }


                                    TransactionStatus.PENDING -> {
                                        "Chờ"
                                    }

                                    TransactionStatus.FAILED -> {
                                        "Thất bại"
                                    }

                                    TransactionStatus.CANCELED -> {
                                        "Đã hủy"
                                    }
                                },
                                style = CustomTypography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = when (uiState.status) {

                                    TransactionStatus.COMPLETED -> {
                                        Green1
                                    }

                                    TransactionStatus.PENDING -> {
                                        WarningColor
                                    }

                                    else -> {

                                        Red1
                                    }
                                }

                            )

                        }
                    }

                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    CustomTextButton(
                        onClick = {
                            onBackToHomeClick()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        style = CustomTypography.titleMedium,
                        text = stringResource(id = R.string.BackToHome),
                        enable = true
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
fun TransferSuccessfullyPreview() {
    TransactionResultScreen(
        onBackToHomeClick = {},
        uiState = TransactionResultUiState(
            amount = 1000000L,
            toMerchantName = "Nguyễn Văn A",
            service = "Chuyển tiền",
            status = TransactionStatus.COMPLETED
        ),
        onContactClick = {}
    )
}