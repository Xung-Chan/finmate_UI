package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import com.example.ibanking_kltn.utils.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedReceiverScreen() {
    val scrollState = rememberScrollState(0)
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                shape = CircleShape,
                containerColor = Blue1
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    tint = White1
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text =stringResource(id = R.string.SavedReceiver_Title),
                    )
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
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CustomTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(stringResource(id = R.string.SavedReceiver_SearchHint))
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                        keyboardType = KeyboardType.Text
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(
                        horizontal = 20.dp,
                        vertical = 10.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Chị 2",
                            style = CustomTypography.bodyMedium.copy(
                                color = Black1
                            )
                        )
                        Text(
                            text = "Nguyễn Văn A",
                            style = CustomTypography.bodySmall.copy(
                                color = Gray1
                            )
                        )
                        Text(
                            text = "Techcombank - 19036099999999",
                            style = CustomTypography.bodySmall.copy(
                                color = Gray1
                            )
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.more),
                        contentDescription = null,
                        tint = Gray1,
                        modifier = Modifier.size(25.dp)
                    )

                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Gray2
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Chị 2",
                            style = CustomTypography.bodyMedium.copy(
                                color = Black1
                            )
                        )
                        Text(
                            text = "Nguyễn Văn A",
                            style = CustomTypography.bodySmall.copy(
                                color = Gray1
                            )
                        )
                        Text(
                            text = "Techcombank - 19036099999999",
                            style = CustomTypography.bodySmall.copy(
                                color = Gray1
                            )
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.more),
                        contentDescription = null,
                        tint = Gray1,
                        modifier = Modifier.size(25.dp)
                    )

                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Gray2
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
fun SavedReceiverPreview() {
    SavedReceiverScreen()
}
