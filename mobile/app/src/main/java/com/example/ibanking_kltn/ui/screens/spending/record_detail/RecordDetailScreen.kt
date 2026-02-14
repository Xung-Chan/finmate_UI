package com.example.ibanking_kltn.ui.screens.spending.record_detail

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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.dtos.definitions.SpendingRecordType
import com.example.ibanking_kltn.dtos.responses.SpendingRecordResponse
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue3
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.Green1
import com.example.ibanking_kltn.ui.theme.Green2
import com.example.ibanking_kltn.ui.theme.Red1
import com.example.ibanking_kltn.ui.theme.Red3
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.utils.formatterDateTimeString
import com.example.ibanking_kltn.utils.formatterVND
import java.math.BigDecimal
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordDetailScreen(
    record: SpendingRecordResponse,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chi tiết bản ghi chi tiêu",
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
                    containerColor = when (record.recordType) {
                        SpendingRecordType.INCOME -> Green2.copy(alpha = 0.1f)
                        SpendingRecordType.EXPENSE -> Red3.copy(alpha = 0.1f)
                        null -> Gray2.copy(alpha = 0.1f)
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
                    // Record Type Icon
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                color = when (record.recordType) {
                                    SpendingRecordType.INCOME -> Green1.copy(alpha = 0.2f)
                                    SpendingRecordType.EXPENSE -> Red1.copy(alpha = 0.2f)
                                    null -> Gray1.copy(alpha = 0.2f)
                                },
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (record.recordType) {
                                SpendingRecordType.INCOME -> Icons.Default.ArrowDownward
                                SpendingRecordType.EXPENSE -> Icons.Default.ArrowUpward
                                null -> Icons.Default.ArrowUpward
                            },
                            contentDescription = null,
                            tint = when (record.recordType) {
                                SpendingRecordType.INCOME -> Green1
                                SpendingRecordType.EXPENSE -> Red1
                                null -> Gray1
                            },
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // Record Type Text
                    Text(
                        text = when (record.recordType) {
                            SpendingRecordType.INCOME -> "Khoản thu"
                            SpendingRecordType.EXPENSE -> "Khoản chi"
                            null -> "Không xác định"
                        },
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = when (record.recordType) {
                            SpendingRecordType.INCOME -> Green1
                            SpendingRecordType.EXPENSE -> Red1
                            null -> Gray1
                        }
                    )

                    // Amount
                    Text(
                        text = "${
                            when (record.recordType) {
                                SpendingRecordType.INCOME -> "+"
                                SpendingRecordType.EXPENSE -> "-"
                                null -> ""
                            }
                        }${formatterVND(record.amount.toLong())} VND",
                        style = AppTypography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        ),
                        color = when (record.recordType) {
                            SpendingRecordType.INCOME -> Green1
                            SpendingRecordType.EXPENSE -> Red1
                            null -> Black1
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Category Card (if exists)
            if (record.categoryName != null) {
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
                            text = "Danh mục",
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Black1
                        )

                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Gray2.copy(alpha = 0.3f)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Category Icon
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = try {
                                            Color(record.categoryBackgroundColor?.toColorInt() ?: 0xFF3629B7.toInt())
                                        } catch (_: Exception) {
                                            Blue3.copy(alpha = 0.1f)
                                        },
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = record.categoryIcon ?: "📦",
                                    style = AppTypography.titleLarge,
                                    fontSize = 24.sp
                                )
                            }

                            // Category Name
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = record.categoryName,
                                    style = AppTypography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Black1
                                )
                                Text(
                                    text = record.categoryCode ?: "Không phân loại",
                                    style = AppTypography.labelMedium,
                                    color = Gray1
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Record Details Card
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
                        text = "Thông tin chi tiết",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Black1
                    )

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Gray2.copy(alpha = 0.3f)
                    )

                    // Record ID
                    RecordInfoRow(
                        label = "Mã bản ghi",
                        value = record.id ?: "Không xác định",
                        icon = R.drawable.card
                    )

                    // Transaction ID
                    RecordInfoRow(
                        label = "Mã giao dịch",
                        value = record.transactionId ?: "Không xác định",
                        icon = R.drawable.card
                    )

                    // Destination Account Name
                    RecordInfoRow(
                        label = "Tên người nhận/gửi",
                        value = record.destinationAccountName,
                        icon = R.drawable.contact
                    )

                    // Destination Account Number
                    RecordInfoRow(
                        label = "Số tài khoản",
                        value = record.destinationAccountNumber,
                        icon = R.drawable.wallet
                    )

                    // Description
                    RecordInfoRow(
                        label = "Nội dung",
                        value = record.description,
                        icon = R.drawable.note,
                        isMultiLine = true
                    )

                    // Date
                    RecordInfoRow(
                        label = "Thời gian",
                        value = formatterDateTimeString(
                            LocalDateTime.parse(record.occurredAt)
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
private fun RecordInfoRow(
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecordDetailScreenPreview() {
    RecordDetailScreen(
        record = SpendingRecordResponse(
            id = "rec_123456",
            snapshotId = "snap_789",
            transactionId = "txn_456789",
            amount = BigDecimal("250000"),
            description = "Thanh toán tiền điện tháng 2/2026",
            destinationAccountName = "Công ty Điện lực Hà Nội",
            destinationAccountNumber = "0123456789",
            recordType = SpendingRecordType.EXPENSE,
            categoryCode = "UTIL",
            categoryName = "Tiện ích",
            categoryIcon = "⚡",
            categoryTextColor = "#FF9800",
            categoryBackgroundColor = "#FFF3E0",
            occurredAt = LocalDateTime.now().toString()
        ),
        onBackClick = {}
    )
}