package com.example.ibanking_kltn.utils

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.data.dtos.BillPayload
import com.example.ibanking_kltn.data.dtos.BillStatus
import com.example.ibanking_kltn.data.dtos.QRPayload
import com.example.ibanking_kltn.data.dtos.TabNavigation
import com.example.ibanking_kltn.data.dtos.TransferPayload
import com.example.ibanking_kltn.ui.theme.BackgroundColor
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.Blue3
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.ErrorGradient
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.Gray3
import com.example.ibanking_kltn.ui.theme.Gray4
import com.example.ibanking_kltn.ui.theme.InfoGradient
import com.example.ibanking_kltn.ui.theme.LabelColor
import com.example.ibanking_kltn.ui.theme.SuccessGradient
import com.example.ibanking_kltn.ui.theme.TextColor
import com.example.ibanking_kltn.ui.theme.WarningGradient
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.theme.White3
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    isError: Boolean = false,
    readOnly: Boolean = false,
    textStyle: TextStyle = CustomTypography.titleSmall,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions = KeyboardActions(

    ),
    singleLine: Boolean = true,
    maxLines: Int = 1,
    textFieldColors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Black1,
        unfocusedTextColor = Gray2,
        unfocusedPlaceholderColor = Gray2,
        unfocusedBorderColor = Gray2,
        focusedBorderColor = Blue1,
    ),
    isPasswordField: Boolean = false,
    isPasswordShow: Boolean = false,
    shape: Shape = RoundedCornerShape(30),
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val textFieldValue = TextFieldValue(
        text = value, selection = TextRange(value.length)
    )


    BasicTextField(
        modifier = modifier,
        value = textFieldValue,
        onValueChange = {
            onValueChange(it.text)
        },
        enabled = enable,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = if (isPasswordField && !isPasswordShow) PasswordVisualTransformation() else VisualTransformation.None,
        decorationBox = @Composable { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = textFieldValue.text,
                innerTextField = innerTextField,
                visualTransformation = visualTransformation,
                placeholder = placeholder,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                enabled = enable,
                interactionSource = interactionSource,
                colors = textFieldColors,
                singleLine = singleLine,
                container = {
                    OutlinedTextFieldDefaults.Container(
                        enabled = enable,
                        isError = isError,
                        interactionSource = interactionSource,
                        colors = textFieldColors,
                        shape = shape
                    )
                },
            )
        },

        )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownField(
    modifier: Modifier = Modifier,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    placeholder: String
) {
    var expanded by remember { mutableStateOf(false) }
    var boxWidth by remember { mutableIntStateOf(0) }
    Box(
        modifier = modifier.onGloballyPositioned { coordinates ->
            boxWidth = coordinates.size.width
        }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 30.dp,
                    shape = RoundedCornerShape(30),
                    ambientColor = Black1.copy(alpha = 0.25f),
                    spotColor = Black1.copy(alpha = 0.25f)
                )
                .border(
                    width = 1.dp, color = Gray2, shape = RoundedCornerShape(30)
                )
                .background(
                    color = White1, shape = RoundedCornerShape(30)
                )
                .clickable {
                    expanded = true
                }
                .padding(15.dp)) {
            Column(
                verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)
            ) {
                if (selectedOption == "") Text(
                    text = placeholder, style = CustomTypography.titleMedium, color = Gray2
                )
                else Text(
                    text = selectedOption, style = CustomTypography.titleMedium, color = Black1
                )

            }
            Icon(
                painter = painterResource(R.drawable.dropdown),
                tint = Gray1,
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            containerColor = White1,
            modifier = Modifier.width(with(LocalDensity.current) { boxWidth.toDp() })

        ) {
            options.forEach { option ->
                DropdownMenuItem(

                    text = { Text(option) },
                    onClick = {
                        expanded = false
                        onOptionSelected(
                            option
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun CustomTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    enable: Boolean = true,
    buttonColor: ButtonColors = ButtonDefaults.textButtonColors(
        containerColor = Blue1,
        disabledContainerColor = Gray4,
        contentColor = White1,
        disabledContentColor = White1
    ),
    isLoading: Boolean = false,
    style: TextStyle = CustomTypography.titleMedium,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(30),
        colors = buttonColor,
        enabled = enable && !isLoading,
    ) {
        if (isLoading) CircularProgressIndicator(color = White1, modifier = Modifier.size(20.dp))
        else Text(
            text = text,
            style = style,
            color = if (enable) buttonColor.contentColor else buttonColor.disabledContentColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun DashedDivider(
    modifier: Modifier = Modifier,
    dashWidth: Dp = 4.dp,
    dashHeight: Dp = 2.dp,
    gapWidth: Dp = 2.dp,
    color: Color = Gray1,
) {
    Canvas(modifier) {

        val pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashWidth.toPx(), gapWidth.toPx()), phase = 0f
        )

        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect,
            strokeWidth = dashHeight.toPx()
        )
    }
}

@Composable
fun OtpDialogCustom(
    otpLength: Int,
    otpValue: String,
    onOtpChange: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(
                onClick = onDismiss,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = White1, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {}) {
                Surface(
                    shadowElevation = 8.dp, color = Color.Transparent
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Blue3,
                                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                            )
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Nhập mã OTP",
                            style = CustomTypography.titleMedium,
                            color = White1
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(20.dp))
                            .background(color = White3)
                            .padding(vertical = 10.dp)
                    ) {
                        BasicTextField(
                            value = otpValue, onValueChange = {
                                if (it.length <= otpLength && it.all { c -> c.isDigit() }) {
                                    onOtpChange(it)
                                }
                            }, keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.NumberPassword
                            ), decorationBox = {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(shape = RoundedCornerShape(20.dp))
                                        .background(color = BackgroundColor)
                                ) {
                                    repeat(otpLength) { index ->
                                        val isFilled = index < otpValue.length
                                        Box(
                                            modifier = Modifier
                                                .size(30.dp)
                                                .clip(CircleShape)
                                                .background(if (isFilled) TextColor else LabelColor)
                                                .border(
                                                    width = 2.dp,
                                                    color = if (isFilled) TextColor else LabelColor,
                                                    shape = CircleShape
                                                )
                                        )
                                    }
                                }
                            })
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        Text(
                            text = "", style = CustomTypography.labelLarge, color = BackgroundColor
                        )
                    }
                }
            }

        }

    }
}


