package com.example.ibanking_kltn.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.DepositUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.CustomDropdownField
import com.example.ibanking_kltn.utils.CustomTextButton
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.formatterVND

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GatewayDeposit(
    uiState: DepositUiState,
    onBackClick: () -> Unit,
    onContinuePayment: () -> Unit,
    onAccountTypeChange: (String) -> Unit,
    onChangeAmount: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    LoadingScaffold(isLoading = uiState.screenState is StateType.LOADING) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Nạp tiền")
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
                ) {

                    Row(modifier = Modifier.fillMaxWidth()) {
                        CustomDropdownField(
                            modifier = Modifier.fillMaxWidth(),
                            options = uiState.availableAccount,
                            onOptionSelected = {
                                onAccountTypeChange(it)
                            },
                            selectedOption = uiState.accountType,
                            placeholder = "Chọn tài khoản thanh toán thụ hưởng"
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Số dư: ${formatterVND(uiState.balance)} VND",
                            style = CustomTypography.titleMedium,
                            color = Blue1
                        )
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row {
                                Text(
                                    text = "Số tiền cần nạp",
                                    style = CustomTypography.titleMedium,
                                    color = Gray1
                                )
                            }
                            CustomTextField(
                                value = formatterVND(uiState.amount),
                                placeholder = {
                                    Text(
                                        "Số tiền", style = CustomTypography.titleMedium,
                                        color = Gray2
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done

                                ),
                                keyboardActions = KeyboardActions (
                                    onDone = {
                                        focusManager.clearFocus()
                                    }
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
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            HintAmount(
                                onAmountClick = { amount ->
                                    onChangeAmount(formatterVND(amount))
                                    focusManager.clearFocus()
                                }
                            )

                        }
                    }
                }

                //navigationbar
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Bottom),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    CustomTextButton(
                        enable = true,
                        onClick = {
                            onContinuePayment()
                        },
                        isLoading = false,
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "Nạp tiền",
                    )
                }
            }

        }


    }

}


@Composable
fun HintAmount(
    onAmountClick: (Long) -> Unit
) {
    val amounts = listOf(
        50000L,
        100000L,
        200000L,
        500000L,
        1000000L,
        2000000L,
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(
                    corner = CornerSize(20.dp)
                )
            )
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,

            ) {

            items(items = amounts) { amount ->
                OutlinedButton(
                    onClick = { onAmountClick(amount) },
                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                    contentPadding = PaddingValues(all = 0.dp),
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(formatterVND(amount), fontSize = 15.sp, color = Color.Black)
                }
            }
        }

    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewDeposit() {
    GatewayDeposit(
        uiState = DepositUiState(
            screenState = StateType.NONE,
            balance = 5000000L,
            availableAccount = listOf("TK Thanh Toán 1", "TK Thanh Toán 2"),
            amount = 0L,
            accountType = "TK Thanh Toán 1"
        ),
        onBackClick = {},
        onContinuePayment = {},
        onAccountTypeChange = {},
        onChangeAmount = {}
    )
}
