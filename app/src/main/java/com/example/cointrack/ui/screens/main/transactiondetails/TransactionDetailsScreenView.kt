package com.example.cointrack.ui.screens.main.transactiondetails

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.KeyboardType.Companion.Number
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cointrack.R
import com.example.cointrack.domain.enums.TransactionType
import com.example.cointrack.domain.enums.TransactionType.EXPENSE
import com.example.cointrack.domain.enums.TransactionType.INCOME
import com.example.cointrack.domain.enums.TransactionsSource
import com.example.cointrack.domain.enums.TransactionsSource.UNKNOWN
import com.example.cointrack.domain.enums.toDisplayString
import com.example.cointrack.ui.screens.main.transactiondetails.TransactionDetailsScreenViewModel.Events.NavigateBack
import com.example.cointrack.ui.theme.Dark
import com.example.cointrack.ui.theme.Grey70
import com.example.cointrack.ui.theme.RedError
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.components.BoxWithDiagonalBackgroundPattern
import com.example.cointrack.ui.util.components.RowInputField
import com.example.cointrack.ui.util.components.buttons.StickyCTAButton
import com.example.cointrack.ui.util.components.dialogs.CreateCategoryDialog
import com.example.cointrack.ui.util.components.dialogs.SelectCategoryDialog
import com.example.cointrack.ui.util.components.loadings.TransactionDetailsLoadingScreen
import com.example.cointrack.ui.util.primary.PrimaryErrorScreen
import com.example.cointrack.ui.util.primary.PrimaryHeader
import com.example.cointrack.ui.util.primary.PrimaryRadioButton
import com.example.cointrack.ui.util.primary.PrimaryTextField
import com.example.cointrack.util.extentions.conditional

private const val NAV_BACK_ICON_SIZE = 18

@Composable
fun TransactionDetailsScreen(
    navController: NavHostController
) {

    val viewModel = hiltViewModel<TransactionDetailsScreenViewModel>()

    TransactionDetailsScreenView(viewModel)

    IsLoadingState(viewModel)

    IsErrorState(viewModel)

    CategoryDialogsSate(viewModel)

    EventsHandler(viewModel, navController)
}

