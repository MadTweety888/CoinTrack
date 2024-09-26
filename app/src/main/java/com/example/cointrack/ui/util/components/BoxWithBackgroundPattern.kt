package com.example.cointrack.ui.util.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import com.example.cointrack.ui.theme.BackgroundPatternColor
import com.example.cointrack.ui.theme.CoinTrackTheme

@Composable
fun BoxWithBackgroundPattern(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        BackgroundPatternColor,
                        MaterialTheme.colors.background,
                    ),
                )
            )
    ) {
        content()
    }
}

@Preview
@Composable
private fun BackgroundPatternPreview() = CoinTrackTheme {

    BoxWithBackgroundPattern {

        Text(
            text = "Background pattern",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h1,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}