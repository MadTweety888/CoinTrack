package com.example.cointrack.ui.util.primary

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.RedError
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.components.BoxWithBackgroundPattern

@Composable
fun PrimaryErrorScreen(
    errorText: String,
    hasRetryButton: Boolean = false,
    onRetryClicked: () -> Unit = {}
) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .pointerInput(Unit) { detectTapGestures(onTap = { /* NO ACTION */ }) }
    ) {

        BoxWithBackgroundPattern {

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = errorText,
                    style = MaterialTheme.typography.h5,
                    color = RedError
                )

                if (hasRetryButton) {

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                    PrimaryButton(
                        modifier = Modifier.fillMaxWidth(0.6f),
                        text = "Retry",
                        onClick = onRetryClicked
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PrimaryErrorScreenPreview(
    @PreviewParameter(HasRetryButtonProvider::class) hasRetryButton: Boolean
) = CoinTrackTheme {

    PrimaryErrorScreen(
        errorText = "An unexpected error occurred!",
        hasRetryButton = hasRetryButton
    )
}

private class HasRetryButtonProvider : PreviewParameterProvider<Boolean> {

    override val values: Sequence<Boolean> = sequenceOf(true, false)
}