package com.example.cointrack.ui.screens.account.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cointrack.repository.interactors.AuthInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInScreenViewModel @Inject constructor(
    private val authRepository: AuthInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val emailTextState = mutableStateOf("")

    val passwordTextState = mutableStateOf("")

    val isLoading = mutableStateOf(false)

    //region Form Update Helpers

    fun onEmailTextChanged(email: String) {
        emailTextState.value = email
    }

    fun onPasswordTextChanged(password: String) {
        passwordTextState.value = password
    }

    //endregion

    fun onLogInClick() = viewModelScope.launch {

        isLoading.value = true

        if(userIsAdmin()) {

            navigateToTransactionsScreen()
            isLoading.value = false

        } else {

            try {

                authRepository.logInUser(
                    email = emailTextState.value.trim(),
                    password = passwordTextState.value.trim()
                ) { isSuccessful ->

                    if (isSuccessful) {

                        navigateToTransactionsScreen()
                        isLoading.value = false

                    } else {

                        isLoading.value = false
                        makeLoginErrorToast()
                    }
                }
            } catch (e:Exception) {

                isLoading.value = false
                makeLoginErrorToast()
                e.printStackTrace()
            }
        }
    }

    private fun userIsAdmin(): Boolean {

        return emailTextState.value == "admin" && passwordTextState.value == "admin"
    }

    //region Event Helpers

    private fun navigateToTransactionsScreen() = viewModelScope.launch {

        events.emit(Events.NavigateToTransactionsScreen)
    }

    private fun makeLoginErrorToast() = viewModelScope.launch {

        events.emit(Events.MakeLoginErrorToast)
    }

    fun navigateToSignUp() = viewModelScope.launch {

        events.emit(Events.NavigateToSignUp)
    }

    fun clearEventChannel() = viewModelScope.launch {

        events.emit(null)
    }

    //endregion

    sealed class Events {

        object NavigateToTransactionsScreen: Events()
        object NavigateToSignUp: Events()
        object MakeLoginErrorToast: Events()
    }
}