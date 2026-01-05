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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.data.dtos.PayLaterApplicationType
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.Red1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.PayLaterApplicationUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.CustomTextButton
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.formatterVND

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayLaterApplicationScreen(
    uiState: PayLaterApplicationUiState,
    onBackClick: () -> Unit,
    onLimitChange: (String) -> Unit,
    onChangeReason: (String) -> Unit,
    onConfirmClick: () -> Unit,
) {
    val scrollState = rememberScrollState(0)
    val focusManager = LocalFocusManager.current
    LoadingScaffold(
        isLoading = uiState.screenState is StateType.LOADING,
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Nộp đơn",
                            style = AppTypography.titleMedium
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
                        .padding(vertical = 10.dp)
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "Loại yêu cầu",
                                    style = AppTypography.bodyLarge,
                                    color = Gray1
                                )
                                Row(modifier = Modifier.padding(horizontal = 5.dp)) {

                                    CustomTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = "",
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Next,
                                            keyboardType = KeyboardType.Text
                                        ),
                                        enable = false,
                                        onValueChange = {
                                        },
                                        placeholder = {
                                            Text(
                                                text=uiState.applicationType.typeName,
                                                style = AppTypography.bodyMedium,
                                                color = Gray2
                                            )
                                        }
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "Hạn mức mong muốn",
                                    style = AppTypography.bodyLarge,
                                    color = Gray1
                                )
                                Row(modifier = Modifier.padding(horizontal = 5.dp)) {

                                    CustomTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = formatterVND(uiState.requestLimit),
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Next,
                                            keyboardType = KeyboardType.Number
                                        ),
                                        enable = true,
                                        onValueChange = {
                                            onLimitChange(it)
                                        },
                                        placeholder = {

                                        }
                                    )
                                }
                                    if(uiState.requestLimit < 5000000){
                                        Text(
                                            text = "(Tối thiểu 5.000.000 VND)",
                                            style = AppTypography.bodySmall,
                                            color = Red1,
                                            modifier = Modifier.padding(horizontal = 5.dp)
                                        )
                                    }

                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "Lý do(nếu có)",
                                    style = AppTypography.bodyLarge,
                                    color = Gray1
                                )
                                Row(modifier = Modifier.padding(horizontal = 5.dp)) {

                                    CustomTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = uiState.reason ?: "",
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Next,
                                            keyboardType = KeyboardType.Number
                                        ),
                                        enable = true,
                                        onValueChange = {
                                            onChangeReason(it)
                                        },
                                        placeholder = {
                                            Text(
                                                text = "Nhập lý do",
                                                style = AppTypography.bodySmall,
                                                color = Gray2
                                            )
                                        }
                                    )
                                }
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
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CustomTextButton(
                            enable = uiState.requestLimit >= 5000000,
                            onClick = {
                                onConfirmClick()
                            },
                            isLoading = false,
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(R.string.Confirm),
                        )

                    }
                }


            }

        }

    }


}


@Preview(
    showSystemUi = true,
)
@Composable
fun PayLaterApplicationScreenPreview() {
    PayLaterApplicationScreen(
        uiState = PayLaterApplicationUiState(
            screenState = StateType.SUCCESS,
            applicationType = PayLaterApplicationType.ACTIVATION,
            requestLimit = 5000000L,
            reason = "Mở rộng kinh doanh",
        ),
        onBackClick = {},
        onLimitChange = {},
        onChangeReason = {},
        onConfirmClick = {},
    )
}