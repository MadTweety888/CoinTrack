package com.example.cointrack.repository.implementations

import com.example.cointrack.domain.models.TransactionDraft
import com.example.cointrack.repository.constants.CollectionReferences.TRANSACTIONS_COLLECTION_REF
import com.example.cointrack.repository.interactors.TransactionsInteractor
import com.example.cointrack.repository.mappers.toCreateTransactionDTO
import com.example.cointrack.util.Resource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class TransactionsRepository: TransactionsInteractor {

    private val transactionsRef = Firebase.firestore.collection(TRANSACTIONS_COLLECTION_REF)

    override fun createTransaction(userId: String, draft: TransactionDraft): Flow<Resource<Unit>> {

        return flow {

            emit(Resource.Loading(true))

            try {

                val tmp = transactionsRef.add(draft.toCreateTransactionDTO(userId)).await()

                Timber.i("TAGA: $tmp")

                emit(Resource.Success(Unit))
                emit(Resource.Loading(false))

            } catch (e: Exception) {

                e.printStackTrace()
                emit(Resource.Error("Error creating transaction!"))
                emit(Resource.Loading(false))
            }
        }
    }
}