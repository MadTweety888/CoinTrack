package com.example.cointrack.ui.util.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cointrack.R
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.Grey30
import com.example.cointrack.ui.theme.Grey70
import com.example.cointrack.ui.theme.Grey85
import com.example.cointrack.ui.theme.White
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.primary.PrimaryInputFormTrailingIcon

private const val DEFAULT_TRAILING_ICON_SIZE = 16

@Composable
fun RowInputField(
    modifier: Modifier = Modifier,
    item: String,
    placeholder: String = "",
    trailingIconSize: Dp = DEFAULT_TRAILING_ICON_SIZE.dp,
    onRowClicked: () -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(Grey85)
            .clickable { onRowClicked() }
            .padding(
                bottom = MaterialTheme.spacing.small,
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.small
            ),
        verticalAlignment = CenterVertically,
        horizontalArrangement = SpaceBetween
    ) {

        if (item.isBlank()) {

            Text(
                modifier = Modifier.padding(top = MaterialTheme.spacing.small),
                text = placeholder,
                style = MaterialTheme.typography.body1,
                color = Grey70
            )

        } else {

            Text(
                modifier = Modifier.padding(top = MaterialTheme.spacing.small),
                text = item,
                style = MaterialTheme.typography.body1,
                color = White
            )
        }

        PrimaryInputFormTrailingIcon(
            modifier = Modifier.padding(top = MaterialTheme.spacing.small),
            trailingIcon = painterResource(id = R.drawable.arrow_down_icon),
            trailingIconTint = Grey30,
            trailingIconSize = trailingIconSize,
            onTrailingIconClick = onRowClicked
        )
    }
}

@Preview
@Composable
private fun RowInputFieldPreview() = CoinTrackTheme {

    Column {

        RowInputField(
            item = "",
            placeholder = "Category",
            onRowClicked = { /* NO ACTION */ }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        RowInputField(
            item = "Social Life",
            placeholder = "Category",
            onRowClicked = { /* NO ACTION */ }
        )
    }

}