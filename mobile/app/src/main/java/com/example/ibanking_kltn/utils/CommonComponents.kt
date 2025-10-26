package com.example.ibanking_kltn.utils

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue1
import com.example.ibanking_kltn.ui.theme.CustomTypography
import com.example.ibanking_kltn.ui.theme.Gray1
import com.example.ibanking_kltn.ui.theme.Gray2
import com.example.ibanking_kltn.ui.theme.Gray4
import com.example.ibanking_kltn.ui.theme.White1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    isError: Boolean = false,
    readOnly: Boolean = false,
    textStyle: TextStyle = CustomTypography.titleMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done
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
    BasicTextField(
        modifier = modifier.then(
            Modifier.clickable(onClick = {})
        ),
        value = value,
        onValueChange = onValueChange,
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
                value = value,
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
    selectedOption: String? = null,
    onOptionSelected: (String) -> Unit,
    placeholder: @Composable (() -> Unit)
) {
    var expanded by remember { mutableStateOf(false) }
    var boxWidth by remember { mutableIntStateOf(0) }
    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
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
                    width = 1.dp, color = Gray2,
                    shape = RoundedCornerShape(30)
                )
                .background(
                    color = White1,
                    shape = RoundedCornerShape(30)
                )
                .clickable {
                    expanded = true
                }
                .padding(15.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                placeholder()
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
            modifier = Modifier
                .width(with(LocalDensity.current) { boxWidth.toDp() })

        ) {
            options.forEach { option ->
                DropdownMenuItem(

                    text = { Text(option) },
                    onClick = {
                        expanded = false
                        selectedOption?.let {
                            onOptionSelected(selectedOption)
                        }
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
        enabled = enable,
    ) {
        if (isLoading)
            CircularProgressIndicator(color = White1, modifier = Modifier.size(20.dp))
        else
            Text(
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
            intervals = floatArrayOf(dashWidth.toPx(), gapWidth.toPx()),
            phase = 0f
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