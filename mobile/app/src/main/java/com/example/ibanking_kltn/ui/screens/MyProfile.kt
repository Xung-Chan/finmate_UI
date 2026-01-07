package com.example.ibanking_kltn.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.data.dtos.TransferPayload
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Gray3
import com.example.ibanking_kltn.ui.theme.Green1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.MyProfileUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.CustomConfirmDialog
import com.example.ibanking_kltn.utils.DefaultImageProfile
import com.example.ibanking_kltn.utils.InformationLine
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.QrCodeImage
import com.example.ibanking_kltn.utils.RetryCompose
import com.example.ibanking_kltn.utils.SkeletonBox
import com.example.ibanking_kltn.utils.formatterDateString
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    uiState: MyProfileUiState,
    myWalletNumber: String,
    onBackClick: () -> Unit,
    onUpdateImageProfile: (Uri) -> Unit,
    onRetry: () -> Unit,
    onNavigateMyContacts: () -> Unit,
    onNavigateToVerificationRequest: () -> Unit,
    onNavigateToTermsAndConditions: () -> Unit,
    onSavedQrSuccess: () -> Unit,
) {
    val scrollState = rememberScrollState(0)
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        selectedImageUri = uri
    }

    var isShowMyQr by remember {
        mutableStateOf(false)
    }
    LoadingScaffold(
        isLoading = uiState.screenState is StateType.LOADING,
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Thông tin của tôi", style = AppTypography.titleMedium)
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
            if (!uiState.initialedUserInfo || !uiState.initialVerification) {
                ProfileScreenSkeleton(paddingValues)

            } else if (uiState.userInfo == null) {
                RetryCompose(
                    onRetry = {
                        onRetry()
                    },
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(paddingValues)
                        .padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            if (uiState.userInfo.avatarUrl == null) {
                                DefaultImageProfile(
                                    modifier = Modifier
                                        .size(100.dp),
                                    name = uiState.userInfo.fullName
                                )
                            } else {

                                AsyncImage(
                                    model = uiState.userInfo.avatarUrl,
                                    contentDescription = "Avatar",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = Gray3,
                                        shape = RoundedCornerShape(percent = 50)
                                    )
                                    .shadow(
                                        elevation = 10.dp,
                                        shape = RoundedCornerShape(percent = 50),
                                        ambientColor = Color.Transparent,
                                        spotColor = Color.Transparent,
                                    )
                                    .clickable {
                                        singlePhotoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                                    .padding(7.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.image),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = uiState.userInfo.fullName,
                            style = AppTypography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Blue1,
                        )
                        if (uiState.isVerified) {

                            Spacer(Modifier.width(10.dp))
                            Icon(
                                painter = painterResource(R.drawable.ok_status_bold),
                                contentDescription = null,
                                tint = Green1,
                                modifier = Modifier
                                    .size(20.dp)
                            )
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
                        Text(
                            text = "Thông tin cá nhân",
                            style = AppTypography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Black1

                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(15.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            InformationLine(
                                title = "Username",
                                trailing = {
                                    Text(
                                        text = uiState.userInfo.username,
                                        style = AppTypography.bodyMedium,
                                        color = it,
                                        textAlign = TextAlign.End
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            InformationLine(
                                title = "Họ và tên",
                                trailing = {
                                    Text(
                                        text = uiState.userInfo.fullName,
                                        style = AppTypography.bodyMedium,
                                        color = it,
                                        textAlign = TextAlign.End
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            InformationLine(
                                title = "Ngày sinh",
                                trailing = {
                                    Text(
                                        text = formatterDateString(LocalDate.parse(uiState.userInfo.dateOfBirth)),
                                        style = AppTypography.bodyMedium,
                                        color = it,
                                        textAlign = TextAlign.End
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            InformationLine(
                                title = "Email",
                                trailing = {
                                    Text(
                                        text = uiState.userInfo.mail,
                                        style = AppTypography.bodyMedium,
                                        color = it,
                                        textAlign = TextAlign.End
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            InformationLine(
                                title = "CCCD",
                                trailing = {
                                    Text(
                                        text = uiState.userInfo.cardId,
                                        style = AppTypography.bodyMedium,
                                        color = it,
                                        textAlign = TextAlign.End
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            InformationLine(
                                title = "Sô điện thoại",
                                trailing = {
                                    Text(
                                        text = uiState.userInfo.phoneNumber,
                                        style = AppTypography.bodyMedium,
                                        color = it,
                                        textAlign = TextAlign.End
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            InformationLine(
                                title = "Địa chỉ",
                                trailing = {
                                    Text(
                                        text = uiState.userInfo.address,
                                        style = AppTypography.bodyMedium,
                                        color = it,
                                        textAlign = TextAlign.End

                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )

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
                        Text(
                            text = "Tiện ích",
                            style = AppTypography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Black1

                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(15.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            InformationLine(
                                title = "Mã QR của tôi",
                                color = Black1,
                                leading = {
                                    Icon(
                                        painter = painterResource(R.drawable.qr),
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp),
                                        tint = it
                                    )
                                },
                                trailing = {
                                    Icon(

                                        imageVector = Icons.Default.ArrowForwardIos,
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp), tint = it

                                    )
                                },
                                enable = true,
                                onClick = {
//                                onNavigateMyQR()
                                    isShowMyQr = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            InformationLine(
                                title = "Người nhận đã lưu",
                                color = Black1,
                                leading = {
                                    Icon(
                                        painter = painterResource(R.drawable.contact),
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp),
                                        tint = it
                                    )
                                },
                                trailing = {
                                    Icon(

                                        imageVector = Icons.Default.ArrowForwardIos,
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp), tint = it

                                    )
                                },
                                enable = true,
                                onClick = {
                                    onNavigateMyContacts()
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            if (!uiState.isVerified) {
                                InformationLine(
                                    title = "Xác thực tài khoản",
                                    color = Black1,
                                    leading = {
                                        Icon(
                                            painter = painterResource(R.drawable.verify),
                                            contentDescription = null,
                                            modifier = Modifier.size(25.dp),
                                            tint = it
                                        )
                                    },
                                    trailing = {
                                        Icon(

                                            imageVector = Icons.Default.ArrowForwardIos,
                                            contentDescription = null,
                                            modifier = Modifier.size(25.dp), tint = it

                                        )
                                    },
                                    enable = true,
                                    onClick = {
                                        onNavigateToVerificationRequest()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                )

                            }
                            InformationLine(
                                title = "Điều khoản - Điều kiện",
                                color = Black1,
                                leading = {
                                    Icon(
                                        painter = painterResource(R.drawable.note),
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp),
                                        tint = it
                                    )
                                },
                                trailing = {
                                    Icon(

                                        imageVector = Icons.Default.ArrowForwardIos,
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp), tint = it

                                    )
                                },
                                enable = true,
                                onClick = {
                                    onNavigateToTermsAndConditions()
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )

                        }

                    }
                }

            }
        }


        if (selectedImageUri != null) {
            CustomConfirmDialog(
                dimissText = "Không",
                confirmText = "Cập nhật",
                onDimiss = {
                    selectedImageUri = null
                },
                onConfirm = {
                    onUpdateImageProfile(selectedImageUri!!)
                    selectedImageUri = null
                }
            ) {
                AsyncImage(
                    model = if (selectedImageUri == null) "https://placehold.jp/150x150.png" else selectedImageUri,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = "Xác nhận cập nhật avatar",
                        style = AppTypography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),

                        )
                }
            }

        }
        if (isShowMyQr) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Gray3.copy(alpha = 0.5f),
                    )
                    .padding(20.dp)
                    .pointerInput(Unit) {
                    },
                contentAlignment = Alignment.Center
            ) {

                Box(
                    contentAlignment = Alignment.TopEnd,
                    modifier = Modifier
                        .background(
                            color = White1,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()

                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        QrCodeImage(
                            content = TransferPayload(
                                toWalletNumber = myWalletNumber
                            ),
                            size = 200.dp,
                            onSavedSuccess = {

                                onSavedQrSuccess()
                            },
                        )
                    }
                    Row(
                        modifier = Modifier
                            .shadow(
                                elevation = 10.dp,
                                shape = CircleShape,
                                ambientColor = Color.Transparent,
                                spotColor = Color.Transparent
                            )
                            .clickable {
                                isShowMyQr = false
                            }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.close), contentDescription = null,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }
        }
    }

}


@Composable
fun ProfileHeaderSkeleton() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SkeletonBox(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            shape = CircleShape
        )

        SkeletonBox(
            modifier = Modifier
                .width(180.dp)
                .height(22.dp)
        )
    }
}

@Composable
fun PersonalInfoSkeleton(lines: Int = 7) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 30.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SkeletonBox(
            modifier = Modifier
                .width(150.dp)
                .height(18.dp)
        )

        repeat(lines) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SkeletonBox(
                    modifier = Modifier
                        .width(120.dp)
                        .height(14.dp)
                )
                SkeletonBox(
                    modifier = Modifier
                        .width(140.dp)
                        .height(14.dp)
                )
            }
        }
    }
}

@Composable
fun UtilityMenuSkeleton(items: Int = 4) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(30.dp, RoundedCornerShape(20.dp))
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SkeletonBox(
            modifier = Modifier
                .width(100.dp)
                .height(18.dp)
        )

        repeat(items) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                SkeletonBox(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape),
                    shape = CircleShape
                )
                Spacer(modifier = Modifier.width(12.dp))
                SkeletonBox(
                    modifier = Modifier
                        .height(14.dp)
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
fun ProfileScreenSkeleton(
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ProfileHeaderSkeleton()
        PersonalInfoSkeleton()
        UtilityMenuSkeleton()
    }
}


//@Preview(
//    showBackground = true,
//    showSystemUi = true
//)
//@Composable
//fun MyProfileScreenPreview() {
//    MyProfileScreen(
//        uiState = MyProfileUiState(
//            userInfo = UserInfoResponse(
//                id = "user-id-123",
//                username = "mocked-username",
//                fullName = "Mocked User",
//                mail = "mocked-mail",
//                status = UserStatusEnum.ACTIVE,
//                phoneNumber = "1234567890",
//                avatarUrl = "https://placehold.jp/150x150.png",
//                dateOfBirth = [2004,1,1],
//                gender = "Male",
//                address = "123 Mock St, Mock City",
//                cardId = "ID123456789"
//
//            )
////            userInfo = null
//        ),
//        onBackClick = {},
//        onUpdateImageProfile = {},
//        onRetry = {},
//        onNavigateMyContacts = {},
//        onNavigateToVerificationRequest = {},
//        onNavigateToTermsAndConditions = {},
//        myWalletNumber = "1234567890",
//        onSavedQrSuccess = {}
//    )
//}