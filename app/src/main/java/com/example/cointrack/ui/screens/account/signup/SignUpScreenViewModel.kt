package com.example.cointrack.ui.screens.account.signup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cointrack.repository.interactors.AuthInteractor
import com.example.cointrack.repository.interactors.UserDataInteractor
import com.example.cointrack.util.Resource
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val authRepository: AuthInteractor,
    private val userDataRepository: UserDataInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val nameTextState = mutableStateOf("")

    val surnameTextState = mutableStateOf("")

    val emailTextState = mutableStateOf("")

    val passwordTextState = mutableStateOf("")

    val repeatPasswordTextState = mutableStateOf("")

    val isLoading = mutableStateOf(false)

    //region Form Update Helpers

    fun onNameTextChanged(name: String) {
        nameTextState.value = name
    }

    fun onSurnameTextChanged(surname: String) {
        surnameTextState.value = surname
    }

    fun onEmailTextChanged(email: String) {
        emailTextState.value = email
    }

    fun onPasswordTextChanged(password: String) {
        passwordTextState.value = password
    }

    fun onRepeatPasswordTextChanged(repeatPassword: String) {
        repeatPasswordTextState.value = repeatPassword
    }

    //endregion

    fun onSignUpClicked() {

        signUpUser()
    }

    private fun signUpUser() = viewModelScope.launch(Dispatchers.IO) {

        authRepository.signUpUser(
            email = emailTextState.value.trim(),
            password = passwordTextState.value.trim()
        ).collect { result ->

            when (result) {

                is Resource.Success -> handleSignUpUserSuccess(result.data)
                is Resource.Error   -> handleSignUpUserError()
                is Resource.Loading -> handleSignUpUserLoading(result.isLoading)
            }
        }
    }

    //region SignUp Helpers

    private fun handleSignUpUserSuccess(authResult: AuthResult?) {

        authResult?.user?.uid?.let {

            createUserData(it)

        } ?: run {

            makeSignUpErrorToast()
        }
    }

    private fun handleSignUpUserError() {

        makeSignUpErrorToast()
    }

    private fun handleSignUpUserLoading(isLoading: Boolean) {

        //SINCE THE CALL IS CHANGED WE DON'T WANT TO TURN OFF LOADING UNTIL THE SECOND CALL IS DONE
        if (isLoading) this.isLoading.value = true
    }

    //endregion

    private fun createUserData(id: String) = viewModelScope.launch {

        userDataRepository.addUserData(
            userId = id,
            name = nameTextState.value,
            surname = surnameTextState.value,
            email = emailTextState.value
        ).collect { result ->

            when (result) {

                is Resource.Success -> handleCreateUserDataSuccess()
                is Resource.Error   -> handleCreateUserDataError()
                is Resource.Loading -> handleCreateUserDataLoading(result.isLoading)
            }
        }
    }

    //region Create UserData Helpers

    private fun handleCreateUserDataSuccess() {

        navigateToTransactionsScreen()
    }

    private fun handleCreateUserDataError() {

        makeSignUpErrorToast()
    }

    private fun handleCreateUserDataLoading(isLoading: Boolean) {

        this.isLoading.value = isLoading
    }

    //endregion

    //region Event Helpers

    private fun makeSignUpErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeSignupErrorToast)
    }

    private fun navigateToTransactionsScreen() = viewModelScope.launch {
        events.emit(Events.NavigateToTransactionsScreen)
    }

    fun navigateToLogIn() = viewModelScope.launch {
        events.emit(Events.NavigateToLogIn)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    //endregion

    sealed class Events {

        object NavigateToTransactionsScreen: Events()
        object NavigateToLogIn: Events()
        object MakeSignupErrorToast: Events()
    }
}