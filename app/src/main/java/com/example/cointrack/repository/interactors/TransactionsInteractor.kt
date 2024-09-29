package com.example.cointrack.repository.interactors

import com.example.cointrack.domain.models.TransactionDraft
import com.example.cointrack.util.Resource
import kotlinx.coroutines.flow.Flow

interface TransactionsInteractor {

    fun createTransaction(userId: String, draft: TransactionDraft): Flow<Resource<Unit>>
}