package com.example.cointrack.ui.util.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.Grey20
import com.example.cointrack.ui.theme.Grey30
import com.example.cointrack.ui.theme.RedError
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.components.buttons.DestroyingActionButton

@Composable
fun DestroyingActionDialog(
    isDisplayed: Boolean,
    title: String = "Delete account?",
    firstText: String = "Are you sure you want to delete your account?",
    secondText: String = "This action will delete all your data permanently, and cannot be undone!",
    buttonText: String = "Delete",
    onAction: () -> Unit,
    onCancel: () -> Unit
) {

    BottomPopUpDialog(
        isDisplayed = isDisplayed,
        onCancel = onCancel
    ) { modifier ->

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium)
        ) {

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            Row(
                modifier = Modifier
                    .align(Alignment.Start)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle1,
                    color = RedError
                )

                Icon(
                    modifier = Modifier.clickable { onCancel() },
                    imageVector = Icons.Default.Close,
                    tint = Grey20,
                    contentDescription = "Cancel Dialog Button"
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Text(
                text = firstText,
                style = MaterialTheme.typography.body1,
                color = Grey30
            )

            if (secondText.isNotBlank()) {

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                Text(
                    text = secondText,
                    style = MaterialTheme.typography.body1,
                    color = Grey30
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            DestroyingActionButton(
                modifier = Modifier.fillMaxWidth(),
                text = buttonText,
                onClick = onAction
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}

/** Preview needs to be run on device because onSizeChanged is used in the popup */
@Preview
@Composable
private fun DestroyingActionDialogPreview() = CoinTrackTheme {

        val isDialogDisplayed = remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {

            Button(
                onClick = { isDialogDisplayed.value = true }
            ) {

                Text(text = "SHOW POPUP")
            }

            DestroyingActionDialog(
                isDisplayed = isDialogDisplayed.value,
                onAction = { isDialogDisplayed.value = false },
                onCancel = { isDialogDisplayed.value = false }
            )
        }
}