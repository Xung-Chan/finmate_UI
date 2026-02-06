package com.example.ibanking_kltn.ui.screens.auth.change_password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.Green1
import com.example.ibanking_kltn.ui.theme.Red2
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.CustomTextButton
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.customClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    uiState: ChangePasswordUiState,
    onBackClick: () -> Unit,
    onEvent: (ChangePasswordEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    var isShowOldPassword by remember {
        mutableStateOf(false)
    }
    var isShowNewPassword by remember {
        mutableStateOf(false)
    }
    LoadingScaffold(
        isLoading = uiState.screenState is StateType.LOADING
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.ChangePassword_Title))
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
                    .padding(20.dp)
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
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.ChangePassword_OldPassword),
                            style = AppTypography.bodyMedium,
                            color = Gray1
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        CustomTextField(
                            value = uiState.oldPassword,
                            placeholder = {
                                Text(
                                    "Nhập mật khẩu cũ",
                                    style = AppTypography.bodyMedium,
                                    color = Gray2
                                )
                            },
                            isPasswordField = true,
                            isPasswordShow = isShowOldPassword,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next
                            ),
                            trailingIcon = {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .customClick  {
                                            isShowOldPassword = !isShowOldPassword
                                        }
                                ) {
                                    Icon(
                                        imageVector = if (isShowOldPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = null,
                                        tint = Gray1,
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            onValueChange = {
                                onEvent(
                                    ChangePasswordEvent.ChangeOldPassword(it)
                                )
                            }
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.ChangePassword_NewPassword),
                            style = AppTypography.bodyMedium,
                            color = Gray1
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        CustomTextField(
                            value = uiState.newPassword,
                            placeholder = {
                                Text(
                                    "Nhập mật khẩu mới",
                                    style = AppTypography.bodyMedium,
                                    color = Gray2
                                )
                            },
                            isPasswordField = true,
                            isPasswordShow = isShowNewPassword,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            trailingIcon = {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    modifier = Modifier
                                        .padding(horizontal = 15.dp)
                                ) {
                                    if (uiState.newPassword.isNotEmpty()) {


                                        if (uiState.isValidNewPassword) {
                                            Icon(
                                                painter = painterResource(R.drawable.ok_status),
                                                contentDescription = null,
                                                tint = Green1,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        } else

                                            Icon(
                                                painter = painterResource(
                                                    R.drawable.error
                                                ),
                                                contentDescription = null,
                                                tint = Red2,
                                                modifier = Modifier.size(20.dp)
                                            )


                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .customClick { isShowNewPassword = !isShowNewPassword }
                                    ) {
                                        Icon(
                                            imageVector = if (isShowNewPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = null,
                                            tint = Gray1,
                                        )
                                    }
                                }
                            },

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            onValueChange = {
                                onEvent(
                                    ChangePasswordEvent.ChangeNewPassword(it)
                                )
                            }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        Text(
                            stringResource(R.string.ChangePassword_ValidPassword),
                            style = AppTypography.labelMedium,
                            color = Gray1
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        CustomTextButton(
                            onClick = {
                                focusManager.clearFocus()
                                onEvent(ChangePasswordEvent.ConfirmChangePassword)
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(id = R.string.ChangePassword_Title),
                            enable = uiState.oldPassword.isNotEmpty() && uiState.newPassword.isNotEmpty() && uiState.isValidNewPassword
                        )


                    }
                }
            }
        }
    }

}


//@Preview(
//    showBackground = true,
//    showSystemUi = true
//
//)
//@Composable
//fun ChangePasswordPreview() {
//    ChangePasswordScreen(
//        uiState = ChangePasswordUiState(
//            oldPassword = "oldpassword",
//            newPassword = "newpassword",
//            isValidNewPassword = false
//        ),
//        onChangeOldPassword = {},
//        onChangeNewPassword = {},
//        onConfirmChangePassword = {},
//        onBackClick = {},
//        isEnableConfirm = true,
//        onChangeVisibleOldPassword = {},
//        onChangeVisibleNewPassword = {}
//    )
//}