package com.example.cointrack.repository.implementations

import com.example.cointrack.domain.models.Transaction
import com.example.cointrack.repository.interactors.TransactionsInteractor
import com.example.cointrack.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TransactionsRepository: TransactionsInteractor {

    override fun getTransactionsSimulated(isError: Boolean): Flow<Resource<List<Transaction>>> = flow {

        emit(Resource.Loading(true))

        try {

            delay(5000)

            if (isError) {

                emit(Resource.Error("Error fetching transactions!"))

            } else {

                val response = Transaction.getExampleTransactions()

                emit(Resource.Success(response))
            }

        } catch (t: Throwable) {

            t.printStackTrace()

            emit(Resource.Error("Error fetching transactions!"))

        } finally {

            emit(Resource.Loading(false))
        }
    }
}