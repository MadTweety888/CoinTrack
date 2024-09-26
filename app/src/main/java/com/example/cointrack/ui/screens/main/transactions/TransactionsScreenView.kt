package com.example.cointrack.ui.screens.main.transactions

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cointrack.domain.enums.TransactionType.EXPENSE
import com.example.cointrack.domain.enums.toDisplayString
import com.example.cointrack.domain.models.Transaction
import com.example.cointrack.ui.activities.MainActivityViewModel
import com.example.cointrack.ui.screens.main.transactions.TransactionsScreenViewModel.Events.NavigateToTransactionDetails
import com.example.cointrack.ui.theme.GreenValid
import com.example.cointrack.ui.theme.GreyLight
import com.example.cointrack.ui.theme.RedError
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.ComponentSizes
import com.example.cointrack.ui.util.components.BoxWithBackgroundPattern
import com.example.cointrack.ui.util.primary.PrimaryErrorScreen
import com.example.cointrack.ui.util.primary.PrimaryLoadingScreen

@Composable
fun TransactionsScreen(
    navController: NavHostController,
    mainActivityViewModel: MainActivityViewModel
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
                bottom = ComponentSizes.bottomNavBarHeight.dp
            )
        ) {

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

            TransactionsList(viewModel)
        }
    }
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

            TransactionListItem(transaction)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}

@Composable
private fun TransactionListItem(
    transaction: Transaction
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colors.surface)
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

    when (event) {

        is NavigateToTransactionDetails -> {/*TODO Implement navigation */}
        else                            -> { /* NO ACTION */ }
    }
}