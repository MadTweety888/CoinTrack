package com.example.cointrack.repository.implementations

import com.example.cointrack.domain.models.Transaction
import com.example.cointrack.domain.models.TransactionDraft
import com.example.cointrack.repository.constants.CollectionReferences.TRANSACTIONS_COLLECTION_REF
import com.example.cointrack.repository.interactors.TransactionsInteractor
import com.example.cointrack.repository.mappers.toCreateTransactionDTO
import com.example.cointrack.repository.mappers.toTransaction
import com.example.cointrack.repository.mappers.toTransactionResponseDTO
import com.example.cointrack.repository.mappers.toTransactionResponseDTOs
import com.example.cointrack.repository.mappers.toTransactions
import com.example.cointrack.repository.mappers.toUpdateTransactionDTO
import com.example.cointrack.util.Resource
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class TransactionsRepository: TransactionsInteractor {

    private val transactionsRef = Firebase.firestore.collection(TRANSACTIONS_COLLECTION_REF)

    override fun getTransactions(userId: String): Flow<Resource<List<Transaction>>> = callbackFlow {

        val snapshotStateListener: ListenerRegistration? = null

        try {

            transactionsRef
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, _ ->

                    trySend(Resource.Loading(true))

                    val response = if (snapshot != null) {

                        val transactions = snapshot
                            .documents
                            .toTransactionResponseDTOs()
                            .toTransactions()

                        Resource.Success(data = transactions)

                    } else {

                        Resource.Success(data = emptyList())
                    }

                    trySend(response)

                    trySend(Resource.Loading(false))
                }

        } catch (e: Exception) {

            e.printStackTrace()
            trySend(Resource.Error("Error loading transactions!"))
            trySend(Resource.Loading(false))
        }

        awaitClose { snapshotStateListener?.remove() }
    }

    override fun getTransactionDetails(transactionId: String): Flow<Resource<Transaction?>> = callbackFlow {

        val snapshotStateListener: ListenerRegistration? = null

        try {

            transactionsRef
                .document(transactionId)
                .addSnapshotListener { snapshot, _ ->

                    trySend(Resource.Loading(true))

                    val response = if (snapshot != null) {

                        val transactions = snapshot
                            .toTransactionResponseDTO()
                            .toTransaction()

                        Resource.Success(data = transactions)

                    } else {

                        Resource.Error(message = "Transaction not found!")
                    }

                    trySend(response)

                    trySend(Resource.Loading(false))
                }

        } catch (e: Exception) {

            e.printStackTrace()
            trySend(Resource.Error("Error loading transaction details!"))
            trySend(Resource.Loading(false))
        }

        awaitClose { snapshotStateListener?.remove() }
    }

    override fun createTransaction(userId: String, draft: TransactionDraft): Flow<Resource<Unit>> {

        return flow {

            emit(Resource.Loading(true))

            try {

                transactionsRef.add(draft.toCreateTransactionDTO(userId)).await()

                emit(Resource.Success(Unit))
                emit(Resource.Loading(false))

            } catch (e: Exception) {

                e.printStackTrace()
                emit(Resource.Error("Error creating transaction!"))
                emit(Resource.Loading(false))
            }
        }
    }

    override fun updateTransaction(transaction: Transaction): Flow<Resource<Unit>> {

        return flow {

            emit(Resource.Loading(true))

            try {

                transactionsRef
                    .document(transaction.id)
                    .set(transaction.toUpdateTransactionDTO())
                    .await()

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