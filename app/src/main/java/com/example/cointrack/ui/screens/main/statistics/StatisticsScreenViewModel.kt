package com.example.cointrack.ui.screens.main.statistics

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cointrack.domain.enums.TransactionType
import com.example.cointrack.domain.enums.TransactionType.EXPENSE
import com.example.cointrack.domain.models.Transaction
import com.example.cointrack.repository.interactors.AuthInteractor
import com.example.cointrack.repository.interactors.TransactionsInteractor
import com.example.cointrack.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class StatisticsScreenViewModel @Inject constructor(
    private val authRepository: AuthInteractor,
    private val transactionsRepository: TransactionsInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    var userId: String? = null

    private val fetchedTransactions = mutableStateOf<List<Transaction>>(emptyList())

    val selectedDate = mutableStateOf(LocalDateTime.now())

    val selectedTransactionType = mutableStateOf(EXPENSE)

    val calculatedTransactions = derivedStateOf {

        fetchedTransactions.value.filter {
            it.date?.month == selectedDate.value.month &&
                    it.date?.year == selectedDate.value.year &&
                    it.type == selectedTransactionType.value
        }
    }

    val isLoading = mutableStateOf(false)
    val isError = mutableStateOf<String?>(null)

    init {

        initUserId()

        fetchTransactions()
    }

    private fun initUserId() =  viewModelScope.launch {

        if (authRepository.hasUser()) userId = authRepository.getUserId()
    }

    private fun fetchTransactions() = viewModelScope.launch {

        val userId = authRepository.getUserId()

        if (userId.isBlank()) return@launch

        transactionsRepository.getTransactions(userId).collect { result ->

            when (result) {

                is Resource.Success -> handleFetchTransactionsSuccess(result.data)
                is Resource.Error   -> handleFetchTransactionsError(result.message)
                is Resource.Loading -> handleFetchTransactionsLoading(result.isLoading)
            }
        }
    }

    //region Fetch Transactions Helpers

    private fun handleFetchTransactionsSuccess(transactions: List<Transaction>?) {

        this.fetchedTransactions.value = transactions ?: emptyList()
    }

    private fun handleFetchTransactionsError(error: String?) {

        isError.value = error
    }

    private fun handleFetchTransactionsLoading(isLoading: Boolean) {

        this.isLoading.value = isLoading
    }

    //endregion

    fun onPreviousMonthIconClicked() {

        selectedDate.value = selectedDate.value.minusMonths(1)
    }

    fun onNextMonthIconClicked() {

        selectedDate.value = selectedDate.value.plusMonths(1)
    }

    fun onSelectedTransactionTypeChanged(type: TransactionType) {

        selectedTransactionType.value = type
    }

    fun calculateTotalSumPerCategory(): Map<String, Double> {

        val groupedTransactions = calculatedTransactions.value.groupBy { it.category }

        return groupedTransactions.mapValues { transactionsForCategory ->

            transactionsForCategory.value.fold(0.0) { total, transaction ->

                total + transaction.amount
            }
        }
    }

    fun calculatePieChartValues(): Map<String, Float> {

        val groupedTransactions = calculatedTransactions.value.groupBy { it.category }

        val totalSum = calculatedTransactions.value.fold(0.0) { total, transaction ->

            total + transaction.amount
        }

        val totalSumsPerCategory = groupedTransactions.mapValues { transactionsForCategory ->

            transactionsForCategory.value.fold(0.0) { total, transaction ->

                total + transaction.amount
            }
        }

        return totalSumsPerCategory.mapValues { totalSumForCategory ->

            360f * totalSumForCategory.value.toFloat() / totalSum.toFloat()
        }
    }

    fun onRetryButtonClicked() {

        fetchTransactions()
    }

    sealed class Events {


    }
}