@Composable
private fun TransactionDetailsScreenView(
    viewModel: TransactionDetailsScreenViewModel
) {

    val focusManager = LocalFocusManager.current

    val transactionDetails by remember { viewModel.displayedTransactionDetails }

    val isEditing by remember { viewModel.isEditing }
    val isSaveButtonVisible by remember { viewModel.isSaveButtonVisible }

    BoxWithDiagonalBackgroundPattern {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.spacing.medium)
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
        ) {

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            AddTransactionHeader(
                onNavigateBackClicked = viewModel::onNavigateBackIconClicked,
                isEditing = isEditing,
                onEditClicked = viewModel::onEditButtonClicked,
                onStopEditingClicked = viewModel::onStopEditingClicked
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            TransactionTypeSection(
                type = transactionDetails?.type ?: EXPENSE,
                isEditing = isEditing,
                onTransactionTypeClicked = { viewModel.onTransactionTypeChanged(it) }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            AmountSection(viewModel, isEditing)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            CategorySection(
                category = transactionDetails?.category ?: "",
                isEditing = isEditing,
                onCategorySectionClick = {

                    focusManager.clearFocus()
                    viewModel.onCategorySectionClicked()
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            NoteSection(
                note = transactionDetails?.note ?: "",
                isEditing = isEditing,
                onNoteChanged = { viewModel.onTransactionNoteChanged(it) }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            AccountSection(
                source = transactionDetails?.source ?: UNKNOWN,
                isEditing = isEditing,
                onSourceClick = { viewModel.onTransactionSourceChanged(it) }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            DescriptionSection(
                description = transactionDetails?.description ?: "",
                isEditing = isEditing,
                onDescriptionChanged = { viewModel.onTransactionDescriptionChanged(it) }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        }

        StickyCTAButton(
            isVisible = isSaveButtonVisible,
            onClick = viewModel::onSaveButtonClicked
        )
    }
}

@Composable
private fun AddTransactionHeader(
    isEditing: Boolean,
    onNavigateBackClicked: () -> Unit,
    onEditClicked: () -> Unit,
    onStopEditingClicked: () -> Unit
) {

    if (isEditing) {

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            PrimaryHeader(
                modifier = Modifier.align(Alignment.CenterStart),
                headerLeadingIcon = painterResource(id = R.drawable.navigate_back_icon),
                headerTitle = "Edit Transaction",
                headerIconsSize = NAV_BACK_ICON_SIZE.dp,
                headerIconsTint = MaterialTheme.colors.onBackground,
                isHeaderLeadingIconClickable = true,
                onHeaderLeadingIconClick = onNavigateBackClicked
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clip(CircleShape)
                    .clickable { onStopEditingClicked() }
                    .padding(
                        vertical = MaterialTheme.spacing.extraSmall,
                        horizontal = MaterialTheme.spacing.small
                    )
            ) {

                Text(
                    text = "Stop Editing",
                    style = MaterialTheme.typography.h5,
                    color = RedError
                )
            }

        }

    } else {

        PrimaryHeader(
            headerLeadingIcon = painterResource(id = R.drawable.navigate_back_icon),
            headerTitle = "Transaction Details",
            headerIconsSize = NAV_BACK_ICON_SIZE.dp,
            headerIconsTint = MaterialTheme.colors.onBackground,
            isHeaderLeadingIconClickable = true,
            onHeaderLeadingIconClick = onNavigateBackClicked,
            additionalActionIcons = listOf(
                Pair(painterResource(id = R.drawable.edit_icon)) { onEditClicked() }
            )
        )
    }
}

@Composable
private fun TransactionTypeSection(
    type: TransactionType,
    isEditing: Boolean,
    onTransactionTypeClicked: (type: TransactionType) -> Unit
) {

    Row {

        TransactionTypeBubble(
            transactionType = EXPENSE,
            isSelected = type == EXPENSE,
            isEnabled = isEditing,
            onClick = { onTransactionTypeClicked(EXPENSE) }
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.large))

        TransactionTypeBubble(
            transactionType = INCOME,
            isSelected = type == INCOME,
            isEnabled = isEditing,
            onClick = { onTransactionTypeClicked(INCOME) }
        )
    }
}

@Composable
private fun RowScope.TransactionTypeBubble(
    transactionType: TransactionType,
    isSelected: Boolean,
    isEnabled: Boolean,
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
            .conditional(isEnabled) {
                clickable { onClick() }
            }
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

@Composable
private fun AmountSection(
    viewModel: TransactionDetailsScreenViewModel,
    isEditing: Boolean
) {

    val focusManager = LocalFocusManager.current

    val transactionAmountTextInput by remember { viewModel.transactionAmountTextInput }

    PrimaryTextField(
        text = transactionAmountTextInput,
        onTextChanged = { viewModel.onTransactionAmountChanged(it) },
        placeholder = "Amount",
        trailingText = "RSD",
        enabled = isEditing,
        keyboardOptions = KeyboardOptions(
            keyboardType = Number,
            imeAction = Done
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}

@Composable
private fun CategorySection(
    category: String,
    isEditing: Boolean,
    onCategorySectionClick: () -> Unit
) {

    RowInputField(
        item = category,
        placeholder = "Category",
        enabled = isEditing,
        onRowClicked = onCategorySectionClick
    )
}

@Composable
private fun NoteSection(
    note: String,
    isEditing: Boolean,
    onNoteChanged: (String) -> Unit
) {

    val focusManager = LocalFocusManager.current

    PrimaryTextField(
        text = note,
        onTextChanged = { onNoteChanged(it) },
        placeholder = "Note",
        enabled = isEditing,
        trailingIcon = Icons.Default.Close,
        onTrailingIconClick = { onNoteChanged("") },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}

@Composable
private fun AccountSection(
    source: TransactionsSource,
    isEditing: Boolean,
    onSourceClick: (TransactionsSource) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        TransactionsSource.values().filter { it != UNKNOWN }.forEach { transactionSource ->

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                PrimaryRadioButton(
                    isSelected = transactionSource == source,
                    enabled = isEditing,
                    onClick = { onSourceClick(transactionSource) }
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                Text(
                    text = transactionSource.toDisplayString(),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.onBackground
                )

            }
        }
    }
}

@Composable
private fun DescriptionSection(
    description: String,
    isEditing: Boolean,
    onDescriptionChanged: (String) -> Unit
) {

    val focusManager = LocalFocusManager.current

    PrimaryTextField(
        text = description,
        onTextChanged = { onDescriptionChanged(it) },
        singleLine = false,
        enabled = isEditing,
        placeholder = "Description",
        trailingIcon = Icons.Default.Close,
        onTrailingIconClick = { onDescriptionChanged("") },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}

@Composable
private fun IsLoadingState(
    viewModel: TransactionDetailsScreenViewModel
) {

    val isLoading by remember { viewModel.isLoading }

    if (isLoading) {

        TransactionDetailsLoadingScreen()
    }
}

@Composable
private fun IsErrorState(
    viewModel: TransactionDetailsScreenViewModel
) {

    val isError by remember { viewModel.isError }

    isError?.let { error ->

        PrimaryErrorScreen(errorText = error)
    }
}

@Composable
private fun CategoryDialogsSate(
    viewModel: TransactionDetailsScreenViewModel
) {

    val isSelectDialogVisible by remember { viewModel.isSelectCategoryDialogVisible }
    val isCreateDialogVisible by remember { viewModel.isAddCategoryDialogVisible }

    val categories by remember { viewModel.categories }

    val isLoadingCategories by remember { viewModel.isLoadingCategories }
    val isErrorLoadingCategories by remember { viewModel.isErrorLoadingCategories }

    SelectCategoryDialog(
        isDisplayed = isSelectDialogVisible,
        categories = categories,
        onDismissed = viewModel::onSelectCategoryDialogDismissed,
        onCategoryClicked = { viewModel.onSelectCategoryDialogConfirmed(it) },
        onAddCategoryClicked = viewModel::onAddCategoryClicked,
        isLoading = isLoadingCategories,
        isError = isErrorLoadingCategories
    )

    CreateCategoryDialog(
        isDisplayed = isCreateDialogVisible,
        existingCategories = categories,
        onDialogDismissed = viewModel::onAddCategoryDialogDismissed,
        onDialogConfirmed = { viewModel.onAddCategoryDialogConfirmed(it) }
    )
}

@Composable
private fun EventsHandler(
    viewModel: TransactionDetailsScreenViewModel,
    navController: NavHostController
) {

    val event by viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event) {

        when (event) {

            NavigateBack -> navController.popBackStack()
            else         -> { /* NO ACTION */ }
        }
    }
}