package com.example.ibanking_kltn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ibanking_kltn.data.dtos.RequestOtpPurpose
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.ForgotPasswordStep
import com.example.ibanking_kltn.ui.uistates.ForgotPasswordUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.CustomTextButton
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.LoadingScaffold
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    uiState: ForgotPasswordUiState,
    onFindUsernameClick: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onSendOtpClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onConfirmOtpClick: () -> Unit,
    onOtpChange: (String) -> Unit,
    onResetPasswordClick: () -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onBackToEnterUsernameClick: () -> Unit,
    onBackToEnterEmailClick: () -> Unit,
    onBackClick: () -> Unit,
    onChangeVisiblePassword: () -> Unit

) {
    LoadingScaffold(
        isLoading = uiState.screenState is StateType.LOADING,
    )
    {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = when (uiState.purpose) {
                                RequestOtpPurpose.PASSWORD_RESET -> stringResource(id = R.string.ForgotPassword_Title)
                                RequestOtpPurpose.EMAIL_VERIFICATION -> "Kích hoạt tài khoản"
                            }
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
                    .padding(20.dp)
            ) {
                when (uiState.currentStep) {

                    ForgotPasswordStep.ENTER_USERNAME -> {
                        EnterUsername(
                            uiState = uiState,
                            onFindUsernameClick = onFindUsernameClick,
                            onUsernameChange = onUsernameChange
                        )
                    }


                    ForgotPasswordStep.ENTER_EMAIL -> {
                        EnterEmail(
                            uiState = uiState,
                            onEmailChange = onEmailChange,
                            onSendOtpClick = onSendOtpClick,
                            onBackToEnterUsernameClick = onBackToEnterUsernameClick
                        )

                    }

                    ForgotPasswordStep.CONFIRM_OTP -> {
                        EnterOTP(
                            uiState = uiState,
                            onOtpChange = onOtpChange,
                            onConfirmOtpClick = onConfirmOtpClick,
                            onResendOtpClick = onSendOtpClick,
                            onBackToEnterEmailClick = onBackToEnterEmailClick
                        )
                    }


                    ForgotPasswordStep.RESET_PASSWORD -> {
                        EnterNewPassword(
                            uiState = uiState,
                            onNewPasswordChange = onNewPasswordChange,
                            onResetPasswordClick = onResetPasswordClick,
                            onBackToEnterEmailClick = onBackToEnterEmailClick,
                            onChangeVisiblePassword = onChangeVisiblePassword
                        )
                    }
                }

            }
        }
    }

}

@Composable
fun EnterOTP(
    uiState: ForgotPasswordUiState,
    onOtpChange: (String) -> Unit,
    onConfirmOtpClick: () -> Unit,
    onResendOtpClick: () -> Unit,
    onBackToEnterEmailClick: () -> Unit
) {
    var countdownTime by remember { mutableIntStateOf(60) }
    var startCountDown by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(startCountDown) {
        if (startCountDown) {
            while (countdownTime > 0) {
                delay(1000L)
                countdownTime--
            }
            startCountDown = false
            countdownTime = 60
        }
    }

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
                .padding(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.ForgotPassword_OTP_Label),
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
                value = uiState.otp,
                placeholder = {
                    Text(
                        stringResource(id = R.string.ForgotPassword_OTP),
                        style = AppTypography.bodySmall,
                        color = Gray2
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                onValueChange = {
                    onOtpChange(it)
                }
            )
            CustomTextButton(
                text = "Gửi lại ${if (startCountDown) "($countdownTime s)" else ""}",
                onClick = {
                    focusManager.clearFocus()
                    onResendOtpClick()
                    startCountDown = true
                },
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp),
                style = AppTypography.bodySmall,
                enable = uiState.isEnableResendOtp && !startCountDown
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.ForgotPassword_Notify),
                style = AppTypography.bodyMedium,
                color = Black1,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "OTP sẽ hết hạn sau 60 giây",
                style = AppTypography.bodyMedium,
                color = Black1,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(id = R.string.ForgotPassword_Change_Email),
                style = AppTypography.bodyMedium,
                color = Blue1,
                modifier = Modifier.clickable {
                    onBackToEnterEmailClick()
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
                onClick = {
                    focusManager.clearFocus()
                    onConfirmOtpClick()
                },
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.Continue),
                enable = uiState.otp.isNotEmpty()
            )


        }
    }
}

