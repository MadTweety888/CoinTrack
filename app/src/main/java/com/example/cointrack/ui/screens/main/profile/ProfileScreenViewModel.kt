package com.example.cointrack.ui.screens.main.profile

import android.net.Uri
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cointrack.domain.models.UserData
import com.example.cointrack.repository.interactors.AuthInteractor
import com.example.cointrack.repository.interactors.UserDataInteractor
import com.example.cointrack.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val authRepository: AuthInteractor,
    private val userDataRepository: UserDataInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    private val userData = mutableStateOf<UserData?>(null)
    val editingUserData = mutableStateOf<UserData?>(null)

    val isSaveButtonVisible = derivedStateOf {

        userData.value != editingUserData.value &&
                !editingUserData.value?.name.isNullOrBlank() &&
                !editingUserData.value?.surname.isNullOrBlank()
    }

    val isLogOutDialogVisible = mutableStateOf(false)
    val isDeleteAccountDialogVisible = mutableStateOf(false)

    val isLoading = mutableStateOf(false)
    val isError = mutableStateOf<String?>(null)

    init {

        fetchUserData()
    }

    private fun fetchUserData() = viewModelScope.launch(/*Dispatchers.IO*/) {

        if (!authRepository.hasUser()) {

            isError.value = "No user found!"
            return@launch
        }

        userDataRepository.getUserData(authRepository.getUserId()).collect { result ->

            when (result) {

                is Resource.Success -> handleFetchUserDataSuccess(result.data)
                is Resource.Error   -> handleFetchUserDataError(result.message)
                is Resource.Loading -> handleFetchUserDataLoading(result.isLoading)
            }
        }
    }

    //region Fetch UserData Helpers

    private fun handleFetchUserDataSuccess(userData: UserData?) {

        this.userData.value = userData
        this.editingUserData.value = userData
    }

    private fun handleFetchUserDataError(error: String?) {

        isError.value = error
    }

    private fun handleFetchUserDataLoading(isLoading: Boolean) {

        this.isLoading.value = isLoading
    }

    //endregion

    //region Photo Section

    fun onPhotoPicked(photoUri: Uri?) {

        photoUri?.let {

            uploadUserPhoto(it)
        }
    }

    private fun uploadUserPhoto(photoUri: Uri) = viewModelScope.launch(Dispatchers.IO) {

        userDataRepository.uploadUserPhoto(authRepository.getUserId(), photoUri).collect { result ->

            when (result) {

                is Resource.Success -> { /* NO ACTION */}
                is Resource.Error   -> handlePhotoUploadError(result.message)
                is Resource.Loading -> handlePhotoUploadLoading(result.isLoading)
            }
        }
    }

    //region Upload Photo Helpers

    private fun handlePhotoUploadError(error: String?) {

        isError.value = error
    }

    private fun handlePhotoUploadLoading(isLoading: Boolean) {

        this.isLoading.value = isLoading
    }

    //endregion

    //endregion

    fun onNameChanged(name: String) {

        editingUserData.value = editingUserData.value?.copy(name = name)
    }

    fun onSurnameChanged(surname: String) {

        editingUserData.value = editingUserData.value?.copy(surname = surname)
    }

    fun onSaveButtonClicked() {

        editingUserData.value?.let { userData ->

            updateUserData(userData)
        }
    }

    private fun updateUserData(userData: UserData) = viewModelScope.launch(Dispatchers.IO) {

        userDataRepository.editUserData(userData).collect { result ->

            when (result) {

                is Resource.Success -> { /* NO ACTION */ }
                is Resource.Error   -> handleUpdateUserDataError(result.message)
                is Resource.Loading -> handleUpdateUserDataLoading(result.isLoading)
            }
        }
    }

    //region Update UserData Helpers

    private fun handleUpdateUserDataError(error: String?) {

        isError.value = error
    }

    private fun handleUpdateUserDataLoading(isLoading: Boolean) {

        this.isLoading.value = isLoading
    }

    //endregion

    //region LogOut

    fun onLogOutButtonClicked() {

        isLogOutDialogVisible.value = true
    }

    fun onLogOutDialogDismissed() {

        isLogOutDialogVisible.value = false
    }

    fun onLogOutDialogConfirmed() {

        isLogOutDialogVisible.value = false
        logOutUser()
    }

    private fun logOutUser() = viewModelScope.launch {

        authRepository.logOutUser().collect { result ->

            when (result) {

                is Resource.Success -> handleLogOutUserSuccess()
                is Resource.Error   -> handleLogOutUserError(result.message)
                is Resource.Loading -> handleLogOutUserLoading(result.isLoading)
            }
        }
    }

    //region LogOut User Helpers

    private fun handleLogOutUserSuccess() {

        navigateToSplashScreen()
    }

    private fun handleLogOutUserError(error: String?) {

        isError.value = error
    }

    private fun handleLogOutUserLoading(isLoading: Boolean) {

        this.isLoading.value = isLoading
    }

    //endregion

    //endregion

    //region Delete Account

    fun onDeleteAccountButtonClicked() {

        isDeleteAccountDialogVisible.value = true
    }

    fun onDeleteAccountDialogDismissed() {

        isDeleteAccountDialogVisible.value = false
    }

    fun onDeleteAccountDialogConfirmed() {

        isDeleteAccountDialogVisible.value = false
        deleteUserAccount()
    }

    private fun deleteUserAccount() = viewModelScope.launch {

        if (!authRepository.hasUser()) return@launch

        isLoading.value = true

        val deletingAccount = async { authRepository.deleteUserAccount() }

        val deletingData = async(Dispatchers.IO) {
            userDataRepository.deleteUserData(authRepository.getUserId())
        }

        val isSuccessful = listOf(deletingAccount, deletingData).awaitAll().all { it }

        if (isSuccessful) {

            navigateToSplashScreen()
            isLoading.value = false

        } else {

            isError.value = "Error deleting user account!"
            isLoading.value = false
        }
    }

    //endregion

    private fun navigateToSplashScreen() = viewModelScope.launch {

        events.emit(Events.NavigateToSplashScreen)
    }

    sealed class Events {

        object NavigateToSplashScreen: Events()
    }
}