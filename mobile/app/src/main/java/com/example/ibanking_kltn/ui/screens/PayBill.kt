package com.example.ibanking_kltn.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayBillScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.PayBill_Title))
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
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
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
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)

                ) {
                    Text(
                        text = stringResource(R.string.PayBill_ElectricityBill),
                        style = CustomTypography.titleMedium,
                        color = Black1
                    )
                    Text(
                        text = stringResource(R.string.ElectricBill_Description),
                        style = CustomTypography.bodyMedium,
                        color = Gray2
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.electric_bill),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
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
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)

                ) {
                    Text(
                        text = stringResource(R.string.PayBill_InternetBill),
                        style = CustomTypography.titleMedium,
                        color = Black1
                    )
                    Text(
                        text = stringResource(R.string.InternetBill_Description),
                        style = CustomTypography.bodyMedium,
                        color = Gray2
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.internet_bill),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
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
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)

                ) {
                    Text(
                        text = stringResource(R.string.PayBill_WaterBill),
                        style = CustomTypography.titleMedium,
                        color = Black1
                    )
                    Text(
                        text = stringResource(R.string.WaterBill_Description),
                        style = CustomTypography.bodyMedium,
                        color = Gray2
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.water_bill),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true

)
@Composable
fun PayBillPreview() {
    PayBillScreen()
}
