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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.Grey20
import com.example.cointrack.ui.theme.Grey30
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.primary.PrimaryButton
import com.example.cointrack.ui.util.primary.PrimaryTextField

@Composable
fun CreateCategoryDialog(
    isDisplayed: Boolean,
    existingCategories: List<String>,
    onDialogDismissed: () -> Unit,
    onDialogConfirmed: (String) -> Unit
) {

    val focusManager = LocalFocusManager.current

    val newCategory = remember { mutableStateOf("") }

    val isSaveButtonEnabled by remember {
        derivedStateOf {
            newCategory.value.isNotBlank() && !existingCategories.contains(newCategory.value)
        }
    }

    BottomPopUpDialog(
        isDisplayed = isDisplayed,
        onCancel = onDialogDismissed
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
                    text = "Create Category",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onSurface
                )

                Icon(
                    modifier = Modifier.clickable { onDialogDismissed() },
                    imageVector = Icons.Default.Close,
                    tint = Grey20,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Text(
                text = "Enter the name of the category you wish to create",
                style = MaterialTheme.typography.body1,
                color = Grey30
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            PrimaryTextField(
                text = newCategory.value,
                onTextChanged = { newCategory.value = it },
                placeholder = "Category name",
                trailingIcon = Icons.Default.Close,
                onTrailingIconClick = { newCategory.value = "" },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Save",
                enabled = isSaveButtonEnabled,
                onClick = { onDialogConfirmed(newCategory.value) }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}

/** Preview needs to be run on device because onSizeChanged is used in the popup */
@Preview
@Composable
private fun CreateCategoryDialogPreview() = CoinTrackTheme {

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

        CreateCategoryDialog(
            isDisplayed = isDialogDisplayed.value,
            existingCategories = emptyList(),
            onDialogConfirmed = { isDialogDisplayed.value = false },
            onDialogDismissed = { isDialogDisplayed.value = false }
        )
    }
}