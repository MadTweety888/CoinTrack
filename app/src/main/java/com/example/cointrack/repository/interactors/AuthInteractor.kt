package com.example.cointrack.repository.interactors

import com.example.cointrack.util.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthInteractor {

    val currentUser: FirebaseUser?

    fun hasUser(): Boolean

    fun getUserId(): String

    suspend fun signUpUser(email: String, password: String): Flow<Resource<AuthResult>>

    suspend fun logInUser(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ): AuthResult

    fun logOutUser(): Flow<Resource<Unit>>

    fun deleteUserAccount(): Boolean
}