package com.example.ibanking_kltn.ui.screens.category_management

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.dtos.responses.DefinedSpendingCategoryResponse
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue3
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.Red1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.colorFromLabel
import com.example.ibanking_kltn.utils.customClick


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagement(
//    uiState: SpendingDetailUiState,
    onBackClick: () -> Unit = {},
//    onEvent: (SpendingDetailE={}vent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var isShowBottomSheet by remember {
        mutableStateOf(false)
    }

    val mock = listOf(
        DefinedSpendingCategoryResponse(
            id = "TODO()",
            name = "thú cưn",
            code = "CAT001",
            icon = "pet",
            textColor = "TODO()",
            backgroundColor = " TODO()",
            systemCategoryCode = "TODO()",
            systemCategoryName = "TODO()"
        )
    )

    Box {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Quản lý danh mục chi tiêu")
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
                        isShowBottomSheet=true
                    },
                    containerColor = Blue3,
                    contentColor = White1,
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(  R.drawable.add_regular),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                    )
                }
            },
            modifier = Modifier.systemBarsPadding(),
            containerColor = White3
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
            ) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    items(items = mock) { category ->
                        val iconRes =
                            category.icon.toIntOrNull() ?: R.drawable.airplane_service

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
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = category.name.let { colorFromLabel(it) }
                                            .copy(alpha = 0.1f),
                                        shape = CircleShape
                                    )
                                    .padding(5.dp)
                            ) {

                                Icon(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = null,
                                    tint = colorFromLabel(category.name),
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
                                    shape = CircleShape,
                                    onClick = {
                                        //todo
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
                                    shape = CircleShape,
                                    onClick = {
                                        //todo
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
        if (isShowBottomSheet) {
            ModalBottomSheet(
                containerColor = White3,
                onDismissRequest = { isShowBottomSheet = false },
                sheetState = sheetState
            ) {
                var selectedIcon by remember {
                    mutableStateOf<Any?>(null)
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp),

                    ) {
                    // Header
                    Text(
                        text = "Thêm danh mục chi tiêu",
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
                                selectedIcon.toString().toIntOrNull() ?: R.drawable.unknown
                            ),
                            contentDescription = null,
                            tint = Black1,
                            modifier = Modifier.size(50.dp)
                        )
                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = "uiState.categoryBudgetName",
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text
                            ),
                            onValueChange = {
//                                onEvent(
//                                    SpendingDetailEvent.ChangeCategoryName(it)
//                                )
                            },
                            placeholder = {
                                Text(
                                    "Tên danh mục", style = AppTypography.bodyMedium,
                                    color = Gray2
                                )
                            },
                        )
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .height(400.dp)
                                .padding(top = 16.dp)
                        ) {
                            items(items = mock) {

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
    CategoryManagement()
}
