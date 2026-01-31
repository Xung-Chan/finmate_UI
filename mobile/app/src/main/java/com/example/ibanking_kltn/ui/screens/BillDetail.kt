package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.dtos.definitions.BillPayload
import com.example.ibanking_kltn.dtos.definitions.BillStatus
import com.example.ibanking_kltn.dtos.responses.BillResponse
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Green1
import com.example.ibanking_kltn.ui.theme.Orange1
import com.example.ibanking_kltn.ui.theme.Red1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.BillDetailUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.DashedDivider
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.QrCodeImage
import com.example.ibanking_kltn.utils.customClick
import com.example.ibanking_kltn.utils.formatterDateString
import com.example.ibanking_kltn.utils.formatterVND
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillDetailScreen(
    uiState: BillDetailUiState,
    onSavedSuccess: () -> Unit,
    onBackClick: () -> Unit,
    onCancelBill: () -> Unit
) {
    LoadingScaffold(
        isLoading = uiState.screenState == StateType.LOADING
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Chi tiết hóa đơn")
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
            if (uiState.bill == null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Không tìm thấy hóa đơn",
                        style = AppTypography.bodyMedium,
                        color = Gray1,
                    )
                }
            } else {

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 20.dp)
                ) {
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
                            horizontalArrangement = Arrangement.Center
                        ) {

                            QrCodeImage(
                                content = BillPayload(
                                    billCode = uiState.bill.qrIdentifier,
                                ),
                                size = 200.dp,
                                onSavedSuccess = {
                                    onSavedSuccess()
                                },
                            )

                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)

                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Mã hóa đơn",
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
                                    text = uiState.bill.qrIdentifier,
                                    style = AppTypography.bodySmall,
                                    color = Black1,
                                    textAlign = TextAlign.End

                                )
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
                                    text = "Tên người thụ hưởng",
                                    style = AppTypography.bodyMedium,
                                    color = Gray1,
                                    textAlign = TextAlign.End

                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = uiState.bill.merchantName,
                                    style = AppTypography.bodySmall,
                                    color = Black1,
                                )
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
                                    text = "Số tài khoản thụ hưởng",
                                    style = AppTypography.bodyMedium,
                                    color = Gray1,
                                    textAlign = TextAlign.End

                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = uiState.bill.walletNumber,
                                    style = AppTypography.bodySmall,
                                    color = Black1,
                                )
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
                                    text = uiState.bill.description,
                                    style = AppTypography.bodySmall,
                                    color = Black1,
                                    textAlign = TextAlign.End
                                )
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
                                    text = "Ngày hết hạn",
                                    style = AppTypography.bodyMedium,
                                    color = Gray1,
                                    textAlign = TextAlign.End

                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = formatterDateString(
                                        LocalDateTime.parse(uiState.bill.dueDate).toLocalDate()
                                    ),
                                    style = AppTypography.bodySmall,
                                    color = Black1,
                                )
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
                                    text = "Trạng thái",
                                    style = AppTypography.bodyMedium,
                                    color = Gray1,
                                    textAlign = TextAlign.End

                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = uiState.bill.billStatus.status,
                                    style = AppTypography.bodyMedium,
                                    color = when (uiState.bill.billStatus) {
                                        BillStatus.ACTIVE -> Orange1
                                        BillStatus.PAID -> Green1
                                        else -> Gray1
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
                                    text = "Số tiền",
                                    style = AppTypography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
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
                                    text = "${
                                        formatterVND(uiState.bill.amount)
                                    } VND",
                                    style = AppTypography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Black1,
                                )
                            }
                        }
                        if (uiState.bill.billStatus == BillStatus.ACTIVE) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = Red1,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .customClick(shape = RoundedCornerShape(12.dp)) {
                                        onCancelBill()
                                    }
                                    .padding(vertical = 5.dp),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    text = "Hủy hóa đơn",
                                    style = AppTypography.bodyMedium,
                                    color = Red1

                                )

                            }
                        }

                    }


                }

            }
        }
    }
}

@Preview(
    showBackground = true, showSystemUi = true

)
@Composable
fun BillDetailPreview() {
    BillDetailScreen(
        uiState = BillDetailUiState(
            bill = BillResponse(
                merchantName = "Công ty ABC",
                amount = 250000,
                dueDate = "2024-10-15T14:30:00",
                billStatus = BillStatus.ACTIVE,
                description = "Thanh toán dịch vụ Internet tháng 12",
                metadata = mapOf("orderId" to "ORD-123456"),
                qrIdentifier = "01KCNM2ZWMGEFHK4QJFBSC6K7Z",
                walletNumber = "0987654321"
            )
        ),
        onSavedSuccess = {},
        onBackClick = {},
        onCancelBill = {}

    )
}
