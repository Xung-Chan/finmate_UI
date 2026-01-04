package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = AppTypography.bodyLarge.copy(
            fontWeight = FontWeight.SemiBold
        ),
        color = Black1,
        modifier = Modifier.padding(vertical = 10.dp)
    )
}

@Composable
fun SectionBody(text: String) {
    Text(
        text = text,
        style = AppTypography.bodyMedium,
        lineHeight = 20.sp,
        color = Gray1
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAndConditionsScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Điều khoản - Điều kiện",
                        style = AppTypography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = null,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Black1, containerColor = White3
                ),
            )
        }, modifier = Modifier.systemBarsPadding(), containerColor = White3
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(state = scrollState)
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)

        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 30.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Black1.copy(alpha = 0.25f),
                        spotColor = Black1.copy(alpha = 0.25f)
                    )
                    .background(color = White1, shape = RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Text(
                    text="ĐIỀU KHOẢN VÀ ĐIỀU KIỆN DÀNH CHO KHÁCH HÀNG SỬ DỤNG ỨNG DỤNG FINTMATE",
                    style=AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Blue1,
                    textAlign = TextAlign.Center,
                    modifier=Modifier.fillMaxWidth()
                )
                SectionTitle("1. Phạm vi áp dụng")
                SectionBody(
                    "Điều khoản và Điều kiện này áp dụng cho toàn bộ người dùng sử dụng các sản phẩm và dịch vụ do ứng dụng cung cấp, bao gồm nhưng không giới hạn ở các dịch vụ thanh toán, chuyển tiền, ví điện tử và các dịch vụ tài chính liên quan."
                )

                SectionTitle("2. Điều kiện sử dụng dịch vụ")
                SectionBody(
                    "Người dùng phải có đầy đủ năng lực hành vi dân sự theo quy định pháp luật Việt Nam. Người dùng có trách nhiệm cung cấp thông tin chính xác, đầy đủ và cập nhật khi đăng ký và sử dụng dịch vụ."
                )

                SectionTitle("3. Bảo mật thông tin")
                SectionBody(
                    "Người dùng có trách nhiệm bảo mật thông tin đăng nhập, mã xác thực (OTP), và các thông tin liên quan đến tài khoản. Ứng dụng không chịu trách nhiệm đối với các thiệt hại phát sinh do người dùng để lộ thông tin bảo mật."
                )

                SectionTitle("4. Giao dịch và xử lý giao dịch")
                SectionBody(
                    "Mọi giao dịch được thực hiện thông qua ứng dụng sau khi xác thực hợp lệ sẽ được coi là giao dịch hợp pháp của người dùng. Các giao dịch đã hoàn tất không thể hủy bỏ, trừ trường hợp được pháp luật hoặc quy định nội bộ cho phép."
                )

                SectionTitle("5. Phí dịch vụ")
                SectionBody(
                    "Một số dịch vụ có thể áp dụng phí. Mức phí (nếu có) sẽ được thông báo công khai tại thời điểm người dùng thực hiện giao dịch."
                )

                SectionTitle("6. Giới hạn trách nhiệm")
                SectionBody(
                    "Ứng dụng không chịu trách nhiệm đối với các thiệt hại gián tiếp, ngẫu nhiên hoặc phát sinh do sự kiện bất khả kháng, lỗi hệ thống ngoài tầm kiểm soát hoặc do hành vi vi phạm của người dùng."
                )

                SectionTitle("7. Tạm ngưng hoặc chấm dứt dịch vụ")
                SectionBody(
                    "Ứng dụng có quyền tạm ngưng hoặc chấm dứt cung cấp dịch vụ trong trường hợp phát hiện hành vi gian lận, vi phạm pháp luật hoặc vi phạm Điều khoản và Điều kiện này."
                )

                SectionTitle("8. Thay đổi điều khoản")
                SectionBody(
                    "Điều khoản và Điều kiện có thể được sửa đổi, bổ sung theo từng thời kỳ. Người dùng có trách nhiệm theo dõi và cập nhật các thay đổi này."
                )

                SectionTitle("9. Luật áp dụng")
                SectionBody(
                    "Điều khoản và Điều kiện này được điều chỉnh và giải thích theo pháp luật nước Cộng hòa Xã hội Chủ nghĩa Việt Nam."
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(
    showBackground = true, showSystemUi = true

)
@Composable
fun TermConditionPreview() {
    TermsAndConditionsScreen(
        onBack = {}
    )
}
