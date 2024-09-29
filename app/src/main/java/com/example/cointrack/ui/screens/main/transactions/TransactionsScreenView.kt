package com.example.cointrack.ui.screens.main.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cointrack.R
import com.example.cointrack.domain.enums.TransactionType.EXPENSE
import com.example.cointrack.domain.enums.toDisplayString
import com.example.cointrack.domain.models.Transaction
import com.example.cointrack.ui.navigation.Routes.ADD_TRANSACTION_SCREEN
import com.example.cointrack.ui.navigation.Routes.TRANSACTION_DETAILS_SCREEN
import com.example.cointrack.ui.screens.main.transactions.TransactionsScreenViewModel.Events.NavigateToAddTransaction
import com.example.cointrack.ui.screens.main.transactions.TransactionsScreenViewModel.Events.NavigateToTransactionDetails
import com.example.cointrack.ui.theme.GreenValid
import com.example.cointrack.ui.theme.GreyLight
import com.example.cointrack.ui.theme.RedError
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.ComponentSizes
import com.example.cointrack.ui.util.components.BoxWithBackgroundPattern
import com.example.cointrack.ui.util.primary.PrimaryErrorScreen
import com.example.cointrack.ui.util.primary.PrimaryHeader
import com.example.cointrack.ui.util.primary.PrimaryLoadingScreen
import com.example.cointrack.util.extentions.addArgs

@Composable
fun TransactionsScreen(
    navController: NavHostController
) {

    val viewModel = hiltViewModel<TransactionsScreenViewModel>()

    TransactionsScreenView(viewModel)

    IsLoadingState(viewModel)

    IsErrorState(viewModel)

    EventsHandler(navController, viewModel)
}

@Composable
private fun TransactionsScreenView(
    viewModel: TransactionsScreenViewModel
) {

    BoxWithBackgroundPattern {

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
                onSortAndFilterClick = viewModel::onSortAndFilterButtonClicked
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

    val transactions by remember { viewModel.transactions }

    LazyColumn(
        Modifier.fillMaxSize()
    ) {

        items(transactions) { transaction ->

            TransactionListItem(transaction) { viewModel.onTransactionClicked(transaction.id) }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}

@Composable
private fun TransactionListItem(
    transaction: Transaction,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colors.surface)
            .clickable { onClick() }
            .padding(
                horizontal = MaterialTheme.spacing.small,
                vertical = MaterialTheme.spacing.extraSmall
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier.weight(0.25f)) {

            Text(
                text = transaction.category,
                style = MaterialTheme.typography.body1,
                color = GreyLight,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

        Column(modifier = Modifier.weight(0.75f)) {

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
private fun IsLoadingState(
    viewModel: TransactionsScreenViewModel
) {

    val isLoading by remember { viewModel.isLoading }

    if (isLoading) PrimaryLoadingScreen()
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