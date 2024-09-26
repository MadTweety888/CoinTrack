package com.example.cointrack.ui.screens.main.transactions

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cointrack.domain.models.Transaction
import com.example.cointrack.repository.interactors.AuthInteractor
import com.example.cointrack.repository.interactors.TransactionsInteractor
import com.example.cointrack.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsScreenViewModel @Inject constructor(
    private val authRepository: AuthInteractor,
    private val transactionsRepository: TransactionsInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    var userId: String? = null

    val transactions = mutableStateOf<List<Transaction>>(emptyList())

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

        transactionsRepository.getTransactionsSimulated(false).collect { result ->

            when (result) {

                is Resource.Success -> handleFetchTransactionsSuccess(result.data)
                is Resource.Error   -> handleFetchTransactionsError(result.message)
                is Resource.Loading -> handleFetchTransactionsLoading(result.isLoading)
            }
        }
    }

    //region Fetch Transactions Helpers

    private fun handleFetchTransactionsSuccess(transactions: List<Transaction>?) {

        this.transactions.value = transactions ?: emptyList()
    }

    private fun handleFetchTransactionsError(error: String?) {

        isError.value = error
    }

    private fun handleFetchTransactionsLoading(isLoading: Boolean) {

        this.isLoading.value = isLoading
    }

    //endregion

    fun onRetryClicked() {

        isError.value = null

        fetchTransactions()
    }

    private fun navigateToTransactionDetails() = viewModelScope.launch {

        events.emit(Events.NavigateToTransactionDetails)
    }

    sealed class Events {

        object NavigateToTransactionDetails: Events()
    }
}