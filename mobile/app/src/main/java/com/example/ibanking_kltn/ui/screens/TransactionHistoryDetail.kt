package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.dtos.definitions.ServiceType
import com.example.ibanking_kltn.dtos.definitions.TransactionStatus
import com.example.ibanking_kltn.dtos.responses.TransactionHistoryResponse
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue3
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.Green1
import com.example.ibanking_kltn.ui.theme.Green2
import com.example.ibanking_kltn.ui.theme.Orange1
import com.example.ibanking_kltn.ui.theme.Red1
import com.example.ibanking_kltn.ui.theme.Red3
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
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
                    Text(
                        text = stringResource(R.string.TransactionDetail_Title),
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
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
                    titleContentColor = Black1,
                    containerColor = White3
                ),
            )
        },
        modifier = Modifier.systemBarsPadding(),
        containerColor = White3
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
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

            Spacer(modifier = Modifier.height(20.dp))

            // Status Card with Icon
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Black1.copy(alpha = 0.1f),
                        spotColor = Black1.copy(alpha = 0.1f)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = when (transaction.status) {
                        TransactionStatus.COMPLETED -> Green2.copy(alpha = 0.1f)
                        TransactionStatus.PENDING -> Orange1.copy(alpha = 0.1f)
                        TransactionStatus.CANCELED -> Gray2.copy(alpha = 0.1f)
                        else -> Red3.copy(alpha = 0.1f)
                    }
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Status Icon
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                color = when (transaction.status) {
                                    TransactionStatus.COMPLETED -> Green1.copy(alpha = 0.2f)
                                    TransactionStatus.PENDING -> Orange1.copy(alpha = 0.2f)
                                    TransactionStatus.CANCELED -> Gray1.copy(alpha = 0.2f)
                                    else -> Red1.copy(alpha = 0.2f)
                                },
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (transaction.status) {
                                TransactionStatus.COMPLETED -> Icons.Default.CheckCircle
                                TransactionStatus.PENDING -> Icons.Default.Schedule
                                TransactionStatus.CANCELED -> Icons.Default.Cancel
                                else -> Icons.Default.Error
                            },
                            contentDescription = null,
                            tint = when (transaction.status) {
                                TransactionStatus.COMPLETED -> Green1
                                TransactionStatus.PENDING -> Orange1
                                TransactionStatus.CANCELED -> Gray1
                                else -> Red1
                            },
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // Status Text
                    Text(
                        text = when (transaction.status) {
                            TransactionStatus.COMPLETED -> "Giao dịch thành công"
                            TransactionStatus.PENDING -> "Đang xử lý"
                            TransactionStatus.CANCELED -> "Đã hủy"
                            else -> "Giao dịch thất bại"
                        },
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = when (transaction.status) {
                            TransactionStatus.COMPLETED -> Green1
                            TransactionStatus.PENDING -> Orange1
                            TransactionStatus.CANCELED -> Gray1
                            else -> Red1
                        }
                    )

                    // Amount
                    Text(
                        text = "${formatterVND(transaction.amount.toLong())} VND",
                        style = AppTypography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        ),
                        color = Black1
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Transaction Details Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Black1.copy(alpha = 0.1f),
                        spotColor = Black1.copy(alpha = 0.1f)
                    ),
                colors = CardDefaults.cardColors(containerColor = White1),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Thông tin giao dịch",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Black1
                    )

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Gray2.copy(alpha = 0.3f)
                    )

                    // Transaction ID
                    TransactionInfoRow(
                        label = "Mã giao dịch",
                        value = transaction.id,
                        icon = R.drawable.card
                    )

                    // Service Type
                    TransactionInfoRow(
                        label = "Dịch vụ",
                        value = transaction.transactionType.serviceName,
                        icon = R.drawable.category
                    )

                    // Source Account
                    TransactionInfoRow(
                        label = "Tài khoản nguồn",
                        value = transaction.sourceAccountNumber,
                        icon = R.drawable.wallet
                    )

                    // Destination Account (if exists)
                    if (transaction.toWalletNumber != null) {
                        TransactionInfoRow(
                            label = "Tài khoản đích",
                            value = transaction.toWalletNumber,
                            icon = R.drawable.wallet_regular
                        )
                    }

                    // Description
                    TransactionInfoRow(
                        label = "Nội dung",
                        value = transaction.description,
                        icon = R.drawable.note,
                        isMultiLine = true
                    )

                    // Date
                    TransactionInfoRow(
                        label = "Thời gian",
                        value = formatterDateTimeString(
                            LocalDateTime.parse(transaction.processedAt)
                        ),
                        icon = R.drawable.calendar
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun TransactionInfoRow(
    label: String,
    value: String,
    icon: Int,
    isMultiLine: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = if (isMultiLine) Alignment.Top else Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Blue3.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Blue3,
                modifier = Modifier.size(20.dp)
            )
        }

        // Label and Value
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = AppTypography.labelMedium,
                color = Gray1
            )
            Text(
                text = value,
                style = AppTypography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Black1,
                textAlign = if (isMultiLine) TextAlign.Start else TextAlign.End
            )
        }
    }
}

@Preview(
    showBackground = true, showSystemUi = true

)
@Composable
fun TransactionDetailPreview() {
    TransactionDetailScreen(
        transaction = TransactionHistoryResponse(
            id = "TX123456789",
            amount = 1500000.0,
            description = "Chuyển tiền cho Nguyễn Văn A",
            processedAt = "2024-06-15T10:30:00",
            sourceAccountNumber = "1234567890",
            sourceBalanceUpdated = 5000000.0,
            status = TransactionStatus.COMPLETED,
            toWalletNumber = "0987654321",
            transactionType = ServiceType.TRANSFER
        ),
        onBackClick = {}
    )
}

