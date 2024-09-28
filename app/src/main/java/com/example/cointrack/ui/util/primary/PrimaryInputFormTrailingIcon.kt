package com.example.cointrack.ui.util.primary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.Grey30
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.util.extentions.conditional

private const val DEFAULT_TRAILING_ICON_SIZE = 16

@Composable
fun PrimaryInputFormTrailingIcon(
    modifier: Modifier = Modifier,
    trailingIcon: ImageVector,
    trailingIconTint: Color = Grey30,
    onTrailingIconClick: () -> Unit = {},
    trailingIconSize: Dp = DEFAULT_TRAILING_ICON_SIZE.dp,
    isEnabled: Boolean = true
) {

    Box(
        modifier = modifier
            .size(trailingIconSize + MaterialTheme.spacing.medium)
            .clip(CircleShape)
            .conditional(isEnabled) {

                clickable { onTrailingIconClick() }
            },
        contentAlignment = Alignment.Center
    ) {

        Icon(
            modifier = Modifier.size(trailingIconSize),
            imageVector = trailingIcon,
            tint = trailingIconTint,
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun InputFormTrailingIconPreview() = CoinTrackTheme {

    PrimaryInputFormTrailingIcon(trailingIcon = Icons.Default.Close)
}