@Composable
fun LoadingScaffold(
    isLoading: Boolean,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        content()
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Gray3.copy(alpha = 0.5f),
                    )
                    .pointerInput(Unit) {}) {
                CircularProgressIndicator(
                    color = Blue3, modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}


@Composable
fun NavigationBar(
    bottomBarHeight: Dp,
    currentTab: TabNavigation,
    onNavigateToSettingScreen: () -> Unit,
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToWalletScreen: () -> Unit,
    onNavigateToAnalyticsScreen: () -> Unit,
    onNavigateToQRScanner: () -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(bottomBarHeight)
            .background(color = Color.Transparent)
    ) {
        //nav
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .height(bottomBarHeight)
                .fillMaxWidth()
//                        .background(White3)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    75.dp, alignment = Alignment.CenterHorizontally
                ),
                modifier = Modifier

                    .height(bottomBarHeight * 5 / 8)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 30.dp,
                        shape = RoundedCornerShape(topStart = 0.2f, topEnd = 0.2f),
                        ambientColor = Black1.copy(alpha = 0.9f),
                        spotColor = Black1.copy(alpha = 0.9f),
                    )
                    .background(
                        color = White1, shape = RoundedCornerShape(topStart = 0.2f, topEnd = 0.2f)
                    )

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        30.dp, alignment = Alignment.CenterHorizontally
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp)
                ) {
                    IconButton(onClick = {
                        onNavigateToHomeScreen()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.category),
                            contentDescription = null,
                            tint = if (currentTab == TabNavigation.HOME) Blue3 else Gray1,
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    IconButton(onClick = {
                        onNavigateToWalletScreen()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.wallet),
                            contentDescription = null,
                            tint = if (currentTab == TabNavigation.WALLET) Blue3 else Gray1,
                            modifier = Modifier.size(35.dp)
                        )
                    }

                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        30.dp, alignment = Alignment.CenterHorizontally
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp)
                ) {
                    IconButton(onClick = {
                        onNavigateToAnalyticsScreen()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.analytic),
                            contentDescription = null,
                            tint = if (currentTab == TabNavigation.ANALYTICS) Blue3 else Gray1,
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    IconButton(onClick = {
                        onNavigateToSettingScreen()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.profile),
                            contentDescription = null,
                            tint = if (currentTab == TabNavigation.PROFILE) Blue3 else Gray1,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(bottomBarHeight)
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    onNavigateToQRScanner()
                }, modifier = Modifier
                    .shadow(
                        elevation = 20.dp, shape = RoundedCornerShape(40)
                    )
                    .border(
                        width = 5.dp, color = White1, shape = RoundedCornerShape(40)
                    )
                    .background(color = Blue3, shape = RoundedCornerShape(40))
                    .padding(5.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.qr),
                    contentDescription = null,
                    tint = White1,
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }

}


