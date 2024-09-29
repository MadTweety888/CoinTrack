package com.example.cointrack.repository.mappers

import com.example.cointrack.domain.models.TransactionDraft
import com.example.cointrack.repository.dto.CreateTransactionDTO

fun TransactionDraft.toCreateTransactionDTO(userId: String): CreateTransactionDTO {

    return CreateTransactionDTO(
        userId      = userId,
        type        = type.name,
        amount      = amount,
        category    = category,
        source      = source.name,
        note        = note,
        description = description
    )
}