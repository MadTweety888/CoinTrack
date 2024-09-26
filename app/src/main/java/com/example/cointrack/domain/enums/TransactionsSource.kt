package com.example.cointrack.domain.enums

import com.example.cointrack.domain.enums.TransactionsSource.ACCOUNT
import com.example.cointrack.domain.enums.TransactionsSource.CARD
import com.example.cointrack.domain.enums.TransactionsSource.CASH
import com.example.cointrack.domain.enums.TransactionsSource.UNKNOWN

enum class TransactionsSource {

    CARD,
    ACCOUNT,
    CASH,
    UNKNOWN
}

fun TransactionsSource.toDisplayString(): String {

    return when (this) {

        CARD    -> "Card"
        ACCOUNT -> "Account"
        CASH    -> "Cash"
        UNKNOWN -> "Unknown"
    }
}