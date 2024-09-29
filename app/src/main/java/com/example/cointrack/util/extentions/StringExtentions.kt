package com.example.cointrack.util.extentions

fun String.tryUpdatingDoubleState(stateUpdateAction: (Double?) -> Unit) {

    val inputAsDouble = this.replace(',', '.').toDoubleOrNull()

    stateUpdateAction(inputAsDouble)
}