package com.example.cointrack.repository.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponseDTO(

    @SerialName("id")
    val id: String? = null,

    @SerialName("userId")
    val userId: String? = null,

    @SerialName("date")
    val date: String? = null,

    @SerialName("type")
    val type: String? = null,

    @SerialName("amount")
    val amount: Double? = null,

    @SerialName("category")
    val category: String? = null,

    @SerialName("source")
    val source: String? = null,

    @SerialName("note")
    val note: String? = null,

    @SerialName("description")
    val description: String? = null
)