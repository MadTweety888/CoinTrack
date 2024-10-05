package com.example.cointrack.ui.util.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.cointrack.domain.enums.TransactionType
import com.example.cointrack.domain.enums.TransactionType.EXPENSE
import com.example.cointrack.domain.enums.toDisplayString
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.Dark
import com.example.cointrack.ui.theme.Grey70
import com.example.cointrack.ui.theme.spacing

@Composable
fun RowScope.TransactionTypeBubble(
    transactionType: TransactionType,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val bubbleColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colors.primary else Grey70,
        animationSpec = tween(durationMillis = 200, easing = LinearEasing),
        label = "Animated transaction type color"
    )

    Box(
        modifier = Modifier
            .weight(1f)
            .clip(MaterialTheme.shapes.large)
            .background(Dark)
            .border(
                width = 1.dp,
                color = bubbleColor,
                shape = MaterialTheme.shapes.large
            )
            .clickable { onClick() }
            .padding(
                vertical = MaterialTheme.spacing.small
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = transactionType.toDisplayString(),
            style = MaterialTheme.typography.button,
            color = bubbleColor
        )
    }
}

@Preview
@Composable
private fun TransactionTypeBubblePreview(
    @PreviewParameter(IsSelectedProvider::class) isSelected: Boolean
) = CoinTrackTheme {

    Row {

        TransactionTypeBubble(
            transactionType = EXPENSE,
            isSelected = isSelected,
            onClick = { /* NO ACTION */ }
        )
    }
}

private class IsSelectedProvider: PreviewParameterProvider<Boolean> {

    override val values: Sequence<Boolean> = sequenceOf(true, false)
}