package com.example.cointrack.ui.util.primary

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.util.components.BoxWithBackgroundPattern

@Composable
fun PrimaryLoadingScreen() {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .pointerInput(Unit) { detectTapGestures(onTap = { /* NO ACTION */ }) }
    ) {

        BoxWithBackgroundPattern {

            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
private fun PrimaryLoadingScreenPreview() = CoinTrackTheme {

    PrimaryLoadingScreen()
}