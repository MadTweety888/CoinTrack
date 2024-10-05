package com.example.cointrack.ui.screens.main.statistics

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cointrack.domain.enums.TransactionType.EXPENSE
import com.example.cointrack.domain.enums.TransactionType.INCOME
import com.example.cointrack.ui.theme.Grey70
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.ComponentSizes
import com.example.cointrack.ui.util.components.AnimatedPieChart
import com.example.cointrack.ui.util.components.BoxWithDiagonalBackgroundPattern
import com.example.cointrack.ui.util.components.TransactionTypeBubble
import com.example.cointrack.ui.util.components.loadings.StatisticsLoadingScreen
import com.example.cointrack.ui.util.primary.PrimaryErrorScreen
import com.example.cointrack.ui.util.primary.PrimaryHeader
import com.example.cointrack.util.extensions.DateTimeFormatters.STATISTICS_DATE_FORMAT
import com.example.cointrack.util.extensions.format

@Composable
fun StatisticsScreen(
    navController: NavHostController
) {

    val viewModel = hiltViewModel<StatisticsScreenViewModel>()

    StatisticsScreenView(viewModel)

    IsLoadingState(viewModel)

    IsErrorState(viewModel)
}

@Composable
private fun StatisticsScreenView(
    viewModel: StatisticsScreenViewModel
) {

    BoxWithDiagonalBackgroundPattern {

        Column(
            modifier = Modifier.padding(
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium,
                bottom = ComponentSizes.BOTTOM_NAV_BAR_HEIGHT.dp
            )
        ) {

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large + MaterialTheme.spacing.medium))

            HeaderSection()

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            SelectMonthSection(viewModel)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            SelectTransactionTypeSection(viewModel)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            StatisticsSection(viewModel)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}

@Composable
private fun HeaderSection() {

    PrimaryHeader(
        headerLeadingIcon = Icons.Default.BarChart,
        headerTitle = "Statistics"
    )
}

@Composable
private fun SelectMonthSection(
    viewModel: StatisticsScreenViewModel
) {

    val selectedDate by remember { viewModel.selectedDate }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .border(
                width = 1.dp,
                color = Grey70,
                shape = MaterialTheme.shapes.large
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        
        IconButton(onClick = viewModel::onPreviousMonthIconClicked) {

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null
            )
        }

        Text(
            text = selectedDate.format(STATISTICS_DATE_FORMAT),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onBackground
        )

        IconButton(onClick = viewModel::onNextMonthIconClicked) {

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun SelectTransactionTypeSection(
    viewModel: StatisticsScreenViewModel
) {

    val selectedTransactionType by remember { viewModel.selectedTransactionType }

    Row {

        TransactionTypeBubble(
            transactionType = EXPENSE,
            isSelected = selectedTransactionType == EXPENSE,
            onClick = { viewModel.onSelectedTransactionTypeChanged(EXPENSE) }
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.large))

        TransactionTypeBubble(
            transactionType = INCOME,
            isSelected = selectedTransactionType == INCOME,
            onClick = { viewModel.onSelectedTransactionTypeChanged(INCOME) }
        )
    }
}

@Composable
private fun ColumnScope.StatisticsSection(
    viewModel: StatisticsScreenViewModel
) {

    val calculatedTransactions by remember { viewModel.calculatedTransactions }

    val totalSumPerCategory = remember(calculatedTransactions) {
        viewModel.calculateTotalSumPerCategory()
    }

    val normalisedPieChartValues = remember(calculatedTransactions) {
        viewModel.calculatePieChartValues()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {

        if (calculatedTransactions.isEmpty()) {

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "No transactions data",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onBackground
            )

        } else {

            AnimatedPieChart(
                data = totalSumPerCategory,
                normalisedData = normalisedPieChartValues
            )
        }
    }
}

@Composable
private fun IsLoadingState(
    viewModel: StatisticsScreenViewModel
) {

    val isLoading by remember { viewModel.isLoading }

    if (isLoading) {

        StatisticsLoadingScreen()
    }
}

@Composable
private fun IsErrorState(
    viewModel: StatisticsScreenViewModel
) {

    val isError by remember { viewModel.isError }

    isError?.let { error ->

        PrimaryErrorScreen(
            errorText = error,
            hasRetryButton = true,
            onRetryClicked = viewModel::onRetryButtonClicked
        )
    }
}