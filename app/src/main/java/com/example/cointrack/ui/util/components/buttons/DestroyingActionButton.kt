package com.example.cointrack.ui.util.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.Dark
import com.example.cointrack.ui.theme.RedError
import com.example.cointrack.ui.theme.White
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.ComponentSizes.DEFAULT_BUTTON_HEIGHT

@Composable
fun DestroyingActionButton(
    modifier: Modifier = Modifier,
    text: String,
    height: Dp = DEFAULT_BUTTON_HEIGHT.dp,
    backgroundColor: Color = Dark,
    onClick: () -> Unit,
) {

    Button(
        modifier = modifier
            .height(height),
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(1.dp, RedError),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = White
        ),
        onClick = onClick
    ) {

        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = RedError
        )
    }
}

@Preview
@Composable
private fun DestroyingActionButtonPreview() = CoinTrackTheme {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(MaterialTheme.spacing.medium),
        contentAlignment = Alignment.Center
    ) {

        DestroyingActionButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Destroying Action",
            onClick = {}
        )
    }
}