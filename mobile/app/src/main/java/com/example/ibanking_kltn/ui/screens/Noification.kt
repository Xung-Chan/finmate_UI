package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.dtos.definitions.NotificationType
import com.example.ibanking_kltn.dtos.responses.NotificationResponse
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Blue3
import com.example.ibanking_kltn.ui.theme.Blue5
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.NotificationUiState
import com.example.ibanking_kltn.utils.customClick
import com.example.ibanking_kltn.utils.formateDateTimeString
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    uiState: NotificationUiState,
    notifications: LazyPagingItems<NotificationResponse>,
    onChangType: () -> Unit,
    onBackClick: () -> Unit,
    onErrorLoading: (String) -> Unit,
) {
    val refreshState = rememberPullToRefreshState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Thông báo",
                        style = AppTypography.titleMedium
                    )
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
        },

        modifier = Modifier.systemBarsPadding(), containerColor = White3
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (uiState.selectedType) {
                    NotificationType.SYSTEM -> {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(
                                10.dp,
                                Alignment.CenterHorizontally
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = Blue5.copy(
                                        alpha = 0.2f
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(5.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.system),
                                contentDescription = null,
                                tint = Blue1,
                                modifier = Modifier.size(25.dp)
                            )
                            Text(
                                text = "Hệ thống",
                                style = AppTypography.bodyMedium,
                                color = Blue1
                            )
                        }
                        Row(

                            horizontalArrangement = Arrangement.spacedBy(
                                10.dp,
                                Alignment.CenterHorizontally
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .customClick {
                                    onChangType()
                                }
                                .padding(5.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.personal),
                                contentDescription = null,
                                tint = Black1,
                                modifier = Modifier
                                    .size(25.dp)
                            )
                            Text(
                                text = "Cá nhân",
                                style = AppTypography.bodyMedium,
                                color = Black1
                            )
                        }
                    }

                    NotificationType.PERSONAL -> {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(
                                10.dp,
                                Alignment.CenterHorizontally
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .customClick {
                                    onChangType()
                                }
                                .padding(5.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.system),
                                contentDescription = null,
                                tint = Black1,
                                modifier = Modifier.size(25.dp)
                            )
                            Text(
                                text = "Hệ thống",
                                style = AppTypography.bodyMedium,
                                color = Black1
                            )
                        }

                        Row(

                            horizontalArrangement = Arrangement.spacedBy(
                                10.dp,
                                Alignment.CenterHorizontally
                            ), verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = Blue5.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(5.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.personal),
                                contentDescription = null,
                                tint = Blue1,
                                modifier = Modifier.size(25.dp)
                            )
                            Text(
                                text = "Cá nhân",
                                style = AppTypography.bodyMedium,
                                color = Blue1
                            )
                        }
                    }

                }
            }
            PullToRefreshBox(
                isRefreshing = notifications.loadState.refresh is LoadState.Loading,
                state = refreshState,
                onRefresh = {
                    notifications.refresh()
                },
                indicator = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {

                        PullToRefreshDefaults.Indicator(
                            state = refreshState,
                            isRefreshing = notifications.loadState.refresh is LoadState.Loading,
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
                    modifier = Modifier.fillMaxSize()
                    ) {
                    if (notifications.itemCount == 0) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Không có thông báo nào",
                                    style = AppTypography.bodyMedium,
                                    color = Gray1,
                                )
                            }
                        }
                    }
                    items(
                        items = notifications.itemSnapshotList,
                    ) { item ->
                        if (item == null) return@items
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()

                                .border(
                                    width = 1.dp,
                                    color = Blue5.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)

                        ) {
                            Text(
                                text = LocalDateTime.parse(item.createdAt).formateDateTimeString(),
                                style = AppTypography.bodySmall,
                                color = Gray2
                            )
                            Text(
                                text = item.content,
                                style = AppTypography.bodyMedium,
                                color = Black1
                            )

                        }


                    }
                    when (val state = notifications.loadState.append) {
                        is LoadState.NotLoading -> Unit
                        is LoadState.Loading -> {
                            item {
                                CircularProgressIndicator(
                                    color = Blue3, modifier = Modifier.size(30.dp)
                                )
                            }
                        }

                        is LoadState.Error -> {
                            //                                onErrorLoading("Tải dữ liệu không thành công. Vui lòng thử lại sau.")
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


}

@Preview(
    showBackground = true, showSystemUi = true

)
@Composable
fun NotifyPreview() {
    val pagingData = PagingData.from(
        listOf<NotificationResponse>(
            NotificationResponse(
                id = "1",
                title = "Thông báo 1",
                content = "Nội dung thông báo 1",
                createdAt = "2024-10-01T10:00:00",
                isRead = false
            ),
            NotificationResponse(
                id = "1",
                title = "Thông báo 1",
                content = "Nội dung thông báo 1",
                createdAt = LocalDateTime.now().toString(),
                isRead = false
            ),
            NotificationResponse(
                id = "1",
                title = "Thông báo 1",
                content = "Nội dung thông báo 1",
                createdAt = LocalDateTime.now().minusDays(1).toString(),
                isRead = false
            ),
        )
    )
    NotificationScreen(
        uiState = NotificationUiState(),
        notifications = flowOf(pagingData).collectAsLazyPagingItems(),
        onChangType = {},
        onBackClick = {},
        onErrorLoading = {}
    )
}
