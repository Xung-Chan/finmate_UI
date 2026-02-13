package com.example.ibanking_kltn.ui.screens.define_transaction

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.LoadingScaffold
import java.text.NumberFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefineTransactionScreen(
    uiState: DefineTransactionUiState,
    onEvent: (DefineTransactionEvent) -> Unit,
    onNavigateBack: () -> Unit
) {

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            onEvent(DefineTransactionEvent.SelectImage(it))
        }
    }


    LoadingScaffold(
        isLoading = uiState.screenState == StateType.LOADING
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Tạo giao dịch",
                            style = AppTypography.titleMedium
                        )
                    },

                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        titleContentColor = Black1,
                        containerColor = White3
                    ),
                )
            },
            bottomBar = {
                ActionButtons(
                    isEnabled = uiState.isEnableContinue && !uiState.isProcessingImage,
                    onSubmit = {
                        onEvent(DefineTransactionEvent.SubmitTransaction)
                    },
                    onClear = {
                        onEvent(DefineTransactionEvent.ClearForm)
                    }
                )
            },
            modifier = Modifier.systemBarsPadding()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Image Upload Section
                ImageUploadSection(
                    selectedImageUri = uiState.selectedImageUri,
                    isProcessing = uiState.isProcessingImage,
                    onSelectImage = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onClearImage = {
                        onEvent(DefineTransactionEvent.ClearImage)
                    }
                )


                // Form Section
                TransactionFormSection(
                    uiState = uiState,
                    onEvent = onEvent
                )

            }
        }
    }
}

@Composable
private fun ImageUploadSection(
    selectedImageUri: Uri?,
    isProcessing: Boolean,
    onSelectImage: () -> Unit,
    onClearImage: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tải ảnh hóa đơn",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Blue1
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (selectedImageUri != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    if (isProcessing) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }

                    IconButton(
                        onClick = onClearImage,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .background(Color.White, RoundedCornerShape(50))
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.close),
                            contentDescription = "Remove Image",
                            tint = Color.Red
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(
                            BorderStroke(2.dp, Blue1),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable(onClick = onSelectImage),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.image),
                            contentDescription = "Upload Image",
                            modifier = Modifier.size(48.dp),
                            tint = Blue1
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nhấn để chọn ảnh",
                            color = Blue1,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Hệ thống sẽ tự động điền thông tin từ ảnh",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionFormSection(
    uiState: DefineTransactionUiState,
    onEvent: (DefineTransactionEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Thông tin giao dịch",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Blue1
        )

        Spacer(modifier = Modifier.height(16.dp))

//        CustomOutlineTextField(
//            value = uiState.transactionId,
//            onValueChange = {
//                onEvent(DefineTransactionEvent.UpdateTransactionId(it))
//            },
//            label = { Text("Mã giao dịch (tùy chọn)") },
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(8.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = Blue1,
//                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
//            ),
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Text,
//                imeAction = ImeAction.Next
//            ),
//
//            )

        Spacer(modifier = Modifier.height(12.dp))

        // Destination Account Number
        OutlinedTextField(
            value = uiState.toAccountNumber,
            onValueChange = {
                onEvent(DefineTransactionEvent.UpdateDestinationAccountNumber(it))
            },
            label = { Text("Số tài khoản đích *") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue1,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Destination Account Name
        OutlinedTextField(
            value = uiState.toMerchantName,
            onValueChange = {
                onEvent(DefineTransactionEvent.UpdateDestinationAccountName(it))
            },
            label = { Text("Tên người nhận *") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue1,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Amount
        val formattedAmount = if (uiState.amount > 0) {
            NumberFormat.getNumberInstance(Locale.getDefault()).format(uiState.amount)
        } else ""

        OutlinedTextField(
            value = formattedAmount,
            onValueChange = {
                onEvent(DefineTransactionEvent.UpdateAmount(it))
            },
            label = { Text("Số tiền *") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            trailingIcon = {
                Text(
                    text = "VND",
                    modifier = Modifier.padding(end = 8.dp),
                    color = Color.Gray
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue1,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description
        OutlinedTextField(
            value = uiState.description,
            onValueChange = {
                onEvent(DefineTransactionEvent.UpdateDescription(it))
            },
            label = { Text("Nội dung chuyển khoản") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue1,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "* Trường bắt buộc",
            fontSize = 12.sp,
            color = Color.Red,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
private fun ActionButtons(
    isEnabled: Boolean,
    onSubmit: () -> Unit,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onClear,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Blue1)
        ) {
            Text(
                text = "Xóa",
                color = Blue1,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            enabled = isEnabled,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue1,
                disabledContainerColor = Color.Gray
            )
        ) {
            Text(
                text = "Xác nhận",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true

)
@Composable
fun DefineTransactionPreview() {
    DefineTransactionScreen(
        uiState = DefineTransactionUiState(),
        onEvent = {},
        onNavigateBack = {}
    )
}
