package com.example.cointrack.ui.util.components.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.primary.PrimaryButton

@Composable
fun BoxScope.StickyCTAButton(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onClick: () -> Unit
) {

    AnimatedVisibility(
        modifier = modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = MaterialTheme.spacing.large),
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {

        PrimaryButton(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.small)
                .fillMaxWidth(),
            text = "Save",
            onClick = onClick
        )
    }
}