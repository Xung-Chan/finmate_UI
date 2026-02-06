package com.example.ibanking_kltn.ui.screens.wallet.verification


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Blue5
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.Red1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.CustomDropdownField
import com.example.ibanking_kltn.utils.CustomTextButton
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.LoadingScaffold
import com.example.ibanking_kltn.utils.formatReadableSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateVerificationRequestScreen(
    uiState: CreateVerificationRequestUiState,
    onBackClick: () -> Unit,
    onEvent:(CreateVerificationEvent)->Unit,
) {
    val scrollState = rememberScrollState(0)
    val focusManager = LocalFocusManager.current
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris: List<Uri> ->
        onEvent(CreateVerificationEvent.AddFile(uris))
    }
    LoadingScaffold(
        isLoading = uiState.screenState is StateType.LOADING
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Xác thực danh nghiệp")
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
                            Row {
                                Text(
                                    text = "Thông tin doanh nghiệp",
                                    style = AppTypography.bodyMedium,
                                    color = Gray1
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 5.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                CustomTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = uiState.invoiceDisplayName,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Next,
                                        keyboardType = KeyboardType.Text
                                    ),
                                    enable = true,
                                    onValueChange = {
                                        onEvent(CreateVerificationEvent.ChangeInvoiceDisplayName(it))
                                    },
                                    placeholder = {
                                        Text(
                                            text = "Tên hiển thị hóa đơn",
                                            style = AppTypography.bodyMedium,
                                            color = Gray2
                                        )
                                    }
                                )
                                CustomTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = uiState.businessName,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Next,
                                        keyboardType = KeyboardType.Text
                                    ),
                                    enable = true,
                                    onValueChange = {
                                        onEvent(CreateVerificationEvent.ChangeBusinessName(it))
                                    },
                                    placeholder = {
                                        Text(
                                            text = "Tên danh nghiệp",
                                            style = AppTypography.bodyMedium,
                                            color = Gray2
                                        )
                                    }
                                )
                                CustomTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = uiState.businessCode,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Next,
                                        keyboardType = KeyboardType.Text
                                    ),
                                    enable = true,
                                    onValueChange = {
                                        onEvent(CreateVerificationEvent.ChangeBusinessCode(it))
                                    },
                                    placeholder = {
                                        Text(
                                            text = "Mã số doanh nghiệp",
                                            style = AppTypography.bodyMedium,
                                            color = Gray2
                                        )
                                    }
                                )
                                CustomTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = uiState.businessAddress,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Next,
                                        keyboardType = KeyboardType.Text
                                    ),
                                    enable = true,
                                    onValueChange = {
                                        onEvent(CreateVerificationEvent.ChangeBusinessAddress(it))
                                    },
                                    placeholder = {
                                        Text(
                                            text = "Địa chỉ",
                                            style = AppTypography.bodyMedium,
                                            color = Gray2
                                        )
                                    }
                                )
                                CustomTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = uiState.contactEmail,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Next,
                                        keyboardType = KeyboardType.Text
                                    ),
                                    enable = true,
                                    onValueChange = {
                                        onEvent(CreateVerificationEvent.ChangeContactEmail(it))
                                    },
                                    placeholder = {
                                        Text(
                                            text = "Email liên hệ",
                                            style = AppTypography.bodyMedium,
                                            color = Gray2
                                        )
                                    }
                                )
                                CustomTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = uiState.contactPhone,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Next,
                                        keyboardType = KeyboardType.Number
                                    ),
                                    enable = true,
                                    onValueChange = {
                                        onEvent(CreateVerificationEvent.ChangeContactPhone(it))
                                    },
                                    placeholder = {
                                        Text(
                                            text = "Số điện thoại liên hệ",
                                            style = AppTypography.bodyMedium,
                                            color = Gray2
                                        )
                                    }
                                )

                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row {
                                Text(
                                    text = "Người đại diện pháp lý",
                                    style = AppTypography.bodyMedium,
                                    color = Gray1
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 5.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)

                            ) {
                                CustomTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = uiState.representativeName,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done,
                                        keyboardType = KeyboardType.Text
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focusManager.clearFocus()
                                        }
                                    ),
                                    enable = true,
                                    onValueChange = {
                                        onEvent(CreateVerificationEvent.ChangeRepresentativeName(it))
                                    },
                                    placeholder = {
                                        Text(
                                            text = "Tên người đại diện",
                                            style = AppTypography.bodyMedium,
                                            color = Gray2
                                        )
                                    }
                                )
                                CustomDropdownField<IdType>(
                                    modifier = Modifier.fillMaxWidth(),
                                    options = IdType.entries,
                                    onOptionSelected = {
                                        onEvent(CreateVerificationEvent.SelectType(it))
                                    },
                                    optionsComposable = {
                                        Text(
                                            text = it.name,
                                            style = AppTypography.bodySmall,
                                            color = Black1
                                        )
                                    },
                                    selectedOption = uiState.representativeIdType,
                                    placeholder = "Loại giấy tờ",
                                )
                                if (
                                    uiState.representativeIdType != ""
                                ){

                                    CustomTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = uiState.representativeIdNumber,
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Done,
                                            keyboardType = KeyboardType.Text
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                focusManager.clearFocus()
                                            }
                                        ),
                                        enable = true,
                                        onValueChange = {
                                            onEvent(CreateVerificationEvent.ChangeRepresentativeIdNumber(it))
                                        },
                                        placeholder ={
                                            Text(
                                                text = uiState.representativeIdType,
                                                style = AppTypography.bodyMedium,
                                                color = Gray2
                                            )
                                        }

                                        )
                                }

                            }

                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Tài liệu đính kèm (.pdf/.doc/.docx)",
                                    style = AppTypography.bodyMedium,
                                    color = Gray1,
                                    modifier = Modifier.weight(1f)
                                )
                                Row(
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(onClick = {
                                        filePickerLauncher.launch(
                                            arrayOf(
                                                "application/pdf",
                                                "application/msword",
                                                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                                            )
                                        )
                                    }) {
                                        Text(
                                            text = "Thêm đính kèm",
                                            color = Blue1,
                                            style = AppTypography.bodySmall.copy(
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 5.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)

                            ) {
                                uiState.documents.forEach {
                                    FileItem(
                                        fileExention = if (it.extension == FileExtension.PDF.ext) FileExtension.PDF else FileExtension.DOCX,
                                        fileName = it.fileName,
                                        size = it.size,
                                        onDeleteClick = {
                                            onEvent(CreateVerificationEvent.DeleteFile(it))
                                        },
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
                            enable = uiState.isConfirmEnabled,
                            onClick = {
                                onEvent(CreateVerificationEvent.SubmitVerificationRequest)
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


private enum class FileExtension(val ext: String) {
    PDF("pdf"),
    DOC("doc"),
    DOCX("docx"),
}

@Composable
private fun FileItem(

    fileName: String,
    size: Long,
    fileExention: FileExtension,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (fileExention == FileExtension.PDF) {
            Icon(
                painter = painterResource(id = R.drawable.pdf),
                contentDescription = null,
                tint = Red1,
                modifier = Modifier.size(25.dp)
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.word),
                contentDescription = null,
                tint = Blue5,
                modifier = Modifier.size(25.dp)
            )

        }

        Column {
            Text(
                text = fileName,
                style = AppTypography.bodySmall,
                color = Black1
            )
            Text(
                text = formatReadableSize(
                    context = context,
                    size = size
                ),
                style = AppTypography.labelMedium,
                color = Gray2
            )
        }
        Row(
            modifier = Modifier
                .shadow(
                    elevation = 5.dp,
                    shape = RoundedCornerShape(5.dp),
                    spotColor = Color.Transparent,
                    ambientColor = Color.Transparent,
                )
                .clickable {
                    onDeleteClick()
                },
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.close),
                contentDescription = null,
                tint = Gray2,
            )
        }
    }
}

//@Preview(
//    showSystemUi = true,
//)
//@Composable
//fun VerificxationRequestPreview() {
//    CreateVerificationRequestScreen(
//        uiState = CreateVerificationRequestUiState(
//            documents = listOf(
//                FileInfo(
//                    uri = Uri.EMPTY,
//                    fileName = "Document1.pdf",
//                    extension = "pdf",
//                    size = 204800L
//                ),
//                FileInfo(
//                    uri = Uri.EMPTY,
//                    fileName = "Document2.docx",
//                    extension = "docx",
//                    size = 512000L
//                )
//            ),
//        ),
//        onBackClick = {},
//        onDeleteFile = {},
//        onConfirmClick = {},
//        isEnableConfirm = false,
//        onAddFile = {},
//        onSelectType = {},
//        onChangeInvoiceDisplayName = {},
//        onChangeBusinessName = {},
//        onChangeBusinessCode = {},
//        onChangeBusinessAddress = {},
//        onChangeContactEmail = {},
//        onChangeContactPhone = {},
//        onChangeRepresentativeName = {},
//        onChangeRepresentativeIdNumber = {},
//    )
//}