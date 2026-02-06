package com.example.ibanking_kltn.ui.screens.setting


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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.CustomConfirmDialog
import com.example.ibanking_kltn.utils.CustomSwitchButton
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.DefaultImageProfile
import com.example.ibanking_kltn.utils.InformationLine
import com.example.ibanking_kltn.utils.LoadingScaffold


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    uiState: SettingUiState,
    userComponent: @Composable () -> Unit,
    navigationBar: @Composable () -> Unit,
    onEvent: (SettingEvent) -> Unit,
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
    LoadingScaffold(
        isLoading = uiState.screenState == StateType.LOADING
    ) {
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
                    userComponent()
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
                            if (uiState.avatarUrl == null) {
                                DefaultImageProfile(
                                    modifier = Modifier
                                        .size(100.dp),
                                    name = uiState.fullName
                                )
                            } else {

                                AsyncImage(
                                    model = uiState.avatarUrl,
                                    contentDescription = "Avatar",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = uiState.fullName,
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
                                    onEvent(
                                        SettingEvent.NavigateToMyProfile
                                    )
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
                                    onEvent(
                                        SettingEvent.NavigateToChangePasswordScreen
                                    )
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
                                onClick = {
                                    isShowConfirmPasswordBiometric = true

                                },
                                enable = true,
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
                                enable = true,
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
                dismissText = "Không",
                confirmText = "Đăng xuất",
                onDismiss = {
                    isShowConfirmLogout = false
                },
                onConfirm = {
                    onEvent(
                        SettingEvent.Logout
                    )
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
                        style = AppTypography.bodyMedium.copy(
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
                dismissText = "Hủy",
                confirmText = "Xác nhận",
                onDismiss = {
                    isShowConfirmPasswordBiometric = false
                },
                onConfirm = {
                    onEvent(SettingEvent.SwitchBiometric)
                    isShowConfirmPasswordBiometric = false
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
                                style = AppTypography.bodyMedium,
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
                            onEvent(SettingEvent.ChangeConfirmPassword(it))
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
//    SettingScreen(
//        uiState = SettingUiState(),
//        onViewProfileClick = {},
//        onChangePasswordClick = {},
//        onClickBiometric = {},
//        onLogout = {},
//        onChangePassword = {},
//        navigationBar = {},
//        onError = {},
//        fullName = "Nguyễn Văn A",
//        avatarUrl = null
//    )
}