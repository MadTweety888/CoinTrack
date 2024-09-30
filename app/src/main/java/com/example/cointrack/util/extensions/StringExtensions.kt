package com.example.cointrack.util.extensions

fun String.tryUpdatingDoubleState(stateUpdateAction: (Double?) -> Unit) {

    val inputAsDouble = this.replace(',', '.').toDoubleOrNull()

    stateUpdateAction(inputAsDouble)
}