package com.example.ibanking_kltn.ui.screens.confirm


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.utils.CustomDropdownField
import com.example.ibanking_kltn.utils.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPaymentScreen(
) {
    val scrollState = rememberScrollState(0)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Xác nhận")
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
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
                .padding(paddingValues)
                .padding(horizontal=20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(state = scrollState)
            ) {
                Column(
                    modifier=Modifier.fillMaxWidth(),
                    verticalArrangement= Arrangement.spacedBy(10.dp)
                ){
                    Row{
                        Text(
                            text = "Loại giao dịch",
                            style = CustomTypography.titleMedium,
                            color = Gray1
                        )
                    }
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value="Chuyển khoản nội bộ",
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        enable=false,
                        onValueChange = {}
                    )
                }
                Column(
                    modifier=Modifier.fillMaxWidth(),
                    verticalArrangement= Arrangement.spacedBy(10.dp)
                ){
                    Row{
                        Text(
                            text = "Từ",
                            style = CustomTypography.titleMedium,
                            color = Gray1
                        )
                    }
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value="0123456789",
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        enable=false,
                        onValueChange = {}
                    )
                }
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
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Thông tin người nhận",
                            style = CustomTypography.titleMedium,
                            color = Gray1
                        )
                    }
                    Column(
                    modifier=Modifier.fillMaxWidth().padding(horizontal=10.dp),
                    verticalArrangement= Arrangement.spacedBy(10.dp)
                ){
                    Row{
                        Text(
                            text = "Họ tên",
                            style = CustomTypography.titleMedium,
                            color = Gray1
                        )
                    }
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value="Nguyễn Văn A",
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        enable=false,
                        onValueChange = {}
                    )
                }
                    Column(
                    modifier=Modifier.fillMaxWidth().padding(horizontal=10.dp),
                    verticalArrangement= Arrangement.spacedBy(10.dp)
                ){
                    Row{
                        Text(
                            text = "Số tài khoản",
                            style = CustomTypography.titleMedium,
                            color = Gray1
                        )
                    }
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value="0123456789",
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        enable=false,
                        onValueChange = {}
                    )
                }
                }
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
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Thông tin giao dịch",
                            style = CustomTypography.titleMedium,
                            color = Gray1
                        )
                    }
                    Column(
                    modifier=Modifier.fillMaxWidth().padding(horizontal=10.dp),
                    verticalArrangement= Arrangement.spacedBy(10.dp)
                ){
                    Row{
                        Text(
                            text = "Số tiền",
                            style = CustomTypography.titleMedium,
                            color = Gray1
                        )
                    }
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value="10.000",
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        enable=false,
                        onValueChange = {}
                    )
                }
                    Column(
                    modifier=Modifier.fillMaxWidth().padding(horizontal=10.dp),
                    verticalArrangement= Arrangement.spacedBy(10.dp)
                ){
                    Row{
                        Text(
                            text = "Phí giao dịch",
                            style = CustomTypography.titleMedium,
                            color = Gray1
                        )
                    }
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value="1.000",
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        enable=false,
                        onValueChange = {}
                    )
                }
                    Column(
                    modifier=Modifier.fillMaxWidth().padding(horizontal=10.dp),
                    verticalArrangement= Arrangement.spacedBy(10.dp)
                ){
                    Row{
                        Text(
                            text = "Nội dung chuyển khoản",
                            style = CustomTypography.titleMedium,
                            color = Gray1
                        )
                    }
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value="Từ A tới B",
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        enable=false,
                        onValueChange = {}
                    )
                }
                    Column(
                    modifier=Modifier.fillMaxWidth().padding(horizontal=10.dp),
                    verticalArrangement= Arrangement.spacedBy(10.dp)
                ){
                    Row{
                        Text(
                            text = "Phân loại",
                            style = CustomTypography.titleMedium,
                            color = Gray1
                        )
                    }
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value="Ăn uống",
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        enable=false,
                        onValueChange = {}
                    )
                }
                }

            }

            //navigationbar
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp, alignment = Alignment.Bottom),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Column(modifier=Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)){
                    Text(text="Phương thức xác thực",style=CustomTypography.titleMedium,color=Gray1)
                    CustomDropdownField(
                        modifier = Modifier.fillMaxWidth(),
                        options = listOf("SMS Email","Biometric"),
                        onOptionSelected = {}
                    ) {
                        Text(
                            "Chọn phương thức xác thực",
                            style = CustomTypography.titleMedium,
                            color = Gray2,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Blue1, shape = RoundedCornerShape(30))
                    ) {
                        Text(
                            "Xác nhận", style = CustomTypography.titleLarge,
                            color = White1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

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
fun ConfirmPreview() {
    ConfirmPaymentScreen()
}