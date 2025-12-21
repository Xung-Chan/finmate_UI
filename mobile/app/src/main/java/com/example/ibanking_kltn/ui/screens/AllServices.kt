package com.example.ibanking_kltn.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.data.dtos.ServiceCategory
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Green1
import com.example.ibanking_kltn.ui.theme.Red1
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import com.example.ibanking_kltn.ui.uistates.AllServiceUiState

@Composable
fun ServiceComponent(
    icon: Int,
    serviceName: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    actionIcon: (@Composable () -> Unit)? = null,
    action: (() -> Unit)? = null,
) {
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = modifier

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color.Transparent,
                    spotColor = Color.Transparent
                )
                .clickable {
                    if (action == null && actionIcon == null) {
                        onClick()
                    }
                }
                .padding(
                    vertical = 10.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = serviceName,
                style = CustomTypography.bodySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Gray1,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

        }
        if (actionIcon != null) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(15.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                        ambientColor = Color.Transparent,
                        spotColor = Color.Transparent
                    )
                    .clickable(
                        onClick = {
                            action?.invoke()
                        }
                    )

            ) {
                actionIcon()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllServiceScreen(
    uiState: AllServiceUiState,
    onBackClick: () -> Unit,
    onChangeToModifyState: () -> Unit,
    onSaveFavoriteServices: (List<ServiceCategory>) -> Unit,
    navigator: Map<String, () -> Unit>,
) {

    val allServices = ServiceCategory.entries
    var favoriteServices by remember {
        mutableStateOf(uiState.favoriteServices)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Danh sách dịch vụ")
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
            modifier = Modifier
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Dịch vụ yêu thích",
                        style = CustomTypography.titleMedium,
                        color = Gray1,
                        modifier = Modifier.weight(1f)
                    )
                    if (uiState.isModifyFavorite) {

                        TextButton(
                            onClick = {
                                onSaveFavoriteServices(favoriteServices)
                            }
                        ) {
                            Text(
                                text = "Xong",
                                style = CustomTypography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Blue1
                            )
                        }
                    } else {

                        TextButton(
                            onClick = {
                                onChangeToModifyState()
                            }
                        ) {
                            Text(
                                text = "Chỉnh sửa",
                                style = CustomTypography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Blue1
                            )
                        }
                    }

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)

                ) {
                    for (i in 0 until 4) {
                        val serviceCategory = favoriteServices.getOrNull(i)
                        if (serviceCategory == null) {
                            Column(
                                modifier = Modifier.weight(1f).padding(vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .drawBehind {
                                            val strokeWidth = 2.dp.toPx()
                                            val cornerRadius = 7.dp.toPx()

                                            drawRoundRect(
                                                color = Gray1,
                                                size = size,
                                                cornerRadius = CornerRadius(cornerRadius),
                                                style = Stroke(
                                                    width = strokeWidth,
                                                    pathEffect = PathEffect.dashPathEffect(
                                                        floatArrayOf(6f, 6f), // dash – gap
                                                        0f
                                                    )
                                                )
                                            )
                                        }
                                ) {}
                                Text(
                                    text = "-----",
                                    style = CustomTypography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Gray1,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )

                            }
                        } else if (uiState.isModifyFavorite) {
                            ServiceComponent(
                                modifier = Modifier.weight(1f),
                                icon = serviceCategory.icon,
                                serviceName = serviceCategory.serviceName,
                                color = Color(serviceCategory.color),
                                actionIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.minus_bold),
                                        contentDescription = null,
                                        tint = Red1,
                                        modifier = Modifier.size(15.dp)
                                    )
                                },
                                action = {
                                    favoriteServices = favoriteServices.toMutableList().apply {
                                        remove(serviceCategory)
                                    }
                                },
                                onClick = {}
                            )
                        } else {
                            ServiceComponent(
                                modifier = Modifier.weight(1f),
                                icon = serviceCategory.icon,
                                serviceName = serviceCategory.serviceName,
                                color = Color(serviceCategory.color),
                                onClick = {
                                    navigator[serviceCategory.name]?.invoke()
                                }
                            )
                        }

                    }
                }

            }
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Tất cả dịch vụ",
                        style = CustomTypography.titleMedium,
                        color = Gray1,
                        modifier = Modifier.weight(1f)
                    )
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items = allServices) { serviceCategory ->
                        val isExisting = favoriteServices.contains(serviceCategory)
                        if (!uiState.isModifyFavorite) {

                            ServiceComponent(
                                icon = serviceCategory.icon,
                                serviceName = serviceCategory.serviceName,
                                color = Color(serviceCategory.color),
                                onClick = {
                                    navigator[serviceCategory.name]?.invoke()
                                }
                            )
                        } else if (isExisting) {
                            ServiceComponent(
                                icon = serviceCategory.icon,
                                serviceName = serviceCategory.serviceName,
                                color = Color(serviceCategory.color),
                                actionIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ok_status_bold),
                                        contentDescription = null,
                                        tint = Green1,
                                        modifier = Modifier.size(15.dp)
                                    )
                                },
                                onClick = {}
                            )
                        } else {
                            ServiceComponent(
                                icon = serviceCategory.icon,
                                serviceName = serviceCategory.serviceName,
                                color = Color(serviceCategory.color),
                                actionIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.add_bold),
                                        contentDescription = null,
                                        tint = Gray1,
                                        modifier = Modifier.size(15.dp)
                                    )
                                },
                                action = {
                                    favoriteServices = favoriteServices.toMutableList().apply {
                                        add(serviceCategory)
                                    }
                                },
                                onClick = {}

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
fun AllServicePreview() {
    AllServiceScreen(
        onBackClick = {},
        uiState = AllServiceUiState(
            isModifyFavorite = true,
            favoriteServices = listOf(
                ServiceCategory.MONEY_TRANSFER
            )
        ),
        navigator = emptyMap(),
        onChangeToModifyState = {},
        onSaveFavoriteServices = {}
    )
}