@Composable
fun EnterEmail(
    uiState: ForgotPasswordUiState,
    onEmailChange: (String) -> Unit,
    onSendOtpClick: () -> Unit,
    onBackToEnterUsernameClick: () -> Unit
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
        val focusManager = LocalFocusManager.current

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = "Chúng tôi sẽ gửi một OTP đến email ${uiState.maskedEmail} của bạn.",
                style = AppTypography.bodyMedium,
                color = Black1
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.ForgotPassword_Email_Label),
                style = AppTypography.bodyMedium,
                color = Gray1
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CustomTextField(
                value = uiState.email,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.ForgotPassword_Email),
                        style = AppTypography.bodySmall,
                        color = Gray2
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    onEmailChange(it)
                }
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            Text(
                text = "Thay đổi username",
                style = AppTypography.bodyMedium,
                color = Blue1,
                modifier = Modifier.clickable {
                    onBackToEnterUsernameClick()
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
                onClick = {
                    focusManager.clearFocus()
                    onSendOtpClick()
                },
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.Continue),
                enable = uiState.email.isNotEmpty()
            )


        }
    }
}

@Composable
fun EnterNewPassword(
    uiState: ForgotPasswordUiState,
    onNewPasswordChange: (String) -> Unit,
    onResetPasswordClick: () -> Unit,
    onBackToEnterEmailClick: () -> Unit,
    onChangeVisiblePassword: () -> Unit
) {
    val focusManager = LocalFocusManager.current

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
                .padding(10.dp)
        ) {
            Text(
                text = "Nhập mật khẩu mới",
                style = AppTypography.bodyMedium,
                color = Gray1
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CustomTextField(
                value = uiState.newPassword,
                isPasswordField = true,
                isPasswordShow = uiState.isShowPassword,
                placeholder = {
                    Text(
                        text = "Mật khẩu mới",
                        style = AppTypography.bodySmall,
                        color = Gray2
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    onNewPasswordChange(it)
                },
                trailingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(5.dp)
                            .shadow(
                                elevation = 30.dp, shape = CircleShape
                            )
                            .clickable { onChangeVisiblePassword() }
                    ) {
                        Icon(
                            imageVector = if (uiState.isShowPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Gray1,
                        )
                    }
                }
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.ForgotPassword_Notify_3),
                style = AppTypography.bodyMedium,
                color = Black1
            )
            Text(
                text = "Thay đổi email",
                style = AppTypography.bodyMedium,
                color = Blue1,
                modifier = Modifier.clickable {
                    onBackToEnterEmailClick()
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
                onClick = {
                    focusManager.clearFocus()
                    onResetPasswordClick()
                },
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.Continue),
                enable = uiState.newPassword.isNotEmpty()
            )


        }
    }
}

@Composable
fun EnterUsername(
    uiState: ForgotPasswordUiState,
    onFindUsernameClick: () -> Unit,
    onUsernameChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
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
                text = stringResource(id = R.string.ForgotPassword_Username_Label),
                style = AppTypography.bodyMedium,
                color = Gray1
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            CustomTextField(
                value = uiState.username,
                placeholder = {
                    Text(
                        text = "Username",
                        style = AppTypography.bodySmall,
                        color = Gray2
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    onUsernameChange(it)
                }
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CustomTextButton(
                onClick = {
                    focusManager.clearFocus()
                    onFindUsernameClick()
                },
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.Continue),
                enable = uiState.username.isNotEmpty()
            )


        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true

)
@Composable
fun ForgotPasswordPreview() {
    ForgotPasswordScreen(
        uiState = ForgotPasswordUiState(
            currentStep = ForgotPasswordStep.ENTER_EMAIL
        ),
        onFindUsernameClick = {},
        onUsernameChange = {},
        onSendOtpClick = {},
        onEmailChange = {},
        onConfirmOtpClick = {},
        onOtpChange = {},
        onResetPasswordClick = {},
        onNewPasswordChange = {},
        onBackToEnterEmailClick = {},
        onBackToEnterUsernameClick = {},
        onBackClick = {},
        onChangeVisiblePassword = {}

    )
}