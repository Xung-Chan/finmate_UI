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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.AuthUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.CustomTextButton
import com.example.ibanking_kltn.utils.CustomTextField

@Composable
fun SignInScreen(
    uiState: AuthUiState,
    onLoginClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onChangeVisiblePassword: () -> Unit,
    checkEnableLogin: () -> Boolean,
    onForgotPasswordClick: () -> Unit,
    onBiometricClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = White3
    ) { paddingValues ->

        Column(
            modifier = Modifier
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
                        .height(200.dp)
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
                        style = CustomTypography.headlineMedium,
                        color = Blue1
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.LoginScreen_Text),
                        style = CustomTypography.titleSmall,
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
                                style = CustomTypography.titleMedium,
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
                                style = CustomTypography.titleMedium,
                                color = Gray2
                            )
                        },
                        isPasswordField = true,
                        isPasswordShow = uiState.isPasswordShow,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),

                        keyboardActions = KeyboardActions (
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
                                    .shadow(
                                        elevation = 30.dp, shape = CircleShape
                                    )
                                    .clickable { onChangeVisiblePassword() }
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
                        style = CustomTypography.titleSmall,
                        color = Gray2,
                        modifier = Modifier.clickable {
                            onForgotPasswordClick()
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
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(10.dp)
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(
//                            10.dp,
//                            Alignment.CenterHorizontally
//                        ),
//                    ) {
//                        Text(
//                            text = "Bạn chưa có tài khoản?",
//                            style = CustomTypography.titleSmall,
//                            color = Black1
//                        )
//                        Text(
//                            text = stringResource(R.string.LoginScreen_SignupLabel),
//                            style = CustomTypography.titleSmall,
//                            color = Blue1,
//                            modifier = Modifier.clickable {}
//                        )
//                    }
//                }

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
        uiState = AuthUiState(),
        onLoginClick = { },
        onEmailChange = { },
        onPasswordChange = { },
        onChangeVisiblePassword = { },
        checkEnableLogin = {
            false
        },
        onForgotPasswordClick = { },
        onBiometricClick = {}
    )
}