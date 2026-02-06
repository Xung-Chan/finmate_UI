package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Blue3
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3

@Composable
fun SectionTitle(text: String, icon: ImageVector? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = Blue3.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Blue3
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            text = text,
            style = AppTypography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Blue1
        )
    }
}

@Composable
fun SectionBody(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 44.dp)
    ) {
        Text(
            text = text,
            style = AppTypography.bodyMedium,
            lineHeight = 22.sp,
            color = Gray1
        )
    }
}

@Composable
fun TermSection(
    title: String,
    body: String,
    icon: ImageVector? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = White1
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SectionTitle(text = title, icon = icon)
            Spacer(modifier = Modifier.height(8.dp))
            SectionBody(text = body)
        }
    }
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
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
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
                    titleContentColor = Black1,
                    containerColor = White3
                ),
            )
        },
        modifier = Modifier.systemBarsPadding(),
        containerColor = Gray2
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Blue3
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = White1
                    )
                    Text(
                        text = "ĐIỀU KHOẢN VÀ ĐIỀU KIỆN",
                        style = AppTypography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = White1,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Dành cho khách hàng sử dụng ứng dụng FinMate",
                        style = AppTypography.bodyMedium,
                        color = White1.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Term Sections
            TermSection(
                title = "1. Phạm vi áp dụng",
                body = "Điều khoản và Điều kiện này áp dụng cho toàn bộ người dùng sử dụng các sản phẩm và dịch vụ do ứng dụng cung cấp, bao gồm nhưng không giới hạn ở các dịch vụ thanh toán, chuyển tiền, ví điện tử và các dịch vụ tài chính liên quan.",
                icon = Icons.Default.Public
            )

            TermSection(
                title = "2. Điều kiện sử dụng dịch vụ",
                body = "Người dùng phải có đầy đủ năng lực hành vi dân sự theo quy định pháp luật Việt Nam. Người dùng có trách nhiệm cung cấp thông tin chính xác, đầy đủ và cập nhật khi đăng ký và sử dụng dịch vụ.",
                icon = Icons.Default.Person
            )

            TermSection(
                title = "3. Bảo mật thông tin",
                body = "Người dùng có trách nhiệm bảo mật thông tin đăng nhập, mã xác thực (OTP), và các thông tin liên quan đến tài khoản. Ứng dụng không chịu trách nhiệm đối với các thiệt hại phát sinh do người dùng để lộ thông tin bảo mật.",
                icon = Icons.Default.Lock
            )

            TermSection(
                title = "4. Giao dịch và xử lý giao dịch",
                body = "Mọi giao dịch được thực hiện thông qua ứng dụng sau khi xác thực hợp lệ sẽ được coi là giao dịch hợp pháp của người dùng. Các giao dịch đã hoàn tất không thể hủy bỏ, trừ trường hợp được pháp luật hoặc quy định nội bộ cho phép.",
                icon = Icons.Default.AccountBalance
            )

            TermSection(
                title = "5. Phí dịch vụ",
                body = "Một số dịch vụ có thể áp dụng phí. Mức phí (nếu có) sẽ được thông báo công khai tại thời điểm người dùng thực hiện giao dịch.",
                icon = Icons.Default.Receipt
            )

            TermSection(
                title = "6. Giới hạn trách nhiệm",
                body = "Ứng dụng không chịu trách nhiệm đối với các thiệt hại gián tiếp, ngẫu nhiên hoặc phát sinh do sự kiện bất khả kháng, lỗi hệ thống ngoài tầm kiểm soát hoặc do hành vi vi phạm của người dùng.",
                icon = Icons.Default.Warning
            )

            TermSection(
                title = "7. Tạm ngưng hoặc chấm dứt dịch vụ",
                body = "Ứng dụng có quyền tạm ngưng hoặc chấm dứt cung cấp dịch vụ trong trường hợp phát hiện hành vi gian lận, vi phạm pháp luật hoặc vi phạm Điều khoản và Điều kiện này.",
                icon = Icons.Default.Block
            )

            TermSection(
                title = "8. Thay đổi điều khoản",
                body = "Điều khoản và Điều kiện có thể được sửa đổi, bổ sung theo từng thời kỳ. Người dùng có trách nhiệm theo dõi và cập nhật các thay đổi này.",
                icon = Icons.Default.Refresh
            )

            TermSection(
                title = "9. Luật áp dụng",
                body = "Điều khoản và Điều kiện này được điều chỉnh và giải thích theo pháp luật nước Cộng hòa Xã hội Chủ nghĩa Việt Nam.",
                icon = Icons.Default.Gavel
            )

            // Footer Note
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Blue3.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Blue3,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Bằng việc sử dụng ứng dụng, bạn đồng ý với các điều khoản và điều kiện trên.",
                        style = AppTypography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Blue1,
                        lineHeight = 18.sp
                    )
                }
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
