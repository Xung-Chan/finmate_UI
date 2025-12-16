package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray4
import com.example.ibanking_kltn.ui.theme.Red1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.utils.CustomTextButton
import com.example.ibanking_kltn.utils.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen() {
    val scrollState = rememberScrollState(0)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.TransactionHistory_Title))
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
            modifier = Modifier.padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CustomTextField(
                    value = "", onValueChange = {}, placeholder = {
                        Text(stringResource(id = R.string.TransactionHistory_SearchHint))
                    }, leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = null
                        )
                    }, keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search, keyboardType = KeyboardType.Text
                    ), shape = RoundedCornerShape(10.dp), modifier = Modifier.weight(1f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(5.dp)
                        .shadow(
                            elevation = 30.dp, shape = CircleShape
                        )
                        .clickable { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.filter_enable),
                        contentDescription = null,
                        tint = Gray1,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CustomTextButton(
                    text = "Electric",
                    onClick = {},
                    enable = true,
                )
                CustomTextButton(
                    text = "Water", buttonColor = ButtonDefaults.textButtonColors(
                        containerColor = Gray4,
                        contentColor = Black1,
                    ), onClick = {}, enable = true
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(state = scrollState)
            ) {
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
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Nguyen Van A",
                                style = CustomTypography.titleMedium,
                                color = Black1,

                                )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "30/11/2025",
                                style = CustomTypography.titleSmall,
                                color = Gray1,
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Status",
                                style = CustomTypography.titleSmall,
                                color = Gray1,

                                )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "30/11/2025",
                                style = CustomTypography.titleSmall,
                                color = Red1,
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Amount",
                                style = CustomTypography.titleSmall,
                                color = Gray1,

                                )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "500,000 VND",
                                style = CustomTypography.titleSmall,
                                color = Blue1,
                            )
                        }
                    }
                }
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
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Nguyen Van A",
                                style = CustomTypography.titleMedium,
                                color = Black1,

                                )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "30/11/2025",
                                style = CustomTypography.titleSmall,
                                color = Gray1,
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Status",
                                style = CustomTypography.titleSmall,
                                color = Gray1,

                                )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "30/11/2025",
                                style = CustomTypography.titleSmall,
                                color = Red1,
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Amount",
                                style = CustomTypography.titleSmall,
                                color = Gray1,
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "500,000 VND",
                                style = CustomTypography.titleSmall,
                                color = Blue1,
                            )
                        }
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
fun HistoryPreview() {
    TransactionHistoryScreen()
}
