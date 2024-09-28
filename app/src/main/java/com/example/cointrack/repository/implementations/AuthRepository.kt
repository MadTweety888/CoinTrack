package com.example.cointrack.repository.implementations

import com.example.cointrack.repository.interactors.AuthInteractor
import com.example.cointrack.util.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepository: AuthInteractor {

    override val currentUser: FirebaseUser?
        get() = Firebase.auth.currentUser

    override fun hasUser(): Boolean = Firebase.auth.currentUser != null

    override fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    override suspend fun signUpUser(email: String, password: String): Flow<Resource<AuthResult>>  {

        return flow {

            emit(Resource.Loading(true))

            try {

                val result =  Firebase.auth
                    .createUserWithEmailAndPassword(email, password)
                    .await()

                emit(Resource.Success(result))

            } catch (e: Exception) {

                emit(Resource.Error("Error signing up"))
                e.printStackTrace()

            } finally {

                emit(Resource.Loading(false))
            }
        }

    }

    override suspend fun logInUser(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO) {

        Firebase.auth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                if (it.isSuccessful) {

                    onComplete.invoke(true)

                } else {

                    onComplete.invoke(false)
                }
            }
            .await()
    }

    override fun logOutUser(): Flow<Resource<Unit>> {

        return flow {

            emit(Resource.Loading(true))

            try {

                Firebase.auth.signOut()

                emit(Resource.Success(Unit))

            } catch (e: Exception) {

                emit(Resource.Error("Error logging out user!"))
                e.printStackTrace()

            } finally {

                emit(Resource.Loading(false))
            }
        }
    }

    override fun deleteUserAccount(): Boolean {

        try {

            Firebase.auth.currentUser?.delete()

            return true

        } catch (e: Exception) {

            e.printStackTrace()
            return false
        }
    }
}