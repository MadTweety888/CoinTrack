package com.example.cointrack.domain.models

import com.example.cointrack.domain.enums.TransactionType
import com.example.cointrack.domain.enums.TransactionType.EXPENSE
import com.example.cointrack.domain.enums.TransactionsSource
import com.example.cointrack.domain.enums.TransactionsSource.UNKNOWN
import java.time.LocalDateTime

data class TransactionDraft(
    val type: TransactionType = EXPENSE,
    val date: LocalDateTime? = LocalDateTime.now(),
    val amount: Double? = null,
    val category: String = "",
    val source: TransactionsSource = UNKNOWN,
    val note: String = "",
    val description: String = ""
)