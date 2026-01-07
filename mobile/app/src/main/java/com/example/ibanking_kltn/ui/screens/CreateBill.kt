package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.data.dtos.responses.ExpenseType
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.Gray3
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.CreateBillUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.CustomClickField
import com.example.ibanking_kltn.utils.CustomDatePicker
import com.example.ibanking_kltn.utils.CustomDropdownField
import com.example.ibanking_kltn.utils.CustomTextButton
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.formatterDateString
import com.example.ibanking_kltn.utils.formatterVND
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBillScreen(
    uiState: CreateBillUiState,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    isEnableContinue: Boolean,
    onChangeAmount: (String) -> Unit,
    onChangeDescription: (String) -> Unit,
    onExpenseTypeChange: (ExpenseType) -> Unit,
    onDateChange: (LocalDate) -> Unit
) {
    val scrollState = rememberScrollState(0)
    val focusManager = LocalFocusManager.current
    var isShowExpireDatePicker by remember {
        mutableStateOf(false)
    }
    LoadingScaffold(isLoading = uiState.screenState is StateType.LOADING) {
        Box {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Tạo hóa đơn")
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
                                        "Nội dung",
                                        style = AppTypography.bodyMedium,
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
                            CustomDropdownField<ExpenseType>(
                                modifier = Modifier.fillMaxWidth(),
                                options = uiState.allExpenseTypeResponse,
                                onOptionSelected = {
                                    onExpenseTypeChange(it)
                                },
                                optionsComposable = {
                                    Text(
                                        text = it.name,
                                        style = AppTypography.bodySmall,
                                        color = Black1
                                    )
                                },
                                selectedOption = uiState.selectedExpenseType?.name ?: "",
                                placeholder = "Phân loại"
                            )
                            CustomClickField(
                                onClick = {
                                    isShowExpireDatePicker = true
                                },
                                placeholder = "Hạn thanh toán",
                                value = formatterDateString(uiState.expiryDate),
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

                    //navigationbar
                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            10.dp,
                            alignment = Alignment.Bottom
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        CustomTextButton(
                            enable = isEnableContinue,
                            onClick = {
                                onContinueClick()
                            },
                            isLoading = uiState.screenState is StateType.LOADING,
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(R.string.Continue),
                        )
                    }
                }

            }

            if (isShowExpireDatePicker) {
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
                            onDateChange(it)
                            isShowExpireDatePicker = false
                        },
                        onDismiss = {
                            isShowExpireDatePicker = false
                        }
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
fun CreateBillPreview() {
    CreateBillScreen(
        uiState = CreateBillUiState(
        ),
        onBackClick = {},
        isEnableContinue = false,
        onContinueClick = {},
        onChangeAmount = {},
        onChangeDescription = {},
        onExpenseTypeChange = {},
        onDateChange = {}
    )
}
