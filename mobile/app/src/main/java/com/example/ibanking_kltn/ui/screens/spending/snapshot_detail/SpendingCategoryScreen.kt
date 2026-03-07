package com.example.ibanking_kltn.ui.screens.spending.snapshot_detail

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.ibanking_kltn.dtos.definitions.CategoryIcon
import com.example.ibanking_kltn.dtos.responses.SpendingCategoryDetailResponse
import com.example.ibanking_kltn.dtos.responses.SpendingSnapshotDetailResponse
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue3
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.Red1
import com.example.ibanking_kltn.ui.theme.Red3
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.utils.CustomConfirmDialog
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.customClick
import com.example.ibanking_kltn.utils.formatterVND
import com.example.ibanking_kltn.utils.toColorFromHex
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
    val isShowBottomSheet = uiState.isShowCategoryIconBottomSheet
    var categoryToDelete by remember {
        mutableStateOf<String?>(null)
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Recommend FAB
                    FloatingActionButton(
                        onClick = {
                            onEvent(SpendingDetailEvent.ShowRecommendInputDialog)
                        },
                        containerColor = White1,
                        contentColor = Blue3,
                        shape = CircleShape
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ai),
                            contentDescription = "Recommend",
                            modifier = Modifier.size(25.dp),
                        )
                    }
                    // Add FAB
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
                if (uiState.spendingSnapshot.spendingCategories.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Chưa có danh mục chi tiêu nào. Nhấn nút + để thêm danh mục.",
                            style = AppTypography.bodyMedium,
                            color = Gray1,
                            modifier = Modifier.padding(top = 20.dp)
                        )
                    }
                }
                uiState.spendingSnapshot.spendingCategories.forEach { category ->
                    val budget = category.budgetAmount.toFloat()
                    val used = category.usedAmount.toFloat()
                    val isOverBudget = used > budget && budget > 0f
                    val percentRaw = if (budget > 0f) (used / budget) else 0f
                    val percentUsed = percentRaw.coerceIn(0f, 1f)
                    val percentLabel = "${(percentRaw * 100).toInt()}%"

                    val iconRes = CategoryIcon.fromCode(category.categoryIcon).resId
                    val color = category.textColor.toColorFromHex()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 30.dp,
                                shape = RoundedCornerShape(12.dp),
                                ambientColor = if (isOverBudget) Red3.copy(alpha = 0.25f) else Black1.copy(alpha = 0.2f),
                                spotColor = if (isOverBudget) Red3.copy(alpha = 0.25f) else Black1.copy(alpha = 0.2f)
                            )
                            .background(
                                color = if (isOverBudget) Red3.copy(alpha = 0.04f) else White1,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .then(
                                if (isOverBudget) Modifier.padding(start = 4.dp) else Modifier
                            )
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Icon + Category name
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = color.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = null,
                                tint = color,
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
                                    color = if (isOverBudget || percentRaw > 0.8f) Red3 else Blue3
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
                                            color = if (isOverBudget) Red3 else color,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                )
                            }

                            // Over-budget warning chip
                            if (isOverBudget) {
                                Row(
                                    modifier = Modifier
                                        .background(
                                            color = Red3.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = Red3,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "Vượt ngân sách ${formatterVND((category.usedAmount - category.budgetAmount).toLong())}đ",
                                        style = AppTypography.labelSmall.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = Red3
                                    )
                                }
                            }
                        }

                        // Action buttons: Edit & Delete
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconButton(
                                onClick = {
                                    onEvent(
                                        SpendingDetailEvent.ShowEditDialog(
                                            categoryCode = category.categoryCode
                                        )
                                    )
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
                                    categoryToDelete = category.categoryCode
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
                    val selectedIconRes = CategoryIcon.fromCode(uiState.selectedIcon).resId
                    val color = uiState.selectedIconColor.toColorFromHex()
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(
                                color = color.copy(alpha = 0.15f),
                                shape = CircleShape
                            )
                            .customClick(
                                shape = CircleShape,
                                onClick = {
                                    onEvent(SpendingDetailEvent.ShowCategoryIconBottomSheet)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(selectedIconRes),
                            contentDescription = null,
                            tint = color,
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
                        value = formatterVND(uiState.categoryBudget.toLong()),
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
                    if (uiState.errorMessage != null) {
                        androidx.compose.foundation.layout.Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Red1.copy(alpha = 0.1f),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.error),
                                contentDescription = null,
                                tint = Red1,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = uiState.errorMessage,
                                style = AppTypography.bodySmall,
                                color = Red1
                            )
                        }
                    }
                }
            }
        }
        if (uiState.isShowEditCategoryDialog) {
            CustomConfirmDialog(
                dismissText = "Hủy",
                confirmText = "Cập nhật",
                onDismiss = {
                    onEvent(
                        SpendingDetailEvent.ChangeVisibleEditDialog
                    )
                },
                onConfirm = {
                    onEvent(
                        SpendingDetailEvent.UpdateSpendingCategory
                    )
                },
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)

                ) {
                    val selectedIconRes = CategoryIcon.fromCode(uiState.selectedIcon).resId
                    val color = uiState.selectedIconColor.toColorFromHex()
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(
                                color = color.copy(alpha = 0.15f),
                                shape = CircleShape
                            )
                            .customClick(
                                shape = CircleShape,
                                onClick = {
                                    onEvent(SpendingDetailEvent.ShowCategoryIconBottomSheet)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(selectedIconRes),
                            contentDescription = null,
                            tint = color,
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
                        value = formatterVND(uiState.categoryBudget.toLong()),
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
                    if (uiState.errorMessage != null) {
                        androidx.compose.foundation.layout.Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Red1.copy(alpha = 0.1f),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.error),
                                contentDescription = null,
                                tint = Red1,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = uiState.errorMessage,
                                style = AppTypography.bodySmall,
                                color = Red1
                            )
                        }
                    }
                }
            }
        }
        if (isShowBottomSheet) {
            ModalBottomSheet(
                containerColor = White3,
                onDismissRequest = { onEvent(SpendingDetailEvent.HideCategoryIconBottomSheet) },
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
                        items(items = uiState.definedCategories.filter {
                            !uiState.spendingSnapshot!!.spendingCategories.map { sc -> sc.categoryCode }
                                .contains(it.code)
                        }) { category ->
                            val iconRes = CategoryIcon.fromCode(category.icon).resId
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .customClick(
                                        shape = RoundedCornerShape(12.dp),
                                        onClick = {
                                            onEvent(
                                                SpendingDetailEvent.ChangeCategoryIcon(
                                                    id = category.id,
                                                    icon = category.icon,
                                                    color = category.textColor
                                                )
                                            )
                                            onEvent(SpendingDetailEvent.HideCategoryIconBottomSheet)
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
                                            color = category.textColor.toColorFromHex()
                                                .copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = iconRes),
                                        contentDescription = null,
                                        tint = category.textColor.toColorFromHex(),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                // Category name
                                Text(
                                    text = category.name,
                                    style = AppTypography.labelSmall,
                                    color = Black1,
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

        // Delete Confirmation Dialog
        if (categoryToDelete != null) {
            CustomConfirmDialog(
                dismissText = "Hủy",
                confirmText = "Xóa",
                onDismiss = {
                    categoryToDelete = null
                },
                onConfirm = {
                    onEvent(
                        SpendingDetailEvent.DeleteSpendingCategory(
                            categoryCode = categoryToDelete!!
                        )
                    )
                    categoryToDelete = null
                },
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Xác nhận xóa danh mục",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Black1
                    )
                    Text(
                        text = "Bạn có chắc chắn muốn xóa danh mục này không? Hành động này không thể hoàn tác.",
                        style = AppTypography.bodyMedium,
                        color = Gray1,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Recommend input dialog
        if (uiState.isShowRecommendInputDialog) {
            CustomConfirmDialog(
                dismissText = "Hủy",
                confirmText = "Tiếp tục",
                onDismiss = {
                    onEvent(SpendingDetailEvent.HideRecommendInputDialog)
                },
                onConfirm = {
                    onEvent(SpendingDetailEvent.SubmitRecommend)
                },
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Gợi ý danh mục bằng AI",
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Black1
                    )
                    Text(
                        text = "Nhập yêu cầu của bạn để AI gợi ý danh mục phù hợp",
                        style = AppTypography.bodySmall,
                        color = Gray1,
                        textAlign = TextAlign.Center
                    )
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.recommendRequirement,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        onValueChange = {
                            onEvent(SpendingDetailEvent.ChangeRecommendRequirement(it))
                        },
                        placeholder = {
                            Text(
                                "Ví dụ: Tôi muốn ngân sách ăn uống 30%...",
                                style = AppTypography.bodySmall,
                                color = Gray2
                            )
                        },
                    )
                }
            }
        }

        // AI Recommend result dialog
        if (uiState.isShowRecommendResultDialog) {
            CustomConfirmDialog(
                dismissText = "Hủy",
                confirmText = "Áp dụng",
                onDismiss = {
                    onEvent(SpendingDetailEvent.HideRecommendResultDialog)
                },
                onConfirm = {
                    onEvent(SpendingDetailEvent.ApplyRecommend)
                },
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Gợi ý cho bạn",
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Black1
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.recommendResult) { category ->
                            val definedCategory = uiState.definedCategories.find { it.id == category.id }
                            val iconRes = CategoryIcon.fromCode(definedCategory?.icon ?: "").resId
                            val color = (definedCategory?.textColor ?: "#2196F3").toColorFromHex()
                            val budgetLong = category.budgetAmount
                                .replace(",", "")
                                .replace(".", "")
                                .toLongOrNull() ?: 0L
                            val totalBudget = uiState.spendingSnapshot?.budgetAmount?.toLong() ?: 1L
                            val percent = if (totalBudget > 0L) ((budgetLong.toFloat() / totalBudget.toFloat()) * 100).toInt() else 0

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 4.dp,
                                        shape = RoundedCornerShape(12.dp),
                                        ambientColor = Black1.copy(alpha = 0.1f),
                                        spotColor = Black1.copy(alpha = 0.1f)
                                    )
                                    .background(White1, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Icon
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(
                                            color = color.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(10.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(iconRes),
                                        contentDescription = null,
                                        tint = color,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                // Info
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(
                                        text = category.name,
                                        style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = Black1
                                    )
                                    Text(
                                        text = "${formatterVND(budgetLong)}đ",
                                        style = AppTypography.labelMedium,
                                        color = Gray1
                                    )
                                }
                                // Percent badge
                                Row(
                                    modifier = Modifier
                                        .background(
                                            color = Blue3.copy(alpha = 0.08f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "$percent%",
                                        style = AppTypography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Blue3
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Recommending loading overlay
        if (uiState.isRecommending) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black1.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(color = White1)
                    Text(
                        text = "AI đang phân tích...",
                        style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = White1
                    )
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
                        textColor = "#FFFFFF",
                        backgroundColor = "#FF6B6B",
                        budgetAmount = BigDecimal(5000000),
                        usedAmount = BigDecimal(3500000),
                        categoryId = "cat_001"
                    ),
                    SpendingCategoryDetailResponse(
                        categoryName = "Đi lại",
                        categoryCode = "transport",
                        categoryIcon = "",
                        textColor = "#FFFFFF",
                        backgroundColor = "#4D96FF",
                        budgetAmount = BigDecimal(5000000),
                        usedAmount = BigDecimal(3500000),
                        categoryId = "cat_002"
                    ),
                    SpendingCategoryDetailResponse(
                        categoryName = "Mua sắm",
                        categoryCode = "shopping",
                        categoryIcon = "",
                        textColor = "#000000",
                        backgroundColor = "#FFD93D",
                        budgetAmount = BigDecimal(5000000),
                        usedAmount = BigDecimal(3500000),
                        categoryId = "cat_003"
                    ),
                    SpendingCategoryDetailResponse(
                        categoryName = "Giải trí",
                        categoryCode = "entertainment",
                        categoryIcon = "",
                        textColor = "#FFFFFF",
                        backgroundColor = "#6BCB77",
                        budgetAmount = BigDecimal(5000000),
                        usedAmount = BigDecimal(3500000),
                        categoryId = "cat_004"
                    ),
                    SpendingCategoryDetailResponse(
                        categoryName = "Sức khỏe",
                        categoryCode = "health",
                        categoryIcon = "",
                        textColor = "#FFFFFF",
                        backgroundColor = "#9D4EDD",
                        budgetAmount = BigDecimal(5000000),
                        usedAmount = BigDecimal(3500000),
                        categoryId = "cat_005"
                    ),
                ),
            ),
        ),
        onBackClick = {},
        onEvent = {}
    )
}
