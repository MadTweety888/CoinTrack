package com.example.cointrack.repository.interactors

import com.example.cointrack.domain.models.Transaction
import com.example.cointrack.util.Resource
import kotlinx.coroutines.flow.Flow

interface TransactionsInteractor {

    fun getTransactionsSimulated(isError: Boolean): Flow<Resource<List<Transaction>>>
}