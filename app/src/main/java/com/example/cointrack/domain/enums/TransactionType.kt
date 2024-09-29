package com.example.cointrack.domain.enums

import com.example.cointrack.domain.enums.TransactionType.EXPENSE
import com.example.cointrack.domain.enums.TransactionType.INCOME

enum class TransactionType {

    INCOME,
    EXPENSE
}

fun TransactionType.toDisplayString(): String {

    return when (this) {

        INCOME  -> "Income"
        EXPENSE -> "Expense"
    }
}