package com.example.cointrack.repository.implementations

import com.example.cointrack.repository.constants.CollectionReferences.CATEGORIES_COLLECTION_REF
import com.example.cointrack.repository.interactors.CategoriesInteractor
import com.example.cointrack.util.Resource
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class CategoriesRepository: CategoriesInteractor {

    private val categoriesRef = Firebase.firestore.collection(CATEGORIES_COLLECTION_REF)

    override fun getCategories(userId: String): Flow<Resource<List<String>>> = callbackFlow {

        val snapshotStateListener: ListenerRegistration? = null

        try {

            categoriesRef
                .whereIn("userId", listOf("DEFAULT", userId))
                .addSnapshotListener { snapshot, _ ->

                    trySend(Resource.Loading(true))

                    val response = if (snapshot != null) {

                        val categories = snapshot.mapNotNull { it.getString("value") }

                        Resource.Success(data = categories)

                    } else {

                        Resource.Success(data = emptyList())
                    }

                    trySend(response)

                    trySend(Resource.Loading(false))
                }

        } catch (e: Exception) {

            e.printStackTrace()
            trySend(Resource.Error("Error loading categories!"))
            trySend(Resource.Loading(false))
        }

        awaitClose { snapshotStateListener?.remove() }
    }

    override fun createCategory(userId: String, category: String): Flow<Resource<Unit>> {

        val newCategory = hashMapOf(
            "userId" to userId,
            "value" to category
        )

        return flow {

            emit(Resource.Loading(true))

            try {

                categoriesRef.add(newCategory).await()

                emit(Resource.Success(Unit))
                emit(Resource.Loading(false))

            } catch (e: Exception) {

                e.printStackTrace()
                emit(Resource.Error("Error creating category!"))
                emit(Resource.Loading(false))

            }
        }
    }
}