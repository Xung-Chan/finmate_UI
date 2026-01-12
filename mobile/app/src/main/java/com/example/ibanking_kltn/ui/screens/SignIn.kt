package com.example.ibanking_kltn.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.data.dtos.RequestOtpPurpose
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.AuthUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.CustomConfirmDialog
import com.example.ibanking_kltn.utils.CustomTextButton
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.customClick

@Composable
fun SignInScreen(
    uiState: AuthUiState,
    onLoginClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onChangeVisiblePassword: () -> Unit,
    checkEnableLogin: () -> Boolean,
    onRequestOtp: (RequestOtpPurpose) -> Unit,
    onBiometricClick: () -> Unit,
    onDeleteLastLoginUser: () -> Unit,
) {
    var isShowConfirmDeleteLastUser by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    Box {
        Scaffold(
            modifier = Modifier,
            containerColor = White3
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bg_1),
                        contentDescription = null,
                        modifier = Modifier
                            .height(180.dp)
                            .fillMaxWidth()
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.LoginScreen_Welcome),
                            style = AppTypography.headlineMedium,
                            color = Blue1
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.LoginScreen_Text),
                            style = AppTypography.bodySmall,
                            color = Black1
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.login),
                            contentDescription = null,
                            modifier = Modifier
                                .height(160.dp)
                                .width(200.dp)
                        )
                    }
                    if (uiState.fullName == null) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        ) {
                            CustomTextField(
                                value = uiState.username,
                                placeholder = {
                                    Text(
                                        text = stringResource(R.string.LoginScreen_UsernameLabel),
                                        style = AppTypography.bodyMedium,
                                        color = Gray2
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                onValueChange = { onEmailChange(it) }
                            )
                        }
                    } else {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.user_rounded),
                                contentDescription = null,
                            )
                            Row(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Xin chào, ${uiState.fullName}",
                                    style = AppTypography.bodyMedium,
                                    color = Gray1,
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .shadow(
                                        elevation = 30.dp, shape = CircleShape,
                                        ambientColor = Color.Transparent,
                                        spotColor = Color.Transparent
                                    )
                                    .clickable {
                                        isShowConfirmDeleteLastUser = true
                                    }

                                    .padding(
                                        5.dp
                                    )
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.logout),
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        CustomTextField(
                            value = uiState.password,
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.LoginScreen_PasswordLabel),
                                    style = AppTypography.bodyMedium,
                                    color = Gray2
                                )
                            },
                            isPasswordField = true,
                            isPasswordShow = uiState.isPasswordShow,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),

                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = {
                                onPasswordChange(it)
                            },
                            trailingIcon = {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .customClick{ onChangeVisiblePassword() }
                                ) {
                                    Icon(
                                        imageVector = if (uiState.isPasswordShow) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = null,
                                        tint = Gray1,
                                    )
                                }
                            }
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 20.dp,
                            )
                    ) {
                        Text(
                            text = stringResource(R.string.LoginScreen_ForgotPassword),
                            style = AppTypography.bodyMedium,
                            color = Gray2,
                            modifier = Modifier.clickable {
                                onRequestOtp(RequestOtpPurpose.PASSWORD_RESET)
                            }
                        )

                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        CustomTextButton(
                            enable = checkEnableLogin(),
                            onClick = {
                                focusManager.clearFocus()
                                onLoginClick()
                            },
                            isLoading = uiState.loginState is StateType.LOADING,
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(R.string.LoginScreen_ButtonText),
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                                .shadow(
                                    elevation = 30.dp,
                                    shape = CircleShape,
                                    ambientColor = Black1.copy(alpha = 0.25f),
                                    spotColor = Black1.copy(alpha = 0.25f)
                                )
                                .clickable {
                                    onBiometricClick()

                                }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.fingerprint),
                                contentDescription = null,
                                tint = Blue1,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        Text(
                            text="Kích hoạt tài khoản?",
                            style=AppTypography.bodySmall,
                            color=Gray1
                        )
                        Text(
                            text="Kích hoạt",
                            style=AppTypography.bodyMedium,
                            color=Blue1,
                            modifier=Modifier.customClick {
                                onRequestOtp(RequestOtpPurpose.EMAIL_VERIFICATION)
                            }
                        )
                    }


                }
            }
        }
        if (isShowConfirmDeleteLastUser) {
            CustomConfirmDialog(
                dimissText = "Không",
                confirmText = "Đăng xuất",
                onDimiss = {
                    isShowConfirmDeleteLastUser = false
                },
                onConfirm = {
                    onDeleteLastLoginUser()
                    isShowConfirmDeleteLastUser = false
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.logout_illustration),
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp)
                        .width(150.dp)
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = "Đăng xuất tài khoản?",
                        style = AppTypography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),

                        )
                    Text(
                        text = "Khi đăng xuất bạn sẽ không nhận được các thông báo từ tài khoản này nữa.",
                        style = AppTypography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = Gray1
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
fun PreviewLogin() {
    SignInScreen(
        uiState = AuthUiState(
            fullName = "Nguyen Van A"
        ),
        onLoginClick = { },
        onEmailChange = { },
        onPasswordChange = { },
        onChangeVisiblePassword = { },
        checkEnableLogin = {
            false
        },
        onRequestOtp = { },
        onBiometricClick = {},
        onDeleteLastLoginUser = {}
    )
}