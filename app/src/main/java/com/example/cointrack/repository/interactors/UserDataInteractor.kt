package com.example.cointrack.repository.interactors

import android.net.Uri
import com.example.cointrack.domain.models.UserData
import com.example.cointrack.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserDataInteractor {

    fun getUserData(userId: String): Flow<Resource<UserData?>>

    fun addUserData(
        userId: String,
        name: String,
        surname: String,
        email: String
    ): Flow<Resource<Unit>>

    fun editUserData(userData: UserData): Flow<Resource<Unit>>

    fun uploadUserPhoto(userId: String, photoUri: Uri): Flow<Resource<Unit>>

    fun deleteUserData(userId: String): Boolean
}