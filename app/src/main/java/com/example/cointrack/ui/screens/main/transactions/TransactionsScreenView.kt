@file:OptIn(ExperimentalFoundationApi::class)

package com.example.cointrack.ui.screens.main.transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cointrack.R
import com.example.cointrack.domain.enums.SortOrder.DATE_ASCENDING
import com.example.cointrack.domain.enums.SortOrder.DATE_DESCENDING
import com.example.cointrack.domain.enums.TransactionType.EXPENSE
import com.example.cointrack.domain.enums.TransactionType.INCOME
import com.example.cointrack.domain.enums.toDisplayString
import com.example.cointrack.domain.models.Transaction
import com.example.cointrack.domain.util.FilterOption
import com.example.cointrack.ui.activities.MainActivityViewModel
import com.example.cointrack.ui.navigation.Routes.ADD_TRANSACTION_SCREEN
import com.example.cointrack.ui.navigation.Routes.TRANSACTION_DETAILS_SCREEN
import com.example.cointrack.ui.screens.main.transactions.TransactionsScreenViewModel.Events.NavigateToAddTransaction
import com.example.cointrack.ui.screens.main.transactions.TransactionsScreenViewModel.Events.NavigateToTransactionDetails
import com.example.cointrack.ui.theme.Dark
import com.example.cointrack.ui.theme.GreenValid
import com.example.cointrack.ui.theme.Grey70
import com.example.cointrack.ui.theme.GreyLight
import com.example.cointrack.ui.theme.RedError
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.ComponentSizes
import com.example.cointrack.ui.util.components.BoxWithDiagonalBackgroundPattern
import com.example.cointrack.ui.util.components.dialogs.SortAndFilterDialog
import com.example.cointrack.ui.util.components.loadings.TransactionsLoadingScreen
import com.example.cointrack.ui.util.primary.PrimaryErrorScreen
import com.example.cointrack.ui.util.primary.PrimaryHeader
import com.example.cointrack.util.extensions.addArgs
import com.example.cointrack.util.extensions.toDp
import java.time.Month

@Composable
fun TransactionsScreen(
    navController: NavHostController,
    mainViewModel: MainActivityViewModel
) {

    val viewModel = hiltViewModel<TransactionsScreenViewModel>()

    TransactionsScreenView(viewModel, mainViewModel)

    IsErrorState(viewModel)

    SortAndFilterDialogState(viewModel, mainViewModel)

    EventsHandler(navController, viewModel)
}

