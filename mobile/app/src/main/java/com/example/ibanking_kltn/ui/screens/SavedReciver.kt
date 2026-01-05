package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Blue3
import com.example.ibanking_kltn.ui.theme.Blue5
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.Gray3
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.SavedReceiverUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.DefaultImageProfile
import com.example.ibanking_kltn.utils.SwipeComponent
import com.example.ibanking_kltn.utils.customClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedReceiverScreen(
    uiState: SavedReceiverUiState,
    onBackClick: () -> Unit,
    onDoneWalletNumber: () -> Unit,
    onAddReceiver: () -> Unit,
    onDeleteReceiver: (String) -> Unit,
    onSearchReceiver: () -> Unit,
    onChangeKeyword: (String) -> Unit,
    onChangeMemorableName: (String) -> Unit,
    onChangeToWalletNumber: (String) -> Unit,
    onClearAddReceiverDialog: () -> Unit,
) {
    val scrollState = rememberScrollState(0)
    var handled by remember {
        mutableStateOf(
            false
        )
    }
    var isShowAddDialog by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    val refreshState = rememberPullToRefreshState()
    Box {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        isShowAddDialog = true
                    },
                    shape = CircleShape,
                    containerColor = Blue1
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = White1
                    )
                }
            },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.SavedReceiver_Title),
                        )
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
            modifier = Modifier.systemBarsPadding(),
            containerColor = White3
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomTextField(
                        value = uiState.keyword,
                        onValueChange = onChangeKeyword,
                        placeholder = {
                            Text(stringResource(id = R.string.SavedReceiver_SearchHint))
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search,
                            keyboardType = KeyboardType.Text
                        ),

                        keyboardActions = KeyboardActions(
                            onSearch = {
                                focusManager.clearFocus()
                                onSearchReceiver()
                            }
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                PullToRefreshBox(
                    isRefreshing = uiState.screenState is StateType.LOADING,
                    state = refreshState,
                    onRefresh = {
                        onSearchReceiver()
                    },
                    indicator = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {

                            PullToRefreshDefaults.Indicator(
                                state = refreshState,
                                isRefreshing = uiState.screenState is StateType.LOADING,
                                containerColor = White1,
                                color = Blue3,
                            )
                        }

                    },
                    modifier = Modifier
                        .fillMaxSize()

                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(
                                horizontal = 20.dp,
                                vertical = 10.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        if (uiState.savedReceivers.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Không tìm thấy người nhận đã lưu",
                                    style = AppTypography.bodyMedium,
                                    color = Gray2,
                                )
                            }
                            return@Column
                        }
                        uiState.savedReceivers.forEach { savedReceiver ->
                            SwipeComponent(
                                onEndSwipe = {
                                    onDeleteReceiver(savedReceiver.toWalletNumber)
                                },
                                onStartSwipe = null
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(IntrinsicSize.Min),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                                    ) {
                                        DefaultImageProfile(
                                            name = savedReceiver.toMerchantName,
                                            modifier = Modifier.size(40.dp)
                                        )
                                        Column(
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = savedReceiver.memorableName,
                                                style = AppTypography.bodyMedium.copy(
                                                    color = Black1
                                                )
                                            )
                                            Text(
                                                text = savedReceiver.toMerchantName,
                                                style = AppTypography.bodySmall.copy(
                                                    color = Gray1
                                                )
                                            )
                                        }
                                        Column(
                                            modifier = Modifier.fillMaxHeight(),
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = savedReceiver.toWalletNumber,
                                                style = AppTypography.bodyMedium.copy(
                                                    color = Gray1,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }

                                    }
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = Gray2
                                    )
                                }
                            }

                        }

                    }
                }
            }
        }
        if (isShowAddDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Gray3.copy(alpha = 0.5f),
                    )
                    .padding(20.dp)
                    .pointerInput(Unit) {},
                contentAlignment = Alignment.Center
            ) {
                Box(
                    contentAlignment = Alignment.TopEnd,
                    modifier = Modifier
                        .background(
                            color = White1,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()

                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Column {

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row {
                                    Text(
                                        text = "Số tài khoản",
                                        style = AppTypography.bodyMedium,
                                        color = Gray1
                                    )
                                }
                                CustomTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .onFocusChanged(
                                            onFocusChanged = {
                                                if (
                                                    it.isFocused
                                                ) {
                                                    handled = true
                                                } else if (handled) {
                                                    onDoneWalletNumber()
                                                    handled = false
                                                }
                                            }
                                        ),
                                    value = uiState.toWalletNumber,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done,
                                        keyboardType = KeyboardType.Number
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focusManager.clearFocus()
                                            onDoneWalletNumber()
                                        }
                                    ),
                                    enable = true,
                                    onValueChange = {
                                        onChangeToWalletNumber(it)
                                    }
                                )
                            }
                            if (uiState.toMerchantName.isNotEmpty()) {

                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row {
                                        Text(
                                            text = "Tên chủ tài khoản",
                                            style = AppTypography.bodyMedium,
                                            color = Gray1
                                        )
                                    }
                                    CustomTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = uiState.toMerchantName,
                                        enable = false,
                                        onValueChange = {}
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row {
                                    Text(
                                        text = "Tên gợi nhớ",
                                        style = AppTypography.bodyMedium,
                                        color = Gray1
                                    )
                                }
                                CustomTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = uiState.memorableName,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Next,
                                        keyboardType = KeyboardType.Text
                                    ),
                                    enable = true,
                                    onValueChange = {
                                        onChangeMemorableName(it)
                                    }
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                10.dp,
                                alignment = Alignment.End
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = Blue5,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .customClick(
                                        shape = RoundedCornerShape(10.dp),
                                        enable = (uiState.toMerchantName.isNotEmpty() && uiState.memorableName.isNotEmpty() && uiState.toWalletNumber.isNotEmpty())
                                    ) {
                                        onAddReceiver()
                                        isShowAddDialog = false

                                    }
                                    .padding(vertical = 10.dp, horizontal = 20.dp),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    text = "Thêm người nhận",
                                    style = AppTypography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = White1
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .shadow(
                                elevation = 10.dp,
                                shape = CircleShape,
                                ambientColor = Color.Transparent,
                                spotColor = Color.Transparent
                            )
                            .clickable {
                                isShowAddDialog = false
                                onClearAddReceiverDialog()
                            }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.close), contentDescription = null,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }

            }

        }

    }
}

@Preview(
    showBackground = true,
    showSystemUi = true

)
@Composable
fun SavedReceiverPreview() {
    SavedReceiverScreen(
        uiState = SavedReceiverUiState(),
        onBackClick = {},
        onDoneWalletNumber = {},
        onAddReceiver = {},
        onDeleteReceiver = {},
        onSearchReceiver = {},
        onChangeKeyword = {},
        onChangeMemorableName = {},
        onChangeToWalletNumber = {},
        onClearAddReceiverDialog = {},
    )
}
