package com.example.cointrack.ui.util.components.dialogs

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.Dark
import com.example.cointrack.ui.theme.customshapes.RoundedTopCornersWithCurvedEdgeRectangle
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.util.extensions.conditional
import com.example.cointrack.util.extensions.toDp

@Composable
fun BottomPopUpDialog(
    isDisplayed: Boolean,
    isFixedHeight: Boolean = false,
    fixedDialogHeight: Dp = 0.dp,
    onCancel: () -> Unit = { /* intentionally left blank */ },
    content: @Composable (modifier: Modifier) -> Unit
) {

    val contentHeight = remember { mutableStateOf(0.dp) }

    val darkenCoverAlpha by animateFloatAsState(
        targetValue = if (isDisplayed) 0.6f else 0f,
        animationSpec = tween(durationMillis = 400, easing = LinearEasing),
        label = "Darken cover alpha animation"
    )

    val animatedHeight by animateDpAsState(
        targetValue = if (isDisplayed) {

            if (isFixedHeight) fixedDialogHeight else contentHeight.value

        } else {

            0.dp
        },
        animationSpec = tween(durationMillis = 400, easing = LinearEasing),
        label = "Dialog height animation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = darkenCoverAlpha))
            .conditional(isDisplayed) {

                pointerInput(Unit) { detectTapGestures { onCancel() } }
            },
        contentAlignment = Alignment.BottomCenter
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(animatedHeight)
                .clip(RoundedTopCornersWithCurvedEdgeRectangle())
                .background(Dark)
                .padding(horizontal = MaterialTheme.spacing.medium)
                .pointerInput(Unit) { detectTapGestures {} }
        ) {

            content(
                Modifier
                    .conditional(!isFixedHeight) {
                        wrapContentHeight(unbounded = true)
                    }
                    .onSizeChanged { contentHeight.value = it.height.toDp }
            )
        }
    }
}

@Preview
@Composable
private fun BottomPopUpDialogPreview() = CoinTrackTheme {

    val isDialogDisplayed = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {

        Button(
            onClick = { isDialogDisplayed.value = true }
        ) {

            Text(text = "SHOW POPUP")
        }

        BottomPopUpDialog(
            isDisplayed = isDialogDisplayed.value,
            onCancel = { isDialogDisplayed.value = false },
            content = { modifier ->

                Box(
                    modifier = modifier
                        .height(400.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Content",
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        )
    }
}