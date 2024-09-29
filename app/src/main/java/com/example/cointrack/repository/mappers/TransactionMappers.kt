package com.example.cointrack.repository.mappers

import com.example.cointrack.domain.enums.TransactionType
import com.example.cointrack.domain.enums.TransactionsSource
import com.example.cointrack.domain.models.Transaction
import com.example.cointrack.domain.models.TransactionDraft
import com.example.cointrack.repository.dto.CreateTransactionDTO
import com.example.cointrack.repository.dto.TransactionResponseDTO
import com.example.cointrack.repository.dto.UpdateTransactionDTO
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toTransactionResponseDTO(): TransactionResponseDTO {

    return TransactionResponseDTO(
        id = this.id,
        userId = this.getString("userId"),
        type = this.getString("type"),
        amount = this.getDouble("amount"),
        category = this.getString("category"),
        source = this.getString("source"),
        note = this.getString("note"),
        description = this.getString("description")
    )
}

fun List<DocumentSnapshot>.toTransactionResponseDTOs(): List<TransactionResponseDTO> {

    return this.map { it.toTransactionResponseDTO() }
}

fun TransactionResponseDTO?.toTransaction(): Transaction? {

    this?.let {

        return Transaction(
            id = id ?: return null,
            userId = userId ?: return null,
            type = TransactionType.valueOf(type ?: "UNKNOWN"),
            amount = amount ?: return null,
            category = category ?: return null,
            source = TransactionsSource.valueOf(source ?: "UNKNOWN"),
            note = note ?: "",
            description = description ?: ""
        )

    } ?: return null
}

fun List<TransactionResponseDTO>?.toTransactions(): List<Transaction> {

    return this?.mapNotNull { it.toTransaction() } ?: emptyList()
}

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

fun Transaction.toUpdateTransactionDTO(): UpdateTransactionDTO {

    return UpdateTransactionDTO(
        userId      = userId,
        type        = type.name,
        amount      = amount,
        category    = category,
        source      = source.name,
        note        = note,
        description = description
    )
}