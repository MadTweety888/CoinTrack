package com.example.cointrack.domain.models

import com.example.cointrack.domain.enums.TransactionType
import com.example.cointrack.domain.enums.TransactionType.EXPENSE
import com.example.cointrack.domain.enums.TransactionsSource
import com.example.cointrack.domain.enums.TransactionsSource.UNKNOWN

data class TransactionDraft(
    val type: TransactionType = EXPENSE,
    val amount: Double? = null,
    val category: String = "",
    val source: TransactionsSource = UNKNOWN,
    val note: String = "",
    val description: String = ""
)