@Composable
private fun animateFadeAndScaleSnackBar(): Pair<Float, Float> {
    val alpha by animateFloatAsState(
        targetValue = 1f, animationSpec = tween(
            durationMillis = SnackBarConstants.ALPHA_DURATION, easing = FastOutSlowInEasing
        ), label = "alpha"
    )
    val scale by animateFloatAsState(
        targetValue = 1f, animationSpec = tween(
            durationMillis = SnackBarConstants.SCALE_DURATION, easing = FastOutSlowInEasing
        ), label = "scale"
    )
    return alpha to scale
}

private object SnackBarConstants {
    val PADDING = 8.dp
    val CONTENT_PADDING = 12.dp
    val CORNER_RADIUS = 12.dp
    val ICON_SIZE = 24.dp
    val ICON_PADDING = 8.dp
    val SPACING = 8.dp
    val BUTTON_HEIGHT = 36.dp
    val MESSAGE_FONT_SIZE = 14.sp
    val ACTION_FONT_SIZE = 14.sp
    const val ALPHA_DURATION = 250
    const val SCALE_DURATION = 200
}

enum class SnackBarType {
    SUCCESS, ERROR, WARNING, INFO
}

@Composable
fun GradientSnackBar(
    type: SnackBarType,
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = "Đóng",
    onAction: () -> Unit = {},
) {
    val (alphaAnim, scaleAnim) = animateFadeAndScaleSnackBar()
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//
//        Box(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(bottom = 16.dp)
//        ) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight()
            .alpha(alphaAnim)
            .scale(scaleAnim)
            .padding(SnackBarConstants.PADDING),
        shape = RoundedCornerShape(SnackBarConstants.CORNER_RADIUS),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = when (type) {
                            SnackBarType.SUCCESS -> SuccessGradient
                            SnackBarType.ERROR -> ErrorGradient
                            SnackBarType.WARNING -> WarningGradient
                            SnackBarType.INFO -> InfoGradient
                        }
                    )
                )
                .padding(SnackBarConstants.CONTENT_PADDING)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)
            ) {
                when (type) {
                    SnackBarType.SUCCESS -> Icon(
                        painter = painterResource(R.drawable.success),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(SnackBarConstants.ICON_SIZE)
                            .padding(end = SnackBarConstants.ICON_PADDING)
                    )

                    else -> Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(SnackBarConstants.ICON_SIZE)
                            .padding(end = SnackBarConstants.ICON_PADDING)
                    )
                }
                Text(
                    text = message,
                    color = Color.White,
                    fontSize = SnackBarConstants.MESSAGE_FONT_SIZE,
                    textAlign = TextAlign.Start,
                    lineHeight = SnackBarConstants.MESSAGE_FONT_SIZE * 1.2f,
                    modifier = Modifier.padding(end = SnackBarConstants.SPACING)
                )
            }
            actionLabel?.let {
                TextButton(
                    onClick = onAction, modifier = Modifier.height(SnackBarConstants.BUTTON_HEIGHT)
                ) {
                    Text(
                        text = it,
                        color = Color.White,
                        fontSize = SnackBarConstants.ACTION_FONT_SIZE,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
//        }
//    }
}


@Composable
fun QrCodeImage(
    modifier: Modifier = Modifier,
    content: QRPayload,
    size: Dp,
    onSavedSuccess:()->Unit = {},
) {
    val payload = jsonInstance().encodeToString(content)
    val bitmap = generateQrBitmap(payload, size = size.value.toInt())
    val context = LocalContext.current
    val id = UUID.randomUUID()
    Box(
        modifier = modifier
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = remember(bitmap) { BitmapPainter(bitmap.asImageBitmap()) },
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.size(size),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            saveBitmapToInternalStorage(
                                context = context,
                                bitmap = bitmap,
                                fileName = id.toString()
                            )
                            onSavedSuccess()
                        }) {
                    Icon(
                        painter = painterResource(R.drawable.image),
                        contentDescription = "Bill Image",
                        modifier = Modifier.size(25.dp)
                    )
                    Text(
                        text = "Lưu ảnh QR",
                        style = CustomTypography.titleMedium,
                        color = Black1,
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            shareText(
                                context = context,
                                text = when (content) {
                                    is BillPayload -> content.billCode
                                    is TransferPayload -> content.toWalletNumber
                                }
                            )
                        }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.export),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp)
                    )
                    Text(
                        text = "Chia sẻ QR",
                        style = CustomTypography.titleMedium,
                        color = Black1,
                    )
                }
            }
        }
    }
}


