package com.example.cointrack.ui.util.primary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cointrack.R
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.Grey20
import com.example.cointrack.ui.theme.spacing

private const val DEFAULT_HEADER_ICON_SIZE = 24

@Composable
fun PrimaryHeader(
    modifier: Modifier = Modifier,
    headerLeadingIcon: Painter? = null,
    isHeaderLeadingIconClickable: Boolean = false,
    onHeaderLeadingIconClick: () -> Unit = {},
    headerIconsSize: Dp = DEFAULT_HEADER_ICON_SIZE.dp,
    headerIconsTint: Color = Grey20,
    headerTitle: String,
    additionalActionIcons: List<Pair<Painter, () -> Unit>> = emptyList()
) {

    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically
    ) {

        headerLeadingIcon?.let {

            if (isHeaderLeadingIconClickable) {

                IconButton(onClick = onHeaderLeadingIconClick) {

                    Icon(
                        modifier = Modifier.size(headerIconsSize),
                        painter = it,
                        tint = headerIconsTint,
                        contentDescription = null
                    )
                }

            } else {

                Icon(
                    modifier = Modifier.size(headerIconsSize),
                    painter = it,
                    tint = headerIconsTint,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            }
        }


        Text(
            text = headerTitle,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onBackground
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = CenterVertically
        ) {

            additionalActionIcons.forEach { iconAndAction ->

                IconButton(onClick = iconAndAction.second) {

                    Icon(
                        modifier = Modifier.size(headerIconsSize),
                        painter = iconAndAction.first,
                        tint = headerIconsTint,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun PrimaryHeader(
    modifier: Modifier = Modifier,
    headerLeadingIcon: ImageVector,
    isHeaderLeadingIconClickable: Boolean = false,
    onHeaderLeadingIconClick: () -> Unit = {},
    headerIconsSize: Dp = DEFAULT_HEADER_ICON_SIZE.dp,
    headerIconsTint: Color = Grey20,
    headerTitle: String,
    additionalActionIcons: List<Pair<Painter, () -> Unit>> = emptyList()
) {

    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically
    ) {

        if (isHeaderLeadingIconClickable) {

            IconButton(onClick = onHeaderLeadingIconClick) {

                Icon(
                    modifier = Modifier.size(headerIconsSize),
                    imageVector = headerLeadingIcon,
                    tint = headerIconsTint,
                    contentDescription = null
                )
            }

        } else {

            Icon(
                modifier = Modifier.size(headerIconsSize),
                imageVector = headerLeadingIcon,
                tint = headerIconsTint,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
        }

        Text(
            text = headerTitle,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onBackground
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = CenterVertically
        ) {

            additionalActionIcons.forEach { iconAndAction ->

                IconButton(onClick = iconAndAction.second) {

                    Icon(
                        modifier = Modifier.size(headerIconsSize),
                        painter = iconAndAction.first,
                        tint = headerIconsTint,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PrimaryHeaderPreview() = CoinTrackTheme {

    Column {

        PrimaryHeader(
            headerLeadingIcon = painterResource(id = R.drawable.navigate_back_icon),
            isHeaderLeadingIconClickable = true,
            headerTitle = "Page Name"
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        PrimaryHeader(
            headerLeadingIcon = Icons.Default.ReceiptLong,
            headerTitle = "Page Name",
            additionalActionIcons = listOf(
                Pair(painterResource(id = R.drawable.sort_filter_icon)) {}
            )
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        PrimaryHeader(
            headerLeadingIcon = painterResource(id = R.drawable.default_profile_icon),
            headerTitle = "Page Name",
            additionalActionIcons = listOf(
                Pair(painterResource(id = R.drawable.add_item_icon)) {},
                Pair(painterResource(id = R.drawable.sort_filter_icon)) {},
            )
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        PrimaryHeader(
            headerTitle = "Page Name",
            additionalActionIcons = listOf(
                Pair(painterResource(id = R.drawable.add_item_icon)) {},
                Pair(painterResource(id = R.drawable.sort_filter_icon)) {},
            )
        )
    }
}