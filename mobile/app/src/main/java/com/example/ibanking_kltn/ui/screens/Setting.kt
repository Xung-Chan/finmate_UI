package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.SettingUiState
import com.example.ibanking_kltn.utils.CustomConfirmDialog
import com.example.ibanking_kltn.utils.CustomSwitchButton
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.InformationLine


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    uiState: SettingUiState,
    onViewProfileClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onClickBiometric: () -> Unit,
    onLogout: () -> Unit,
    onChangePassword: (String) -> Unit,
    navigationBar: @Composable () -> Unit,
    onError: (String) -> Unit ,
) {
    var isShowConfirmLogout by remember {
        mutableStateOf(false)
    }
    var isShowConfirmPasswordBiometric by remember {
        mutableStateOf(false)
    }
    var isVisiblePassword by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    val bottomBarHeight = 100.dp
    val scrollState = rememberScrollState(0)
    Box {
        Scaffold(
            modifier = Modifier.systemBarsPadding()
        ) { paddingValues ->
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Blue1)
                    .padding(paddingValues)

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    //user infor row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                space = 10.dp,
                                alignment = Alignment.Start
                            ),

                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(
                                        color = White1,
                                        shape = CircleShape
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    tint = Black1,
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp)
                                )

                            }
                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    "2021.03.04", color = White1,
                                    style = CustomTypography.titleMedium
                                )
                                Text(
                                    "Hi John", color = White1,
                                    style = CustomTypography.titleMedium
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = {},
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(
                                        Alignment.CenterVertically
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = White1,
                                    modifier = Modifier.size(35.dp)
                                )
                            }
                            IconButton(
                                onClick = {},
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(
                                        Alignment.CenterVertically
                                    )
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.question),
                                    contentDescription = null,
                                    tint = White1,
                                    modifier = Modifier.size(35.dp)
                                )
                            }
                        }
                    }
                    //Main container
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = White3,
                                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                            )
                            .padding(30.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Image(
                                painter = painterResource(R.drawable.profile),
                                contentDescription = null,
                                modifier = Modifier.size(100.dp)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "FinMate",
                                style = AppTypography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Black1
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            InformationLine(
                                title = "Thông tin của tôi",
                                color = Gray1,
                                trailing = {
                                    Icon(

                                        imageVector = Icons.Default.ArrowForwardIos,
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp), tint = it

                                    )
                                },
                                enable = true,
                                onClick = {
                                        onViewProfileClick()
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            InformationLine(
                                title = "Đổi mật khẩu",
                                color = Gray1,
                                trailing = {
                                    Icon(

                                        imageVector = Icons.Default.ArrowForwardIos,
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp), tint = it

                                    )
                                },
                                enable = true,
                                onClick = {
                                        onChangePasswordClick()
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            InformationLine(
                                title = "Sinh trắc học",
                                color = Gray1,
                                trailing = {
                                    CustomSwitchButton(
                                        value = uiState.isEnableBiometric,
                                        buttonWidth = 50.dp,
                                        buttonHeight = 20.dp,
                                        onClick = {
                                            isShowConfirmPasswordBiometric = true
                                        }
                                    )
                                },
                                enable = false,
                                modifier = Modifier.fillMaxWidth(),
                            )
                            InformationLine(
                                title = "Đăng xuất",
                                color = Gray1,
                                trailing = {
                                    Icon(
                                        painter = painterResource(R.drawable.logout),
                                        contentDescription = null,
                                        tint = Gray1,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                enable =true,
                                onClick = {
                                        isShowConfirmLogout = true

                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Spacer(modifier = Modifier.height(bottomBarHeight * 5 / 8))

                        }
                    }
                }

                navigationBar()
            }
        }

        if (isShowConfirmLogout) {
            CustomConfirmDialog(
                dimissText = "Không",
                confirmText = "Đăng xuất",
                onDimiss = {
                    isShowConfirmLogout = false
                },
                onConfirm = {
                    onLogout()
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
                        text = "Xác nhận đăng xuất",
                        style = CustomTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),

                        )
                }
            }

        }
        if (isShowConfirmPasswordBiometric) {
            CustomConfirmDialog(
                dimissText = "Hủy",
                confirmText = "Xác nhận",
                onDimiss = {
                    isShowConfirmPasswordBiometric = false
                },
                onConfirm = {
                    if (uiState.confirmPassword.isNotEmpty()) {
                        onClickBiometric()
                        isShowConfirmPasswordBiometric = false

                    } else {
                        onError("Mật khẩu không thể để trống")
                    }
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.biometric_illustration),
                    contentDescription = null,
                    modifier = Modifier
                        .height(150.dp)
                        .width(200.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    CustomTextField(
                        value = uiState.confirmPassword,
                        placeholder = {
                            Text(
                                text = "Nhập mật khẩu hiện tại",
                                style = CustomTypography.titleMedium,
                                color = Gray2
                            )
                        },
                        isPasswordField = true,
                        isPasswordShow = isVisiblePassword,
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
                            onChangePassword(it)
                        },
                        trailingIcon = {

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .shadow(
                                        elevation = 30.dp, shape = CircleShape
                                    )
                                    .clickable {
                                        isVisiblePassword = !isVisiblePassword
                                    }
                            ) {
                                Icon(
                                    imageVector = if (isVisiblePassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Gray1,
                                )
                            }
                        }
                    )
                }


            }

        }

    }
}

@Preview(
    showSystemUi = true,
    showBackground = true

)
@Composable
fun SettingPreview() {
    SettingScreen(
        uiState = SettingUiState(),
        onViewProfileClick = {},
        onChangePasswordClick = {},
        onClickBiometric = {},
        onLogout = {},
        onChangePassword = {},
        navigationBar = {},
        onError = {}
    )
}