package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.data.dtos.AccountType
import com.example.ibanking_kltn.data.dtos.ServiceType
import com.example.ibanking_kltn.data.dtos.SortOption
import com.example.ibanking_kltn.data.dtos.TransactionStatus
import com.example.ibanking_kltn.data.dtos.responses.TransactionHistoryResponse
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue3
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray3
import com.example.ibanking_kltn.ui.theme.Green1
import com.example.ibanking_kltn.ui.theme.Orange1
import com.example.ibanking_kltn.ui.theme.Red1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.TransactionHistoryUiState
import com.example.ibanking_kltn.utils.CustomTextField
import com.example.ibanking_kltn.utils.TransactionHistoryFilterDialog
import com.example.ibanking_kltn.utils.formatterDateString
import com.example.ibanking_kltn.utils.formatterVND
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    uiState: TransactionHistoryUiState,
    transactions: LazyPagingItems<TransactionHistoryResponse>,
    onErrorLoading: (String) -> Unit,
    onClickFilter: () -> Unit,
    onBackClick: () -> Unit,
    onApply: (
        TransactionStatus?,
        ServiceType?,
        AccountType?,
        SortOption,
    ) -> Unit,
    onResetAll: () -> Unit,
    onViewDetail: (TransactionHistoryResponse) -> Unit,
) {
    val refreshState = rememberPullToRefreshState()
    var selectedStatus by remember {
        mutableStateOf<TransactionStatus?>(null)
    }

    var selectedService by remember {
        mutableStateOf<ServiceType?>(null)
    }

    var selectedAccountType by remember {
        mutableStateOf<AccountType?>(null)
    }

    var selectedSort by remember {
        mutableStateOf(SortOption.NEWEST)
    }



    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(R.string.TransactionHistory_Title))
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            onBackClick()
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
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(10.dp)

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = {
                            Text(stringResource(id = R.string.TransactionHistory_SearchHint))
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search, keyboardType = KeyboardType.Text
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .shadow(
                                elevation = 30.dp,
                                ambientColor = Color.Transparent,
                                spotColor = Color.Transparent,
                                shape = RoundedCornerShape(20.dp),
                            )
                            .clickable {
                                onClickFilter()
                            },
                    ) {
                        Box(
                            contentAlignment = Alignment.TopEnd,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.filter),
                                contentDescription = null,
                                modifier = Modifier.size(50.dp)
                            )

                        }

                    }
                }

                PullToRefreshBox(
                    isRefreshing = transactions.loadState.refresh is LoadState.Loading,
                    state = refreshState,
                    onRefresh = {
                        transactions.refresh()
                    },
                    indicator = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {

                            PullToRefreshDefaults.Indicator(
                                state = refreshState,
                                isRefreshing = transactions.loadState.refresh is LoadState.Loading,
                                containerColor = White1,
                                color = Blue3,
                            )
                        }

                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)

                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {

                        items(
                            count = transactions.itemCount,
                        ) { item ->
                            val transaction = transactions[item]
                            if (transaction == null) {
                                return@items
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
                                    .clickable {
                                        onViewDetail(transaction)
                                    }
                                    .background(color = White1, shape = RoundedCornerShape(20.dp))
                                    .padding(20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = transaction.description,
                                            style = CustomTypography.titleMedium,
                                            color = Black1,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = formatterDateString(
                                                LocalDateTime.parse(
                                                    transaction.processedAt
                                                ).toLocalDate()
                                            ),
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
                                            text = TransactionStatus.entries.first { t -> t.name == transaction.status }.status,
                                            style = CustomTypography.titleSmall,
                                            color = when (transaction.status) {
                                                TransactionStatus.COMPLETED.name -> Green1
                                                TransactionStatus.PENDING.name -> Orange1
                                                else -> Red1
                                            },
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
                                            text = "${formatterVND(transaction.amount.toLong())} VND",
                                            style = CustomTypography.titleSmall,
                                            color = Black1,
                                        )
                                    }
                                }
                            }
                        }
                        when (val state = transactions.loadState.append) {
                            is LoadState.NotLoading -> Unit
                            is LoadState.Loading -> {
                                item {
                                    CircularProgressIndicator(
                                        color = Blue3, modifier = Modifier.size(30.dp)
                                    )
                                }
                            }

                            is LoadState.Error -> {
                                onErrorLoading(
                                    state.error.message
                                        ?: "Tải dữ liệu không thành công. Vui lòng thử lại sau."
                                )
                            }
                        }
                    }
                }


            }
        }
        if (uiState.isShowFilter) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Gray3.copy(alpha = 0.5f),
                    )
                    .padding(20.dp)
                    .pointerInput(Unit) {}) {
                TransactionHistoryFilterDialog(
                    selectedStatus = selectedStatus,
                    selectedSort = selectedSort,
                    selectedAccountType = selectedAccountType,
                    selectedService = selectedService,
                    onSelectStatus = {
                        selectedStatus = it
                    },
                    onSelectSort = {
                        selectedSort = it
                    },
                    onSelectService = {
                        selectedService = it
                    },
                    onSelectAccountType = {
                        selectedAccountType = it
                    },
                    onResetStatusFilter = {
                        selectedStatus = null
                    },
                    onResetSortFilter = {
                        selectedSort = SortOption.NEWEST
                    },
                    onResetServiceFilter = {
                        selectedService = null
                    },
                    onResetAccountTypeFilter = {
                        selectedAccountType = null
                    },
                    onResetAll = {
                        selectedStatus = null
                        selectedService = null
                        selectedAccountType = null
                        selectedSort = SortOption.NEWEST
                        onResetAll()
                    },
                    onApply = {
                        onApply(
                            selectedStatus,
                            selectedService,
                            selectedAccountType,
                            selectedSort
                        )
                    },
                    onDismiss = {
                        selectedStatus = uiState.selectedStatus
                        selectedSort = uiState.selectedSort
                        selectedService = uiState.type
                        selectedAccountType = uiState.accountType
                        onClickFilter()
                    })
            }

        }

    }
}

//@Preview(
//    showBackground = true, showSystemUi = true
//
//)
//@Composable
//fun HistoryPreview() {
//    TransactionHistoryScreen()
//}
