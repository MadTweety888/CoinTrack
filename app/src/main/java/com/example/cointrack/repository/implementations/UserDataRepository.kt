package com.example.cointrack.repository.implementations

import android.net.Uri
import com.example.cointrack.domain.models.UserData
import com.example.cointrack.repository.constants.CollectionReferences.USERS_DATA_COLLECTION_REF
import com.example.cointrack.repository.constants.CollectionReferences.USER_IMAGES_STORAGE_REF
import com.example.cointrack.repository.interactors.UserDataInteractor
import com.example.cointrack.util.Resource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class UserDataRepository: UserDataInteractor {

    private val usersDataRef: CollectionReference =
        Firebase.firestore.collection(USERS_DATA_COLLECTION_REF)

    private val usersImagesRef: StorageReference =
        Firebase.storage.reference.child(USER_IMAGES_STORAGE_REF)

    override fun getUserData(userId: String): Flow<Resource<UserData?>> = callbackFlow {

        val snapshotStateListener: ListenerRegistration? = null

        try {

            usersDataRef
                .document(userId)
                .addSnapshotListener { snapshot, e ->

                    trySend(Resource.Loading(true))

                    val response = if (snapshot != null) {

                        val userData = snapshot.toObject(UserData::class.java)

                        Resource.Success(data = userData)

                    } else {

                        Resource.Error(message = e?.message ?: "User not found!")
                    }

                    trySend(response)

                    trySend(Resource.Loading(false))
                }

        } catch (e:Exception) {

            trySend(Resource.Error(e.message ?: "Something went wrong!"))
            trySend(Resource.Loading(false))
            e.printStackTrace()

        }

        awaitClose { snapshotStateListener?.remove() }
    }

    override fun addUserData(
        userId: String,
        name: String,
        surname: String,
        email: String
    ): Flow<Resource<Unit>> {

        val userData = UserData(
            id = userId,
            name = name,
            surname = surname,
            email = email
        )

        return flow {

            emit(Resource.Loading(true))

            try {

                usersDataRef
                    .document(userId)
                    .set(userData)
                    .await()

                emit(Resource.Success(Unit))

            } catch (e: Exception) {

                emit(Resource.Error(e.message ?: "Something went wrong!"))
                e.printStackTrace()

            } finally {

                emit(Resource.Loading(false))
            }
        }
    }

    override fun editUserData(userData: UserData): Flow<Resource<Unit>> {

        return flow {

            emit(Resource.Loading(true))

            try {

                usersDataRef
                    .document(userData.id)
                    .set(userData)
                    .await()

                emit(Resource.Success(Unit))

            } catch (e: Exception) {

                emit(Resource.Error(e.message ?: "Something went wrong!"))
                e.printStackTrace()

            } finally {

                emit(Resource.Loading(false))
            }
        }
    }

    override fun uploadUserPhoto(userId: String, photoUri: Uri): Flow<Resource<Unit>> {

        val imageRef = usersImagesRef.child(userId)

        return flow {

            emit(Resource.Loading(true))

            try {

                val uploadTask = imageRef.putFile(photoUri).await()

                if (uploadTask.task.isSuccessful) {

                    val downloadedUrl = imageRef.downloadUrl.await()

                    usersDataRef.document(userId)
                        .update("photoUrl", downloadedUrl.toString())
                        .await()

                    emit(Resource.Success(Unit))

                } else {

                    emit(Resource.Error("Photo upload failed!"))
                }

            } catch (e: Exception) {

                emit(Resource.Error(e.message ?: "Something went wrong!"))
                e.printStackTrace()

            } finally {

                emit(Resource.Loading(false))
            }
        }
    }

    override fun deleteUserData(userId: String): Boolean {

        try {

            usersDataRef.document(userId).delete()

            usersImagesRef.child(userId).delete()

            return true

        } catch (e: Exception) {

            e.printStackTrace()
            return false
        }
    }
}