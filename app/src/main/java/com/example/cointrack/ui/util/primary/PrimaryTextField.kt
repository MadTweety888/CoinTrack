package com.example.cointrack.ui.util.primary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.Grey30
import com.example.cointrack.ui.theme.Grey70
import com.example.cointrack.ui.theme.Grey85
import com.example.cointrack.ui.theme.White
import com.example.cointrack.ui.theme.spacing

private const val DEFAULT_TRAILING_ICON_SIZE = 16

@Composable
fun PrimaryTextField(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    textColor: Color = MaterialTheme.colors.onSurface,
    onTextChanged: (String) -> Unit,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    placeholder: String = "",
    placeholderColor: Color = Grey70,
    trailingIcon: ImageVector? = null,
    trailingIconTint: Color = Grey30,
    onTrailingIconClick: () -> Unit = {},
    trailingText: String = "",
    trailingTextColor: Color = White,
    trailingIconSize: Dp = DEFAULT_TRAILING_ICON_SIZE.dp,
    cursorColor: Color = Grey70,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = Done),
    keyboardActions: KeyboardActions = KeyboardActions()
) {

    BasicTextField(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(Grey85)
            .fillMaxWidth()
            .padding(
                top = MaterialTheme.spacing.small,
                bottom = MaterialTheme.spacing.small,
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.small,
            ),
        value = text,
        onValueChange = onTextChanged,
        enabled = enabled,
        singleLine = singleLine,
        textStyle = MaterialTheme.typography.body1.copy(color = textColor),
        cursorBrush = SolidColor(cursorColor),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->

            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {

                if (text.isEmpty()) {

                    Text(
                        text = placeholder,
                        style = textStyle,
                        color = placeholderColor
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {

                        innerTextField()
                    }

                    trailingIcon?.let {

                        PrimaryInputFormTrailingIcon(
                            trailingIcon = it,
                            trailingIconTint = trailingIconTint,
                            trailingIconSize = trailingIconSize,
                            onTrailingIconClick = onTrailingIconClick
                        )

                    } ?: run {

                        if (trailingText.isNotBlank()) {

                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

                            Box(
                                modifier = Modifier
                                    .padding(vertical = 6.dp)
                            ) {

                                Text(
                                    text = trailingText,
                                    style = textStyle,
                                    color = trailingTextColor
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun PrimaryTextFieldPreview() = CoinTrackTheme {

    val text = remember { mutableStateOf("") }

    Column {

        PrimaryTextField(
            text = text.value,
            onTextChanged = { text.value = it },
            placeholder = "Placeholder",
            trailingIcon = Icons.Default.Close,
            onTrailingIconClick = { text.value = "" }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        PrimaryTextField(
            text = text.value,
            onTextChanged = { text.value = it },
            placeholder = "Placeholder",
            trailingText = "RSD"
        )
    }
}