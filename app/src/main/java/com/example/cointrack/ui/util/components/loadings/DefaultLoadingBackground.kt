package com.example.cointrack.ui.util.components.loadings

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.example.cointrack.ui.util.components.BoxWithDiagonalBackgroundPattern

@Composable
fun DefaultLoadingBackground(
    content: @Composable BoxScope.() -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .pointerInput(Unit) { detectTapGestures(onTap = { /* NO ACTION */ }) }
    ) {

        BoxWithDiagonalBackgroundPattern {

            content()
        }
    }
}