package com.example.cointrack.domain.models

import com.example.cointrack.domain.enums.TransactionType
import com.example.cointrack.domain.enums.TransactionsSource
import java.time.LocalDateTime

data class Transaction(
    val id: String,
    val userId: String,
    val type: TransactionType,
    val date: LocalDateTime? = null,
    val amount: Double,
    val category: String,
    val source: TransactionsSource,
    val note: String,
    val description: String = ""
)
