package com.example.ibanking_kltn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.data.dtos.SavedReceiver
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.ui.uistates.TransferUiState
import com.example.ibanking_kltn.utils.CustomDropdownField
import com.example.ibanking_kltn.utils.CustomTextButton
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.SavedReceiverDialog
import com.example.ibanking_kltn.utils.customClick
import com.example.ibanking_kltn.utils.formatterVND

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    uiState: TransferUiState,
    savedReceivers: List<SavedReceiver>,
    onDoneWalletNumber: () -> Unit,
    onBackClick: () -> Unit,
    onExpenseTypeChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onChangeReceiveWalletNumber: (String) -> Unit,
    onChangeAmount: (String) -> Unit,
    onChangeDescription: (String) -> Unit,
    isEnableContinue: Boolean,
    onSelectSavedReceiver: (SavedReceiver) -> Unit,
    onChangeSaveReceiver: () -> Unit
) {
    val scrollState = rememberScrollState(0)
    val focusManager = LocalFocusManager.current
    var handled by remember {
        mutableStateOf(
            false
        )
    }
    var isShowSavedReceiverDialog by remember{
        mutableStateOf(
            false
        )
    }
    LoadingScaffold(isLoading = uiState.screenState is StateType.LOADING) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(R.string.Transfer_Title))
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
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(state = scrollState)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Người thụ hưởng đã lưu",
                                style = AppTypography.bodyMedium,
                                color = Gray1
                            )

                        }
                        IconButton(onClick = {
                            isShowSavedReceiverDialog = true
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.contact),
                                contentDescription = null,
                                tint = Gray1,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 30.dp,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = Black1.copy(alpha = 0.25f),
                                spotColor = Black1.copy(alpha = 0.25f)

                            )
                            .background(
                                color = White1,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "Thông tin người nhận",
                                style = AppTypography.bodyMedium,
                                color = Gray1
                            )
                        }
                        CustomTextField(
                            value = uiState.toWalletNumber,
                            placeholder = {
                                Text(
                                    "Số tài khoản",
                                    style = AppTypography.bodyMedium,
                                    color = Gray2
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    onDoneWalletNumber()
                                },

                                ),
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
                            onValueChange = {
                                onChangeReceiveWalletNumber(it)
                            }
                        )
                        if(uiState.toMerchantName.isNotEmpty()){
                        CustomTextField(
                            value = uiState.toMerchantName,
                            placeholder = {
                                Text(
                                    "Tên người thụ hưởng", style = AppTypography.bodyMedium,
                                    color = Gray2
                                )
                            },
                            enable = false,
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = {},

                            )

                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 30.dp,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = Black1.copy(alpha = 0.25f),
                                spotColor = Black1.copy(alpha = 0.25f)

                            )
                            .background(
                                color = White1,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "Thông tin giao dịch",
                                style = AppTypography.bodyMedium,
                                color = Gray1
                            )
                        }
                        CustomTextField(
                            value = formatterVND(uiState.amount),
                            placeholder = {
                                Text(
                                    "Số tiền", style = AppTypography.bodyMedium,
                                    color = Gray2
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.vnd),
                                    contentDescription = null,
                                    tint = Gray1,
                                    modifier = Modifier.size(25.dp)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = {
                                onChangeAmount(it)
                            }
                        )
                        CustomTextField(
                            value = uiState.description,
                            placeholder = {
                                Text(
                                    "Nội dung chuyển khoản", style = AppTypography.bodyMedium,
                                    color = Gray2
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = {
                                onChangeDescription(it)
                            }
                        )
                        CustomDropdownField(
                            modifier = Modifier.fillMaxWidth(),
                            options = uiState.allExpenseTypeResponse.map {
                                it.name
                            },
                            onOptionSelected = {
                                onExpenseTypeChange(it)
                            },
                            selectedOption = uiState.expenseType,
                            placeholder = "Phân loại (Hệ thống tự động phân loại nếu để trống)"
                        )
                    }
                }

                //navigationbar
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Bottom),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            5.dp,
                            alignment = Alignment.Start
                        ),
                        modifier = Modifier.fillMaxWidth()
                            .customClick{
                                onChangeSaveReceiver()

                            }
                    ) {
                        Checkbox(
                            checked = uiState.isSaveReceiver,
                            onCheckedChange = {
                                onChangeSaveReceiver()
                            },
                            enabled = true,
                            colors = CheckboxDefaults.colors(
                                checkedColor = Blue1,
                                uncheckedColor = Gray1,
                            ),
                        )
                        Text(text = "Lưu người nhận", style = AppTypography.bodyMedium)
                    }

                    CustomTextButton(
                        enable = isEnableContinue,
                        onClick = {
                            onConfirmClick()
                        },
                        isLoading = uiState.confirmState is StateType.LOADING,
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.Continue),
                    )
                }
            }

        }
        if(isShowSavedReceiverDialog){
            SavedReceiverDialog(
                savedReceivers = savedReceivers,
                onSelect = {
                    onSelectSavedReceiver(it)
                    isShowSavedReceiverDialog = false
                },
                onDimiss = {
                    isShowSavedReceiverDialog = false
                }
            ) 
        }

    }

}

@Preview(
    showBackground = true,
    showSystemUi = true

)
@Composable
fun TransferPreview() {
//    TransferScreen(
//        uiState = TransferUiState(),
//        onDoneWalletNumber = {},
//        onBackClick = {},
//        onAccountTypeChange = {},
//        onExpenseTypeChange = {},
//        onConfirmClick = {},
//        onChangeReceiveWalletNumber = {},
//        onChangeAmount = {},
//        onChangeDescription = {},
//        isEnableContinue = false,
//    )
}
