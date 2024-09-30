package com.example.cointrack.ui.util.components.loadings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.util.calculateNumberOfLoadingRectangles

private const val LOADING_ITEM_HEIGHT = 67

@Composable
fun TransactionsLoadingScreen(
    availableHeight: Dp
) {

    val numberOfLoadingRectangles = calculateNumberOfLoadingRectangles(
        availableSpace = availableHeight,
        rectangleHeight = LOADING_ITEM_HEIGHT.dp,
        rectanglePadding = MaterialTheme.spacing.small
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        for (i in 1..numberOfLoadingRectangles) {

            ShimmeringBox(
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.small),
                height = LOADING_ITEM_HEIGHT.dp
            )
        }
    }
}

@Preview
@Composable
private fun TransactionsLoadingScreenPreview() = CoinTrackTheme {

    val screenHeight = LocalConfiguration.current.screenHeightDp

    TransactionsLoadingScreen(screenHeight.dp)
}