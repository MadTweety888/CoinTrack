package com.example.cointrack.ui.util.components.loadings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.util.extensions.shimmerLoadingAnimation

private const val DEFAULT_BOX_HEIGHT = 40

@Composable
fun ShimmeringBox(
    modifier: Modifier = Modifier,
    height: Dp = DEFAULT_BOX_HEIGHT.dp,
    shape: Shape = MaterialTheme.shapes.small,
    background: Color = MaterialTheme.colors.surface
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(background)
            .shimmerLoadingAnimation()
    )
}

@Preview
@Composable
private fun ShimmeringBoxPreview() = CoinTrackTheme {

    ShimmeringBox()
}