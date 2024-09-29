package com.example.cointrack.ui.util.components.loadings

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AddTransactionLoadingScreen() {

    DefaultLoadingBackground {

        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}