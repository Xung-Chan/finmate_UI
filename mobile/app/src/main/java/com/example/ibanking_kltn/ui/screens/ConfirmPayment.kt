package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.data.dtos.Service
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.ConfirmUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.CustomDropdownField
import com.example.ibanking_kltn.utils.CustomTextButton
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.OtpDialogCustom
import com.example.ibanking_kltn.utils.formatterVND

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPaymentScreen(
    uiState: ConfirmUiState,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onOtpChange: (String) -> Unit,
    onOtpDismiss: () -> Unit,
) {
    val scrollState = rememberScrollState(0)

    LoadingScaffold(
        isLoading = uiState.screenState is StateType.LOADING
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(R.string.Confirm))
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
            modifier = Modifier
                .systemBarsPadding(),
            containerColor = White3
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(state = scrollState)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row {
                            Text(
                                text = "Loại giao dịch",
                                style = CustomTypography.titleMedium,
                                color = Gray1
                            )
                        }
                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = uiState.service.name,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text
                            ),
                            enable = false,
                            onValueChange = {}
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row {
                            Text(
                                text = "Từ",
                                style = CustomTypography.titleMedium,
                                color = Gray1
                            )
                        }
                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = uiState.accountType,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text
                            ),
                            enable = false,
                            onValueChange = {}
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
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "Thông tin người nhận",
                                style = CustomTypography.titleMedium,
                                color = Gray1
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row {
                                Text(
                                    text = "Họ tên",
                                    style = CustomTypography.titleMedium,
                                    color = Gray1
                                )
                            }
                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = uiState.toMerchantName,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Text
                                ),
                                enable = false,
                                onValueChange = {}
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row {
                                Text(
                                    text = "Số tài khoản",
                                    style = CustomTypography.titleMedium,
                                    color = Gray1
                                )
                            }
                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = uiState.toWalletNumber,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Text
                                ),
                                enable = false,
                                onValueChange = {}
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
                                style = CustomTypography.titleMedium,
                                color = Gray1
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row {
                                Text(
                                    text = "Số tiền",
                                    style = CustomTypography.titleMedium,
                                    color = Gray1
                                )
                            }
                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = "${formatterVND(uiState.amount)} VND",
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Text
                                ),
                                enable = false,
                                onValueChange = {}
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row {
                                Text(
                                    text = "Phí giao dịch",
                                    style = CustomTypography.titleMedium,
                                    color = Gray1
                                )
                            }
                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = "1.000",
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Text
                                ),
                                enable = false,
                                onValueChange = {}
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row {
                                Text(
                                    text = "Nội dung chuyển khoản",
                                    style = CustomTypography.titleMedium,
                                    color = Gray1
                                )
                            }
                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = uiState.description,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Text
                                ),
                                enable = false,
                                onValueChange = {}
                            )
                        }
                        if (!uiState.expenseType.isNullOrEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                Row {
                                    Text(
                                        text = "Phân loại",
                                        style = CustomTypography.titleMedium,
                                        color = Gray1
                                    )
                                }
                                CustomTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = uiState.expenseType,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done,
                                        keyboardType = KeyboardType.Text
                                    ),
                                    enable = false,
                                    onValueChange = {}
                                )
                            }

                        }

                    }
                }
                //navigationbar
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        15.dp,
                        alignment = Alignment.Bottom
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Phương thức xác thực",
                            style = CustomTypography.titleMedium,
                            color = Gray1
                        )
                        CustomDropdownField(
                            modifier = Modifier.fillMaxWidth(),
                            options = listOf("SMS Email", "Biometric"),
                            onOptionSelected = {},
                            selectedOption = "",
                            placeholder = "Chọn phương thức xác thực"
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {

                        CustomTextButton(
                            enable = uiState.confirmState !is StateType.LOADING,
                            onClick = {
                                onConfirmClick()
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
    if (uiState.isOtpShow) {
        OtpDialogCustom(
            otpLength = 6,
            otpValue = uiState.otp,
            onOtpChange = { onOtpChange(it) },
            onDismiss = { onOtpDismiss() }
        )
    }


}

@Preview(
    showSystemUi = true,
)
@Composable
fun ConfirmPreview() {
    ConfirmPaymentScreen(
        uiState = ConfirmUiState(
            isOtpShow = true,
            service = Service.TRANSFER
        ),
        onBackClick = { },
        onConfirmClick = { },
        onOtpChange = { },
        onOtpDismiss = {}
    )
}