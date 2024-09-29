package com.example.cointrack.repository.interactors

import com.example.cointrack.domain.models.Transaction
import com.example.cointrack.domain.models.TransactionDraft
import com.example.cointrack.util.Resource
import kotlinx.coroutines.flow.Flow

interface TransactionsInteractor {

    fun getTransactions(userId: String): Flow<Resource<List<Transaction>>>

    fun getTransactionDetails(transactionId: String): Flow<Resource<Transaction?>>

    fun createTransaction(userId: String, draft: TransactionDraft): Flow<Resource<Unit>>

    fun updateTransaction(transaction: Transaction): Flow<Resource<Unit>>
}