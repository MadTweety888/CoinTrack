package com.example.cointrack.ui.screens.main.addtransaction

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
import com.example.cointrack.ui.screens.main.addtransaction.AddTransactionScreenViewModel.Events.NavigateBack
import com.example.cointrack.ui.theme.Dark
import com.example.cointrack.ui.theme.Grey70
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.components.BoxWithDiagonalBackgroundPattern
import com.example.cointrack.ui.util.components.RowInputField
import com.example.cointrack.ui.util.components.TransactionTypeBubble
import com.example.cointrack.ui.util.components.buttons.StickyCTAButton
import com.example.cointrack.ui.util.components.dialogs.CreateCategoryDialog
import com.example.cointrack.ui.util.components.dialogs.SelectCategoryDialog
import com.example.cointrack.ui.util.components.loadings.AddTransactionLoadingScreen
import com.example.cointrack.ui.util.primary.PrimaryErrorScreen
import com.example.cointrack.ui.util.primary.PrimaryHeader
import com.example.cointrack.ui.util.primary.PrimaryRadioButton
import com.example.cointrack.ui.util.primary.PrimaryTextField
import com.example.cointrack.util.extensions.DateTimeFormatters.TRANSACTION_DATE_FORMAT
import com.example.cointrack.util.extensions.format
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime

private const val NAV_BACK_ICON_SIZE = 18

@Composable
fun AddTransactionScreen(
    navController: NavHostController
) {

    val viewModel = hiltViewModel<AddTransactionScreenViewModel>()

    AddTransactionScreenView(viewModel)

    IsLoadingState(viewModel)

    IsErrorState(viewModel)

    CategoryDialogsSate(viewModel)

    EventsHandler(viewModel, navController)
}

@Composable
private fun AddTransactionScreenView(
    viewModel: AddTransactionScreenViewModel
) {

    val focusManager = LocalFocusManager.current

    val transactionDraft by remember { viewModel.transactionDraft }

    val isSaveButtonVisible by remember { viewModel.isSaveButtonVisible }

    BoxWithDiagonalBackgroundPattern {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.spacing.medium)
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
        ) {

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            AddTransactionHeader(viewModel::onNavigateBackIconClicked)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            TransactionTypeSection(
                type = transactionDraft.type,
                onTransactionTypeClicked = { viewModel.onTransactionTypeChanged(it) }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            DateSection(
                date = transactionDraft.date,
                onDatePicked = { viewModel.onTransactionDateChanged(it) }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            AmountSection(viewModel)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            CategorySection(
                category = transactionDraft.category,
                onCategorySectionClick = {

                    focusManager.clearFocus()
                    viewModel.onCategorySectionClicked()
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            NoteSection(
                note = transactionDraft.note,
                onNoteChanged = { viewModel.onTransactionNoteChanged(it) }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            AccountSection(
                source = transactionDraft.source,
                onSourceClick = { viewModel.onTransactionSourceChanged(it) }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            DescriptionSection(
                description = transactionDraft.description,
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
    onNavigateBackClicked: () -> Unit
) {

    PrimaryHeader(
        headerLeadingIcon = painterResource(id = R.drawable.navigate_back_icon),
        headerTitle = "Add Transaction",
        headerIconsSize = NAV_BACK_ICON_SIZE.dp,
        headerIconsTint = MaterialTheme.colors.onBackground,
        isHeaderLeadingIconClickable = true,
        onHeaderLeadingIconClick = onNavigateBackClicked
    )
}

@Composable
private fun TransactionTypeSection(
    type: TransactionType,
    onTransactionTypeClicked: (type: TransactionType) -> Unit
) {

    Row {

        TransactionTypeBubble(
            transactionType = EXPENSE,
            isSelected = type == EXPENSE,
            onClick = { onTransactionTypeClicked(EXPENSE) }
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.large))

        TransactionTypeBubble(
            transactionType = INCOME,
            isSelected = type == INCOME,
            onClick = { onTransactionTypeClicked(INCOME) }
        )
    }
}

@Composable
private fun DateSection(
    date: LocalDateTime?,
    onDatePicked: (LocalDateTime?) -> Unit
) {

    val focusManager = LocalFocusManager.current

    val dateDialogState = rememberMaterialDialogState()

    RowInputField(
        item = date.format(TRANSACTION_DATE_FORMAT),
        placeholder = "Date",
        trailingIcon = painterResource(id = R.drawable.calendar_icon),
        onRowClicked = {

            dateDialogState.show()
            focusManager.clearFocus()
        }
    )

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {

            positiveButton("Confirm")

            negativeButton("Cancel")
        }
    ) {

        datepicker(
            initialDate = date?.toLocalDate() ?: LocalDate.now(),
            title = "Pick a transaction date",
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colors.primary,
                headerTextColor = MaterialTheme.colors.onPrimary
            )
        ) { pickedDate ->

            onDatePicked(pickedDate.atStartOfDay())
        }
    }
}

@Composable
private fun AmountSection(
    viewModel: AddTransactionScreenViewModel
) {

    val focusManager = LocalFocusManager.current

    val transactionAmountTextInput by remember { viewModel.transactionAmountTextInput }

    PrimaryTextField(
        text = transactionAmountTextInput,
        onTextChanged = { viewModel.onTransactionAmountChanged(it) },
        placeholder = "Amount",
        trailingText = "RSD",
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
    onCategorySectionClick: () -> Unit
) {

    RowInputField(
        item = category,
        placeholder = "Category",
        onRowClicked = onCategorySectionClick
    )
}

@Composable
private fun NoteSection(
    note: String,
    onNoteChanged: (String) -> Unit
) {

    val focusManager = LocalFocusManager.current

    PrimaryTextField(
        text = note,
        onTextChanged = { onNoteChanged(it) },
        placeholder = "Note",
        trailingIcon = Icons.Default.Close,
        onTrailingIconClick = { onNoteChanged("") },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}

@Composable
private fun AccountSection(
    source: TransactionsSource,
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
    onDescriptionChanged: (String) -> Unit
) {

    val focusManager = LocalFocusManager.current

    PrimaryTextField(
        text = description,
        onTextChanged = { onDescriptionChanged(it) },
        singleLine = false,
        placeholder = "Description",
        trailingIcon = Icons.Default.Close,
        onTrailingIconClick = { onDescriptionChanged("") },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}

@Composable
private fun IsLoadingState(
    viewModel: AddTransactionScreenViewModel
) {

    val isLoading by remember { viewModel.isLoading }

    if (isLoading) {

        AddTransactionLoadingScreen()
    }
}

@Composable
private fun IsErrorState(
    viewModel: AddTransactionScreenViewModel
) {

    val isError by remember { viewModel.isError }

    isError?.let { error ->

        PrimaryErrorScreen(errorText = error)
    }
}

@Composable
private fun CategoryDialogsSate(
    viewModel: AddTransactionScreenViewModel
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
    viewModel: AddTransactionScreenViewModel,
    navController: NavHostController
) {

    val event by viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 =  event) {

        when (event) {

            NavigateBack -> navController.popBackStack()
            else         -> { /* NO ACTION */ }
        }
    }
}