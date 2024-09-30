package com.example.cointrack.repository.dto

import com.google.firebase.Timestamp

data class UpdateTransactionDTO(
    val userId: String? = null,
    val date: Timestamp? = null,
    val type: String? = null,
    val amount: Double? = null,
    val category: String? = null,
    val source: String? = null,
    val note: String? = null,
    val description: String? = null
)
