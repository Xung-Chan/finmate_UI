package com.example.ibanking_kltn.ui.screens.spending.detail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.dtos.responses.SpendingCategoryDetailResponse
import com.example.ibanking_kltn.dtos.responses.SpendingSnapshotDetailResponse
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue3
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.Red3
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.utils.CustomConfirmDialog
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.colorFromLabel
import com.example.ibanking_kltn.utils.customClick
import com.example.ibanking_kltn.utils.formatterVND
import java.math.BigDecimal


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendingCategory(
    uiState: SpendingDetailUiState,
    onBackClick: () -> Unit,
    onEvent: (SpendingDetailEvent) -> Unit
) {
    val scrollState = rememberScrollState(0)
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var isShowBottomSheet by remember {
        mutableStateOf(false)
    }

    Box {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Điều chỉnh ngân sách")
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
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        onEvent(
                            SpendingDetailEvent.ChangeVisibleAddDialog
                        )
                    },
                    containerColor = Blue3,
                    contentColor = White1,
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_regular),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                    )
                }
            },
            modifier = Modifier.systemBarsPadding(),
            containerColor = White3
        ) { paddingValues ->
            if (uiState.spendingSnapshot == null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Không có dữ liệu ngân sách",
                        style = AppTypography.bodyMedium,
                        color = Gray1
                    )
                }
                return@Scaffold
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp)
            ) {

                uiState.spendingSnapshot.spendingCategories.forEach { category ->
                    val budget = category.budgetAmount.toFloat()
                    val used = category.usedAmount.toFloat()
                    val percentUsed = if (budget > 0f) (used / budget).coerceIn(0f, 1f) else 0f
                    val percentLabel = "${(percentUsed * 100).toInt()}%"

                    val iconRes = category.categoryIcon.toIntOrNull() ?: R.drawable.airplane_service

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 30.dp,
                                shape = RoundedCornerShape(12.dp),
                                ambientColor = Black1.copy(alpha = 0.2f),
                                spotColor = Black1.copy(alpha = 0.2f)
                            )
                            .background(color = White1, shape = RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Icon + Category name
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = colorFromLabel(category.categoryName).copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = null,
                                tint = colorFromLabel(category.categoryName),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Budget/Used info and progress bar
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = category.categoryName,
                                style = AppTypography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Black1
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Ngân sách: ${formatterVND(category.budgetAmount.toLong())}đ",
                                    style = AppTypography.labelMedium,
                                    color = Gray1
                                )
                                Text(
                                    text = percentLabel,
                                    style = AppTypography.labelMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = if (percentUsed > 0.8f) Red3 else Blue3
                                )
                            }

                            Text(
                                text = "Đã dùng: ${formatterVND(category.usedAmount.toLong())}đ",
                                style = AppTypography.labelMedium,
                                color = Red3.copy(alpha = 0.8f)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .background(
                                        color = Gray1.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(percentUsed)
                                        .height(8.dp)
                                        .background(
                                            color = colorFromLabel(category.categoryName),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                )
                            }
                        }

                        // Action buttons: Edit & Delete
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconButton(
                                onClick = {
                                    ///todo
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Blue3,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(
                                onClick = {
                                    onEvent(
                                        SpendingDetailEvent.DeleteSpendingCategory(
                                            categoryCode = category.categoryCode
                                        )
                                    )
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Red3,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }


        }
        if (uiState.isShowAddCategoryDialog) {
            CustomConfirmDialog(
                dismissText = "Hủy",
                confirmText = "Thêm",
                onDismiss = {
                    onEvent(
                        SpendingDetailEvent.ChangeVisibleAddDialog
                    )
                },
                onConfirm = {
                    onEvent(
                        SpendingDetailEvent.AddSpendingCategory
                    )
                },
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)

                ) {
                    val selectedIconRes = uiState.categoryId.toIntOrNull() ?: R.drawable.unknown

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(
                                color = Blue3.copy(alpha = 0.15f),
                                shape = CircleShape
                            )
                            .customClick(
                                shape = CircleShape,
                                onClick = {
                                    isShowBottomSheet = true
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(selectedIconRes),
                            contentDescription = null,
                            tint = Blue3,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Text(
                        text = "Chọn biểu tượng",
                        style = AppTypography.labelMedium,
                        color = Gray1,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.categoryBudgetName,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text
                        ),
                        onValueChange = {
                            onEvent(
                                SpendingDetailEvent.ChangeCategoryName(it)
                            )
                        },
                        placeholder = {
                            Text(
                                "Tên danh mục", style = AppTypography.bodyMedium,
                                color = Gray2
                            )
                        },
                    )
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value =formatterVND(uiState.categoryBudget.toLong()),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                        onValueChange = {
                           onEvent(
                                SpendingDetailEvent.ChangeCategoryBudget(it)
                           )
                        },
                        placeholder = {
                            Text(
                                "Ngân sách", style = AppTypography.bodyMedium,
                                color = Gray2
                            )
                        },
                    )
                }
            }
        }
        if (isShowBottomSheet) {
            ModalBottomSheet(
                containerColor = White3,
                onDismissRequest = { isShowBottomSheet = false },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp)
                ) {
                    // Header
                    Text(
                        text = "Chọn danh mục chi tiêu",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Black1,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Divider
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Gray1.copy(alpha = 0.2f))
                            .padding(bottom = 16.dp)
                    )

                    // Category Grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .height(400.dp)
                            .padding(top = 16.dp)
                    ) {
                        items(items = uiState.definedCategories.filter{
                            !uiState.spendingSnapshot!!.spendingCategories.map { sc->sc.categoryCode }.contains(it.code)
                        }) { category ->
                            val iconRes = category.icon.toIntOrNull() ?: R.drawable.unknown
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .customClick(
                                        shape = RoundedCornerShape(12.dp),
                                        onClick = {
                                            onEvent(
                                                SpendingDetailEvent.ChangeCategoryIcon(category.icon)
                                            )
                                            isShowBottomSheet = false
                                        }
                                    )
                                    .padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Icon container
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(
                                            color = colorFromLabel(category.name).copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = iconRes),
                                        contentDescription = null,
                                        tint = colorFromLabel(category.name),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                // Category name
                                Text(
                                    text = category.name,
                                    style = AppTypography.labelSmall,
                                    color =  Black1,
                                    maxLines = 2,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewCategorySpending() {
    SpendingCategory(
        uiState = SpendingDetailUiState(
            spendingSnapshot = SpendingSnapshotDetailResponse(
                id = "1",
                snapshotName = "Chi tiêu tháng 6",
                budgetAmount = BigDecimal(5000000),
                usedAmount = BigDecimal(3500000),
                monthlySpending = "8/2025",
                spendingCategories = listOf(
                    SpendingCategoryDetailResponse(
                        categoryName = "Ăn uống",
                        categoryCode = "food",
                        categoryIcon = "",
                        textColor = "TODO()",
                        backgroundColor = "TODO()",
                        budgetAmount = BigDecimal(5000000),
                        usedAmount = BigDecimal(3500000),
                    ),
                    SpendingCategoryDetailResponse(
                        categoryName = "Đi lại",
                        categoryCode = "food",
                        categoryIcon = "",
                        textColor = "TODO()",
                        backgroundColor = "TODO()",
                        budgetAmount = BigDecimal(5000000),
                        usedAmount = BigDecimal(3500000),
                    ),
                    SpendingCategoryDetailResponse(
                        categoryName = "Du lịch",
                        categoryCode = "food",
                        categoryIcon = "",
                        textColor = "TODO()",
                        backgroundColor = "TODO()",
                        budgetAmount = BigDecimal(5000000),
                        usedAmount = BigDecimal(3500000),
                    ),
                    SpendingCategoryDetailResponse(
                        categoryName = "Ăn uống",
                        categoryCode = "food",
                        categoryIcon = "",
                        textColor = "TODO()",
                        backgroundColor = "TODO()",
                        budgetAmount = BigDecimal(5000000),
                        usedAmount = BigDecimal(3500000),
                    ),

                ),
            ),
        ),
        onBackClick = {},
        onEvent = {}
    )
}
