package com.example.cointrack.domain.enums

import com.example.cointrack.domain.enums.TransactionType.EXPENSE
import com.example.cointrack.domain.enums.TransactionType.INCOME
import com.example.cointrack.domain.enums.TransactionType.UNKNOWN

enum class TransactionType {

    INCOME,
    EXPENSE,
    UNKNOWN
}

fun TransactionType.toDisplayString(): String {

    return when (this) {

        INCOME  -> "Income"
        EXPENSE -> "Expense"
        UNKNOWN -> "Unknown"
    }
}