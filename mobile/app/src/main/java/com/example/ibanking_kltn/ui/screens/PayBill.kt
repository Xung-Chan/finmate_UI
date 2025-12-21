@file:JvmName("PayBillKt")

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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.BillUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.CustomDropdownField
import com.example.ibanking_kltn.utils.CustomTextButton
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.formatterVND

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayBillScreen(
    uiState: BillUiState,
    onBackClick: () -> Unit = {},
    onCheckingBill: () -> Unit = {},
    onConfirmPayBill: () -> Unit = {},
    onChangeBillCode: (String) -> Unit = {},
    onChangeAccountType: (String) -> Unit = {},
) {
    val verticalScrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    LoadingScaffold(
        isLoading = uiState.screenState is StateType.LOADING
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.PayBill_Title))
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
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(verticalScrollState)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
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
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.BillCode),
                                style = CustomTypography.titleMedium,
                                color = Gray1
                            )

                            CustomTextField(
                                value = uiState.billCode,
                                placeholder = {
                                    Text(
                                        text = stringResource(id = R.string.BillCodeDescription),
                                        color = Gray2
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        focusManager.clearFocus()
                                        onCheckingBill()
                                    }
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                onValueChange = {
                                    onChangeBillCode(it)
                                }
                            )

                        }
                        if (uiState.checkingState is StateType.SUCCESS) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.MerchantName),
                                    style = CustomTypography.titleMedium,
                                    color = Gray1
                                )

                                CustomTextField(
                                    value = uiState.toMerchantName,
                                    placeholder = {
                                        Text(
                                            text = stringResource(id = R.string.MerchantName),
                                            color = Gray2
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    onValueChange = {},
                                    enable = false
                                )

                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.Amount),
                                    style = CustomTypography.titleMedium,
                                    color = Gray1
                                )

                                CustomTextField(
                                    value = formatterVND(uiState.amount),
                                    placeholder = {
                                        Text(
                                            text = stringResource(id = R.string.Amount),
                                            color = Gray2
                                        )
                                    },
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(R.drawable.vnd),
                                            contentDescription = null,
                                            tint = Gray1,
                                            modifier = Modifier.size(25.dp)
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    onValueChange = {},
                                    enable = false
                                )

                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.Description),
                                    style = CustomTypography.titleMedium,
                                    color = Gray1
                                )

                                CustomTextField(
                                    value = uiState.description,
                                    placeholder = {
                                        Text(
                                            text = stringResource(id = R.string.Description),
                                            color = Gray2
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    onValueChange = {},
                                    enable = false
                                )

                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.DueDate),
                                    style = CustomTypography.titleMedium,
                                    color = Gray1
                                )

                                CustomTextField(
                                    value = uiState.dueDate,
                                    placeholder = {
                                        Text(
                                            text = stringResource(id = R.string.DueDate),
                                            color = Gray2
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    onValueChange = {},
                                    enable = false
                                )

                            }

                        } else {

                            Text(
                                text = stringResource(id = R.string.BillDescription),
                                style = CustomTypography.titleMedium,
                                color = Gray2
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                CustomTextButton(
                                    onClick = {
                                        focusManager.clearFocus()
                                        onCheckingBill()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = stringResource(id = R.string.BillCheckButton),
                                    enable = uiState.billCode.isNotEmpty()
                                )


                            }

                        }
                    }
                }

                //bottombar
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp, alignment = Alignment.Bottom),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 30.dp,
                            shape = RectangleShape,
                            ambientColor = Black1.copy(alpha = 0.25f),
                            spotColor = Black1.copy(alpha = 0.25f)
                        )
                        .background(color = White1, shape = RectangleShape)
                        .padding(20.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CustomDropdownField(
                            modifier = Modifier.fillMaxWidth(),
                            options = uiState.availableAccount,
                            onOptionSelected = {
                                onChangeAccountType(it)
                            },
                            selectedOption = uiState.accountType,
                            placeholder = "Chọn tài khoản thanh toán"
                        )
                    }

                    CustomTextButton(
                        enable = uiState.checkingState is StateType.SUCCESS && uiState.confirmState !is StateType.LOADING && uiState.accountType.isNotEmpty(),
                        onClick = {
                            onConfirmPayBill()
                        },
                        isLoading = uiState.confirmState is StateType.LOADING,
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.Confirm),
                    )

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
fun ElectricScreenPreview() {
    PayBillScreen(
        uiState = BillUiState(
            screenState = StateType.NONE,
            confirmState = StateType.NONE,
            checkingState = StateType.SUCCESS,
            billCode = "123456789"
        )

    )
}