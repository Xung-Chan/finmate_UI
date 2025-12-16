package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Red1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.utils.DashedDivider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.TransactionDetail_Title))
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
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
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Name",
                            style = CustomTypography.titleMedium,
                            color = Gray1,

                            )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Nguyen Van A",
                            style = CustomTypography.titleSmall,
                            color = Black1,
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Address",
                            style = CustomTypography.titleMedium,
                            color = Gray1,

                            )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Nguyen Van A",
                            style = CustomTypography.titleSmall,
                            color = Black1,
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Số điện thoại",
                            style = CustomTypography.titleMedium,
                            color = Gray1,

                            )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Nguyen Van A",
                            style = CustomTypography.titleSmall,
                            color = Black1,
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mã giao dịch",
                            style = CustomTypography.titleMedium,
                            color = Gray1,

                            )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Nguyen Van A",
                            style = CustomTypography.titleSmall,
                            color = Black1,
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ngày",
                            style = CustomTypography.titleMedium,
                            color = Gray1,

                            )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Nguyen Van A",
                            style = CustomTypography.titleSmall,
                            color = Black1,
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Số tiền",
                            style = CustomTypography.titleMedium,
                            color = Gray1,

                            )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "1.000.000 VND",
                            style = CustomTypography.titleSmall,
                            color = Blue1,
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DashedDivider(
                        color = Gray1,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Phí giao dịch",
                            style = CustomTypography.titleMedium,
                            color = Gray1,

                            )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "5.000 VND",
                            style = CustomTypography.titleSmall,
                            color = Blue1,
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DashedDivider(
                        color = Gray1,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tổng cộng",
                            style = CustomTypography.titleLarge,
                            color = Black1,

                            )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "5.000 VND",
                            style = CustomTypography.titleLarge,
                            color = Red1,
                        )
                    }
                }

            }


        }
    }
}

@Preview(
    showBackground = true, showSystemUi = true

)
@Composable
fun TransactionDetailPreview() {
    TransactionDetailScreen()
}