@Composable
fun BillFilterDialog(
    selectedStatus: String = "",
    selectedSort: String = "",
    onSelectStatus: (String) -> Unit = {},
    onSelectSort: (String) -> Unit = {},
    onResetStatusFilter: () -> Unit = {},
    onResetSortFilter: () -> Unit = {},
    onResetAll: () -> Unit = {},
    onApply: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val billStatusOptions = BillStatus.entries.map { status ->
        when (status) {
            BillStatus.ACTIVE -> {
                return@map "Chưa thanh toán"
            }

            BillStatus.PAID -> {
                return@map "Đã thanh toán"
            }

            BillStatus.OVERDUE -> {
                return@map "Quá hạn"
            }

            BillStatus.CANCELED -> {
                return@map "Đã hủy"
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = White1, shape = RoundedCornerShape(12.dp)
            )
            .padding(5.dp)
    ) {
        Box(
            contentAlignment = Alignment.TopEnd,
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(15.dp)
            ) {


                Text(
                    text = "Lọc theo:", style = CustomTypography.titleMedium, color = Black1
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Trạng thái hóa đơn",
                        style = CustomTypography.bodyMedium,
                        color = Gray1
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Bỏ lọc",
                        style = CustomTypography.bodyMedium,
                        color = Blue1,
                        modifier = Modifier.clickable {
                            onResetStatusFilter()
                        })
                }

                CustomDropdownField(
                    options = billStatusOptions,
                    selectedOption = selectedStatus,
                    onOptionSelected = {
                        onSelectStatus(it)
                    },
                    placeholder = "Trạng thái hóa đơn"
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Sắp xếp theo:", style = CustomTypography.titleMedium, color = Black1
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ngày tạo", style = CustomTypography.bodyMedium, color = Gray1
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Bỏ lọc",
                        style = CustomTypography.bodyMedium,
                        color = Blue1,
                        modifier = Modifier.clickable {
                            onResetSortFilter()
                        })
                }

                CustomDropdownField(
                    options = listOf("Mới nhất", "Cũ nhất"),
                    selectedOption = selectedSort,
                    onOptionSelected = {
                        onSelectSort(it)
                    },
                    placeholder = "Sắp xếp theo"
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            onResetAll()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Gray2
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Đặt lại", style = CustomTypography.bodyMedium, color = Black1
                        )
                    }

                    Button(
                        onClick = {
                            onApply()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue1
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Áp dụng", style = CustomTypography.bodyMedium, color = White1
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 0.dp, shape = RoundedCornerShape(10.dp)
                    )
                    .clickable {
                        onDismiss()
                    }) {

                Icon(
                    painter = painterResource(R.drawable.close),
                    contentDescription = null,
                    tint = Gray1,
                    modifier = Modifier.size(35.dp)
                )
            }
        }


    }
}


@Composable
@Preview()
fun Preview() {
    QrCodeImage(
        content = BillPayload(
            billCode = "1234567890",
        ),
        size = 200.dp,
    )
}