package com.example.cointrack.ui.util.primary

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.Dark
import com.example.cointrack.ui.theme.White
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.ComponentSizes
import com.example.cointrack.util.extensions.conditional

@Composable
fun PrimaryRadioButton(
    modifier: Modifier = Modifier,
    size: Dp = ComponentSizes.DEFAULT_RADIO_BUTTON_SIZE.dp,
    isSelected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {

    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 4.dp else 1.dp,
        animationSpec = tween(durationMillis = 200, easing = LinearEasing),
        label = "Radio Button Border Width Animation"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) White else Dark,
        animationSpec = tween(durationMillis = 200, easing = LinearEasing),
        label = "Radio Button Background Color Animation"
    )

    Box(
        modifier = modifier
            .size(size)
            .background(backgroundColor, CircleShape)
            .border(borderWidth, MaterialTheme.colors.primary, CircleShape)
            .clip(CircleShape)
            .conditional(enabled) {
                clickable { onClick() }
            }
    )
}

@Preview
@Composable
private fun PrimaryRadioButtonPreview() = CoinTrackTheme {

    Row {

        PrimaryRadioButton(
            isSelected = true,
            onClick = {}
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        PrimaryRadioButton(
            isSelected = false,
            onClick = {}
        )
    }
}