@Composable
private fun TransactionsScreenView(
    viewModel: TransactionsScreenViewModel,
    mainViewModel: MainActivityViewModel
) {

    BoxWithDiagonalBackgroundPattern {

        Column(
            modifier = Modifier.padding(
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium,
                bottom = ComponentSizes.BOTTOM_NAV_BAR_HEIGHT.dp
            )
        ) {

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            TransactionsHeader(
                onAddTransactionClick = viewModel::onAddTransactionButtonClicked,
                onSortAndFilterClick = {

                    mainViewModel.hideBottomNavBar()
                    viewModel.onSortAndFilterButtonClicked()
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            TransactionsList(viewModel)
        }
    }
}

@Composable
private fun TransactionsHeader(
    onAddTransactionClick: () -> Unit,
    onSortAndFilterClick: () -> Unit
) {

    PrimaryHeader(
        headerLeadingIcon = Icons.Default.ReceiptLong,
        headerTitle = "Transactions",
        additionalActionIcons = listOf(
            Pair(painterResource(id = R.drawable.add_item_icon)) { onAddTransactionClick() },
            Pair(painterResource(id = R.drawable.sort_filter_icon)) { onSortAndFilterClick() },
        )
    )
}

@Composable
private fun TransactionsList(
    viewModel: TransactionsScreenViewModel
) {

    val transactions by remember { viewModel.sortedAndFilteredTransactions }

    val groupedTransactions = remember(transactions) {

        transactions.groupBy { it.date?.month }
    }

    val isLoading by remember { viewModel.isLoading }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {

        if (!isLoading) {

            LazyColumn(
                Modifier.fillMaxSize()
            ) {

                groupedTransactions.forEach { group ->

                    stickyHeader {

                        TransactionListStickyHeader(group)
                    }

                    items(group.value) { transaction ->

                        TransactionListItem(
                            transaction = transaction,
                            onClick = { viewModel.onTransactionClicked(transaction.id) }
                        )
                    }
                }
            }

        } else {

            TransactionsLoadingScreen(maxHeight)
        }
    }
}

@Composable
private fun TransactionListStickyHeader(
    groupOfItems: Map.Entry<Month?, List<Transaction>>
) {

    val total = remember(groupOfItems.value) {

        groupOfItems.value.fold(0.0) { total, transaction: Transaction ->

            when (transaction.type) {

                INCOME  -> total + transaction.amount
                EXPENSE -> total - transaction.amount
                else    -> total
            }
        }
    }

    Row(
        modifier = Modifier
            .padding(bottom = MaterialTheme.spacing.medium)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(Dark)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.large
            )
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = groupOfItems.key?.toString() ?: "UNCATEGORIZED",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface
        )

        Text(
            text = buildAnnotatedString {

                append("Total: ")

                withStyle(
                    style = SpanStyle(
                        color = if (total >= 0) GreenValid else RedError,
                        fontSize = MaterialTheme.typography.subtitle1.fontSize,
                        fontStyle = MaterialTheme.typography.subtitle1.fontStyle,
                        fontFamily = MaterialTheme.typography.subtitle1.fontFamily,
                        fontWeight = MaterialTheme.typography.subtitle1.fontWeight,
                        letterSpacing = MaterialTheme.typography.subtitle1.letterSpacing,
                    )
                ) {

                    append(total.toString())
                }
            },
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
private fun TransactionListItem(
    transaction: Transaction,
    onClick: () -> Unit
) {

    val density = LocalDensity.current

    Row(
        modifier = Modifier
            .padding(bottom = MaterialTheme.spacing.small)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colors.surface)
            .clickable { onClick() }
            .padding(
                horizontal = MaterialTheme.spacing.small,
                vertical = MaterialTheme.spacing.extraSmall
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier.width(75.dp)) {

            Text(
                text = transaction.category,
                style = MaterialTheme.typography.body1,
                color = GreyLight,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

        Box(
            modifier = Modifier
                .width(1.dp)
                .height(
                    MaterialTheme.typography.caption.lineHeight.toDp(density) +
                    MaterialTheme.spacing.extraSmall +
                    MaterialTheme.typography.body1.lineHeight.toDp(density)
                )
                .background(Grey70)
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

        Column(modifier = Modifier.weight(1f)) {

            Text(
                text = transaction.note,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

            Text(
                text = transaction.source.toDisplayString(),
                style = MaterialTheme.typography.body1,
                color = GreyLight
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

        Box {

            Text(
                text = transaction.amount.toString(),
                style = MaterialTheme.typography.caption,
                color = if (transaction.type == EXPENSE) RedError else GreenValid
            )
        }
    }
}

@Composable
private fun IsErrorState(
    viewModel: TransactionsScreenViewModel
) {

    val isError by remember { viewModel.isError }

    isError?.let { error ->

        PrimaryErrorScreen(
            errorText = error,
            hasRetryButton = true,
            onRetryClicked = viewModel::onRetryClicked
        )
    }
}

@Composable
private fun SortAndFilterDialogState(
    viewModel: TransactionsScreenViewModel,
    mainViewModel: MainActivityViewModel
) {

    val isDialogDisplayed by remember { viewModel.isSortAndFilterDialogVisible }

    val selectedSort by remember { viewModel.selectedSort }
    val selectedFilter by remember { viewModel.selectedFilter }

    SortAndFilterDialog(
        isDisplayed = isDialogDisplayed,
        isSortToggleable = false,
        initialSelectedSort = selectedSort,
        sortOptions = listOf(DATE_DESCENDING, DATE_ASCENDING),
        initialSelectedFilter = selectedFilter,
        filterOptions = listOf(
            FilterOption("All", null),
            FilterOption("Expenses", EXPENSE),
            FilterOption("Incomes", INCOME)
        ),
        onDoneClicked = { sort, filter ->

            viewModel.onSortAndFilterDialogConfirmed(sort, filter)
            mainViewModel.showBottomNavBar()
        },
        onCancelCTAClicked = {

            viewModel.onSortAndFilterDialogReset()
            mainViewModel.showBottomNavBar()
        },
        onOutsideOfDialogClicked = {

            viewModel.onSortAndFilterDialogDismissed()
            mainViewModel.showBottomNavBar()
        },
        cancelDialogCTA = { ResetFilterDialogClickableText(it) }
    )
}

@Composable
private fun ResetFilterDialogClickableText(onClick: () -> Unit) {

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(
                vertical = MaterialTheme.spacing.extraSmall,
                horizontal = MaterialTheme.spacing.small
            ),
        contentAlignment = Center
    ) {

        Text(
            text = "Reset",
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: TransactionsScreenViewModel
) {

    val event by viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event) {

        when (event) {

            is NavigateToAddTransaction     -> { navController.navigate(ADD_TRANSACTION_SCREEN) }
            is NavigateToTransactionDetails -> {

                val selectedTransactionId = (event as NavigateToTransactionDetails).selectedTransactionId

                navController.navigate(TRANSACTION_DETAILS_SCREEN.addArgs(selectedTransactionId))
            }
            else                            -> { /* NO ACTION */ }
        }
    }
}