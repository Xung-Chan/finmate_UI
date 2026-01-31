package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.dtos.definitions.TransactionStatus
import com.example.ibanking_kltn.dtos.responses.TransactionHistoryResponse
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Green1
import com.example.ibanking_kltn.ui.theme.Orange1
import com.example.ibanking_kltn.ui.theme.Red1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.utils.DashedDivider
import com.example.ibanking_kltn.utils.formatterDateTimeString
import com.example.ibanking_kltn.utils.formatterVND
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    transaction: TransactionHistoryResponse?,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.TransactionDetail_Title))
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
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Black1, containerColor = White3
                ),
            )
        }, modifier = Modifier.systemBarsPadding(), containerColor = White3
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            if (transaction == null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Lỗi tải thông tin giao dịch",
                        style = AppTypography.bodySmall,
                        color = Red1
                    )
                }
                return@Column
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 30.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Black1.copy(alpha = 0.25f),
                        spotColor = Black1.copy(alpha = 0.25f)
                    )
                    .background(color = White1, shape = RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "Mã giao dịch",
                            style = AppTypography.bodyMedium,
                            color = Gray1

                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = transaction.id,
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tài khoản nguồn",
                            style = AppTypography.bodyMedium,
                            color = Gray1,

                            )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = transaction.sourceAccountNumber,
                            style = AppTypography.bodySmall,
                            color = Black1,
                        )
                    }
                }
                if( transaction.toWalletNumber != null){

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tài khoản đích",
                            style = AppTypography.bodyMedium,
                            color = Gray1,

                            )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = transaction.toWalletNumber,
                            style = AppTypography.bodySmall,
                            color = Black1,
                        )
                    }
                }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Nội dung",
                            style = AppTypography.bodyMedium,
                            color = Gray1,

                            )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = transaction.description,
                            style = AppTypography.bodySmall,
                            color = Black1,
                            textAlign = TextAlign.End
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ngày",
                            style = AppTypography.bodyMedium,
                            color = Gray1,

                            )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = formatterDateTimeString(
                                LocalDateTime.parse(transaction.processedAt)
                            ),
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Dịch vụ",
                            style = AppTypography.bodyMedium,
                            color = Gray1,

                            )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = transaction.transactionType.serviceName,
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
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = when (transaction.status) {
                                TransactionStatus.COMPLETED -> "Thành công"
                                TransactionStatus.PENDING -> "Đang xử lý"
                                TransactionStatus.CANCELED -> "Đã hủy"
                                else -> "Thất bại"
                            },
                            style = AppTypography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = when (transaction.status) {
                                TransactionStatus.COMPLETED -> Green1
                                TransactionStatus.PENDING -> Orange1
                                else -> Red1
                            },
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
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tổng cộng",
                            style =AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Black1,

                            )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${formatterVND(transaction.amount.toLong())} VND",
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Black1,
                        )
                    }
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
//fun TransactionDetailPreview() {
//    TransactionDetailScreen(
//        transaction = TransactionHistoryResponse(
//            id = "TX123456789",
//            amount = 1500000.0,
//            description = "Chuyển tiền cho Nguyễn Văn A",
//            processedAt = "2024-06-15T10:30:00",
//            sourceAccountNumber = "1234567890",
//            sourceBalanceUpdated = 5000000.0,
//            status = "COMPLETED",
//            toWalletNumber = "0987654321",
//            transactionType = ServiceType.TRANSFER
//        ),
//        onBackClick = {}
//    )
//}
