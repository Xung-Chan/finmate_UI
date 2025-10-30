package com.example.ibanking_kltn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.utils.CustomTextButton
import com.example.ibanking_kltn.utils.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.ForgotPassword_Title))
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
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            EnterOTP()
        }
    }
}

@Composable
fun EnterOTP() {
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.ForgotPassword_OTP_Label),
                style = CustomTypography.titleMedium,
                color = Gray1
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CustomTextField(
                value = "",
                placeholder = {
                    Text(
                        stringResource(id = R.string.ForgotPassword_OTP),
                        style = CustomTypography.titleMedium,
                        color = Gray2
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.weight(1f).height(50.dp),
                onValueChange = {}
            )
            CustomTextButton(
                text = stringResource(id = R.string.ForgotPassword_OTP_Resend),
                onClick = {},
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp),
                style = CustomTypography.titleSmall
            )
        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.ForgotPassword_Notify),
                style = CustomTypography.titleMedium,
                color = Black1,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(id = R.string.ForgotPassword_OTP_Timeout),
                style = CustomTypography.titleMedium,
                color = Black1,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(id = R.string.ForgotPassword_Change_Email),
                style = CustomTypography.titleMedium,
                color = Blue1,
            )

        }
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
                text = stringResource(R.string.ChangePassword_Title),
                enable = false
            )


        }
    }
}

@Composable
fun EnterEmail() {
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.ForgotPassword_Email_Label),
                style = CustomTypography.titleMedium,
                color = Gray1
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CustomTextField(
                value = "",
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.ForgotPassword_Email),
                        style = CustomTypography.titleMedium,
                        color = Gray2
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {}
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.ForgotPassword_Notify_2),
                style = CustomTypography.titleMedium,
                color = Black1
            )
        }
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
                text = stringResource(R.string.Continue),
                enable = false
            )


        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true

)
@Composable
fun ForgotPasswordPreview() {
    ForgotPasswordScreen()
}