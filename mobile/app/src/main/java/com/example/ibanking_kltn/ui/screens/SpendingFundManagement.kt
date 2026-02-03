package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.dtos.responses.SpendingSnapshotResponse
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue6
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray3
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.SpendingFundState
import com.example.ibanking_kltn.ui.uistates.SpendingFundUiState
import com.example.ibanking_kltn.utils.CustomClickField
import com.example.ibanking_kltn.utils.CustomConfirmDialog
import com.example.ibanking_kltn.utils.CustomDatePicker
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.colorFromLabel
import com.example.ibanking_kltn.utils.formatterDateString
import com.example.ibanking_kltn.utils.formatterVND
import java.math.BigDecimal
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendingFundManagement(
    uiState: SpendingFundUiState = SpendingFundUiState(),
    onBackClick: () -> Unit = {},

    ) {
    var isShowAddDialog by remember { mutableStateOf(false) }
    var isShowMonthlyPicker by remember { mutableStateOf(false) }
    var spendingSnapshotname by remember { mutableStateOf("") }
    var spendingBudget by remember { mutableLongStateOf(0L) }
    var monthlySpending by remember { mutableStateOf(LocalDate.now()) }

    Box {


        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Quỹ")
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
                        containerColor = Blue6.copy(
                            alpha = 0.3f
                        )
                    ),
                )
            },
            modifier = Modifier.systemBarsPadding(),
            containerColor = White3
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            ) {
                Image(
                    painter = painterResource(R.drawable.spending_fund_illustration),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = Black1,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(
                                    R.drawable.add_bold
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Tạo quỹ mới",
                                style = AppTypography.bodySmall
                            )
                        }
                    }
                    PullToRefreshBox(
                        isRefreshing = uiState.screenState == SpendingFundState.LOADING,
                        onRefresh = {
                            //todo
                        }
                    ) {
                        LazyColumn {
                            items(
                                items = uiState.spendingFunds,
                            ) { spending ->
                                SpendingSnapshotCompose(
                                    spending = spending,
                                    modifier = Modifier
                                        .padding(vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        if (isShowAddDialog) {
            CustomConfirmDialog(
                dimissText = "Hủy",
                confirmText = "Xác nhận",
                onDimiss = {
                    isShowAddDialog = false
                },
                onConfirm = {
                    //todo
                    isShowAddDialog = false
                }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row {
                        Text(
                            text = "Tên quỹ",
                            style = AppTypography.bodyMedium,
                            color = Gray1
                        )
                    }
                    CustomTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = spendingSnapshotname,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text
                        ),
                        enable = true,
                        onValueChange = {
                            spendingSnapshotname = it
                        }
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row {
                        Text(
                            text = "Số tiền",
                            style = AppTypography.bodyMedium,
                            color = Gray1
                        )
                    }
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = formatterVND(spendingBudget),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text
                        ),
                        enable = true,
                        onValueChange = {
                            val formatAmount = it
                                .replace(".", "")
                                .replace(",", "")
                            spendingBudget = if (formatAmount == "") {
                                0L
                            } else {
                                formatAmount.toLong()
                            }
                        }
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row {
                        Text(
                            text = "Chọn tháng",
                            style = AppTypography.bodyMedium,
                            color = Gray1
                        )
                    }
                    CustomClickField(
                        onClick = {
                            isShowMonthlyPicker = true
                        },
                        placeholder = "Chọn tháng",
                        value = formatterDateString(monthlySpending, "MM/yyyy"),
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.calendar),
                                tint = Gray1,
                                contentDescription = null,
                                modifier = Modifier.size(25.dp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }


            }

        }
        if (isShowMonthlyPicker) {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Gray3.copy(alpha = 0.5f),
                    )
                    .padding(20.dp)
                    .pointerInput(Unit) {}) {
                CustomDatePicker(
                    minDate = LocalDate.now().plusDays(1),
                    maxDate = LocalDate.now().plusYears(2),
                    onSelectedDate = {
                        monthlySpending = it
                        isShowMonthlyPicker = false
                    },
                    onDismiss = {
                        isShowMonthlyPicker = false
                    }
                )

            }
        }
    }
}

@Composable
private fun SpendingSnapshotCompose(
    spending: SpendingSnapshotResponse,
    modifier: Modifier = Modifier
) {
    val color = colorFromLabel(
        label = spending.snapshotName + spending.id
    )
    val cornerShape = RoundedCornerShape(15.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 30.dp,
                shape = cornerShape,
                ambientColor = Black1.copy(alpha = 0.25f),
                spotColor = Black1.copy(alpha = 0.25f)
            )
            .border(
                width = 1.dp,
                color = color,
                shape = cornerShape
            )
            .background(
                color = color.copy(
                    alpha = 0.1f
                ), shape = cornerShape
            )
            .padding(
                vertical = 15.dp,
                horizontal = 20.dp
            ),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = spending.snapshotName,
                style = AppTypography.bodyMedium,
                color = Black1,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = spending.monthlySpending,
                style = AppTypography.bodySmall,
                color = Black1
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = formatterVND(spending.budgetAmount.toLong()) + " đ",
                style = AppTypography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Black1
            )
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun SpendingFundPreview() {
    SpendingFundManagement(
        uiState = SpendingFundUiState(
            screenState = SpendingFundState.NONE,
            spendingFunds = listOf(
                SpendingSnapshotResponse(
                    id = "1",
                    monthlySpending = "8/2025",
                    snapshotName = "Quỹ du lịch",
                    description = "Dành dụm cho chuyến du lịch cuối năm",
                    usedAmount = BigDecimal("2000000"),
                    budgetAmount = BigDecimal("5000000")
                ),
                SpendingSnapshotResponse(
                    id = "2",
                    monthlySpending = "5/2025",
                    snapshotName = "Quỹ mua sắm",
                    description = "Dành dụm cho việc mua sắm cá nhân",
                    usedAmount = BigDecimal("1500000"),
                    budgetAmount = BigDecimal("3000000")
                ),
                SpendingSnapshotResponse(
                    id = "3",
                    monthlySpending = "9/2025",
                    snapshotName = "Quỹ học tập",
                    description = "Dành dụm cho việc học tập và phát triển bản thân",
                    usedAmount = BigDecimal("1000000"),
                    budgetAmount = BigDecimal("4000000")
                )
            )
        )
    )
}

