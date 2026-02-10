package com.example.ibanking_kltn.ui.screens.spending.category_management

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.dtos.definitions.CategoryIcon
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue3
import com.example.ibanking_kltn.ui.theme.Blue5
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.Red1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.RetryCompose
import com.example.ibanking_kltn.utils.SkeletonBox
import com.example.ibanking_kltn.utils.customClick
import com.example.ibanking_kltn.utils.toColorFromHex
import com.example.ibanking_kltn.utils.toHexString


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagement(
    uiState: CategoryManagementUiState,
    onBackClick: () -> Unit = {},
    onEvent: (CategoryManagementEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var isShowBottomSheet by remember {
        mutableStateOf(false)
    }
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }
    var categoryToDelete by remember {
        mutableStateOf<String?>(null)
    }

    LoadingScaffold(
        isLoading = uiState.screenState == CategoryManagementState.LOADING
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Quản lý danh mục")
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
                        // Reset form for new category
                        onEvent(CategoryManagementEvent.ResetForm)
                        isShowBottomSheet = true
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
            when (
                uiState.screenState
            ) {
                CategoryManagementState.INIT -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 20.dp)
                    ) {
                        CategoryManagementSkeleton()
                    }
                }

                CategoryManagementState.INIT_FAILED -> {
                    RetryCompose {
                        onEvent(CategoryManagementEvent.RetryInitCategories)
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 20.dp)
                    ) {
                        if (
                            uiState.definedCategories.isEmpty()
                        ) {
                            // Empty state
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 50.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Chưa có danh mục chi tiêu",
                                    style = AppTypography.bodyMedium,
                                    color = Gray1
                                )
                            }
                        } else {

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                items(items = uiState.definedCategories) { category ->
                                    val iconRes = CategoryIcon.fromCode(category.icon).resId
                                    val color = category.textColor.toColorFromHex()
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .shadow(
                                                elevation = 30.dp,
                                                shape = RoundedCornerShape(12.dp),
                                                ambientColor = Black1.copy(alpha = 0.2f),
                                                spotColor = Black1.copy(alpha = 0.2f)
                                            )
                                            .background(
                                                color = White1,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .background(
                                                    color = color
                                                        .copy(alpha = 0.1f),
                                                    shape = CircleShape
                                                )
                                                .padding(5.dp)
                                        ) {

                                            Icon(
                                                painter = painterResource(id = iconRes),
                                                contentDescription = null,
                                                tint = color,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                        Text(
                                            text = category.name,
                                            style = AppTypography.bodyMedium,
                                            color = Black1,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Row(
                                            modifier = Modifier.customClick(
                                                shape = RoundedCornerShape(7.dp),
                                                onClick = {
                                                    onEvent(
                                                        CategoryManagementEvent.OpenEditDialog(
                                                            category
                                                        )
                                                    )
                                                    isShowBottomSheet = true
                                                }
                                            )
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.edit),
                                                contentDescription = null,
                                                tint = Gray1,
                                                modifier = Modifier
                                                    .size(25.dp)
                                            )
                                        }
                                        Row(
                                            modifier = Modifier.customClick(
                                                shape = RoundedCornerShape(7.dp),
                                                onClick = {
                                                    categoryToDelete = category.id
                                                    showDeleteDialog = true
                                                }
                                            )
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.trash),
                                                contentDescription = null,
                                                tint = Red1,
                                                modifier = Modifier
                                                    .size(25.dp)
                                            )
                                        }
                                    }
                                }


                            }
                        }
                    }

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
                        .padding(bottom = 20.dp),

                    ) {
                    // Header
                    Text(
                        text = if (uiState.isEditMode) "Chỉnh sửa danh mục chi tiêu" else "Thêm danh mục chi tiêu",
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
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                uiState.selectedIcon.resId
                            ),
                            contentDescription = null,
                            tint = uiState.color.toColorFromHex(),
                            modifier = Modifier.size(50.dp)
                        )
                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = uiState.categoryName,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text
                            ),
                            onValueChange = {
                                onEvent(
                                    CategoryManagementEvent.ChangeCategoryName(it)
                                )
                            },
                            placeholder = {
                                Text(
                                    "Tên danh mục", style = AppTypography.bodyMedium,
                                    color = Gray2
                                )
                            },
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            var sliderPosition by remember { mutableFloatStateOf(0f) }

                            Text(
                                text = "Chọn màu",
                                style = AppTypography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = Gray1
                            )
                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Color preview box
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(
                                            color = uiState.color.toColorFromHex(),
                                            shape = CircleShape
                                        )
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(30.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                brush = Brush.horizontalGradient(
                                                    colors = listOf(
                                                        Color(0xFFFF0000), // Red
                                                        Color(0xFFFF7F00), // Orange
                                                        Color(0xFFFFFF00), // Yellow
                                                        Color(0xFF00FF00), // Green
                                                        Color(0xFF0000FF), // Blue
                                                        Color(0xFF4B0082), // Indigo
                                                        Color(0xFF9400D3)  // Violet
                                                    )
                                                ),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                    )

                                    // Slider
                                    Slider(
                                        value = sliderPosition,
                                        onValueChange = { newValue ->
                                            sliderPosition = newValue
                                            val hue = newValue * 360f
                                            val selectedColor = Color.hsl(
                                                hue,
                                                1f,
                                                0.5f
                                            ).toHexString()
                                            onEvent(
                                                CategoryManagementEvent.ChangeCategoryColor(
                                                    selectedColor
                                                )
                                            )

                                        },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 4.dp),
                                        colors = SliderDefaults.colors(
                                            thumbColor = uiState.color.toColorFromHex(),
                                            activeTrackColor = Color.Transparent,
                                            inactiveTrackColor = Color.Transparent
                                        )
                                    )
                                }
                            }
                        }


                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Chọn icon",
                                style = AppTypography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = Gray1
                            )
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(5),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(items = CategoryIcon.entries) { icon ->
                                    val isSelected = uiState.selectedIcon == icon

                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .then(
                                                if (isSelected) {
                                                    Modifier.border(
                                                        width = 2.dp,
                                                        color = uiState.color.toColorFromHex(),
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                } else Modifier
                                            )
                                            .background(
                                                color = if (isSelected)
                                                    uiState.color.toColorFromHex()
                                                        .copy(alpha = 0.15f)
                                                else
                                                    Gray1.copy(alpha = 0.1f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .customClick(
                                                shape = RoundedCornerShape(12.dp),
                                                onClick = {
                                                    onEvent(
                                                        CategoryManagementEvent.ChangeSelectedIcon(
                                                            icon
                                                        )
                                                    )
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(icon.resId),
                                            contentDescription = icon.name,
                                            tint = if (isSelected)
                                                uiState.color.toColorFromHex()
                                            else
                                                Black1.copy(alpha = 0.7f),
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }

                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Blue5,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .customClick(
                                    shape = RoundedCornerShape(10.dp),
                                    onClick = {
                                        if (uiState.isEditMode) {
                                            onEvent(CategoryManagementEvent.UpdateDefinedCategory)
                                        } else {
                                            onEvent(CategoryManagementEvent.AddDefinedCategory)
                                        }
                                        isShowBottomSheet = false
                                    }
                                )
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = if (uiState.isEditMode) "Cập nhật danh mục" else "Thêm danh mục",
                                style = AppTypography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = White1
                            )
                        }
                    }


                }

            }
        }

        // Delete Confirmation Dialog
        if (showDeleteDialog && categoryToDelete != null) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                    categoryToDelete = null
                },
                title = {
                    Text(
                        text = "Xác nhận xóa",
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Black1
                    )
                },
                text = {
                    Text(
                        text = "Bạn có chắc chắn muốn xóa danh mục này không? Hành động này không thể hoàn tác.",
                        style = AppTypography.bodyMedium,
                        color = Gray1
                    )
                },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .background(
                                color = Red1,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .customClick(
                                shape = RoundedCornerShape(8.dp),
                                onClick = {
                                    categoryToDelete?.let {
                                        onEvent(CategoryManagementEvent.DeleteDefinedCategory(it))
                                    }
                                    showDeleteDialog = false
                                    categoryToDelete = null
                                }
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Xóa",
                            style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = White1
                        )
                    }
                },
                dismissButton = {
                    Row(
                        modifier = Modifier
                            .background(
                                color = Gray1.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .customClick(
                                shape = RoundedCornerShape(8.dp),
                                onClick = {
                                    showDeleteDialog = false
                                    categoryToDelete = null
                                }
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Hủy",
                            style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Black1
                        )
                    }
                },
                containerColor = White3,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
private fun CategoryManagementSkeleton() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(5) {
            CategoryItemSkeleton()
        }
    }
}

@Composable
private fun CategoryItemSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 30.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Black1.copy(alpha = 0.2f),
                spotColor = Black1.copy(alpha = 0.2f)
            )
            .background(
                color = White1,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Icon skeleton
        SkeletonBox(
            modifier = Modifier.size(40.dp),
            shape = CircleShape
        )

        // Category name skeleton
        SkeletonBox(
            modifier = Modifier
                .weight(1f)
                .height(20.dp),
            shape = RoundedCornerShape(4.dp)
        )

        // Edit button skeleton
        SkeletonBox(
            modifier = Modifier.size(25.dp),
            shape = RoundedCornerShape(7.dp)
        )

        // Delete button skeleton
        SkeletonBox(
            modifier = Modifier.size(25.dp),
            shape = RoundedCornerShape(7.dp)
        )
    }
}


//@Preview
//@Composable
//fun PreviewCategorySpending() {
//    CategoryManagement()
//}
