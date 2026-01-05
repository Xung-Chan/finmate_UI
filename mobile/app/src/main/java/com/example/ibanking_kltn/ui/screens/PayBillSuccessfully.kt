package com.example.ibanking_kltn.ui.screens




import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.utils.CustomTextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayBillSuccessfullyScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.Home))
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
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.paybill_successfully),
                    contentDescription = null,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier=Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ){
                Text(
                    text = stringResource(id = R.string.Transaction_Success),
                    style = AppTypography.titleSmall,
                    color = Blue1
                )
                Text(
                    text = stringResource(id = R.string.Transaction_Success_Message),
                    style = AppTypography.bodySmall,
                    color = Black1,
                    textAlign = TextAlign.Center
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    CustomTextButton(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = AppTypography.bodyMedium,
                        text = stringResource(id = R.string.BackToHome),
                        enable = true
                    )


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
fun PayBillSuccessfullyPreview() {
    PayBillSuccessfullyScreen()
}