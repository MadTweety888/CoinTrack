package com.example.cointrack.ui.activities

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cointrack.ui.navigation.Routes.PROFILE_SCREEN
import com.example.cointrack.ui.navigation.Routes.STATISTICS_SCREEN
import com.example.cointrack.ui.navigation.Routes.TRANSACTIONS_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(): ViewModel() {

    val bottomNavBarVisibilityState = (mutableStateOf(false))

    fun setBottomNavBarVisibilityState(currentRoute: String?) = viewModelScope.launch {

        bottomNavBarVisibilityState.value = currentRoute in listOf(
            TRANSACTIONS_SCREEN,
            STATISTICS_SCREEN,
            PROFILE_SCREEN
        )
    }
}