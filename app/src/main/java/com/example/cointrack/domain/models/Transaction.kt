package com.example.cointrack.domain.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.cointrack.domain.enums.TransactionType
import com.example.cointrack.domain.enums.TransactionType.EXPENSE
import com.example.cointrack.domain.enums.TransactionType.INCOME
import com.example.cointrack.domain.enums.TransactionsSource
import com.example.cointrack.domain.enums.TransactionsSource.ACCOUNT
import com.example.cointrack.domain.enums.TransactionsSource.CARD
import com.example.cointrack.domain.enums.TransactionsSource.CASH
import java.time.LocalDate

data class Transaction(
    val id: String,
    val userId: String,
    val type: TransactionType,
    val date: LocalDate,
    val amount: Double,
    val category: String,
    val source: TransactionsSource,
    val note: String,
    val description: String = ""
) {

    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
        fun getExampleTransactions(): List<Transaction> {

            return listOf(
                Transaction(
                    id = "1",
                    userId = "1",
                    date = LocalDate.now(),
                    type = INCOME,
                    amount = 100000.00,
                    category = "Salary",
                    source = ACCOUNT,
                    note = "Monthly Salary"
                ),
                Transaction(
                    id = "2",
                    userId = "1",
                    date = LocalDate.now(),
                    type = EXPENSE,
                    amount = 3000.00,
                    category = "Health",
                    source = ACCOUNT,
                    note = "Gym Subscription"
                ),
                Transaction(
                    id = "3",
                    userId = "1",
                    date = LocalDate.now(),
                    type = EXPENSE,
                    amount = 1000.00,
                    category = "Social Life",
                    source = CASH,
                    note = "Drinks with colleagues"
                ),
                Transaction(
                    id = "4",
                    type = EXPENSE,
                    userId = "1",
                    date = LocalDate.now(),
                    amount = 1000.00,
                    category = "Health",
                    source = CARD,
                    note = "Headache medicine"
                ),
                Transaction(
                    id = "5",
                    userId = "1",
                    date = LocalDate.now(),
                    type = EXPENSE,
                    amount = 3500.00,
                    category = "Apparel",
                    source = CARD,
                    note = "New jeans"
                ),Transaction(
                    id = "6",
                    userId = "1",
                    date = LocalDate.now(),
                    type = EXPENSE,
                    amount = 4000.00,
                    category = "Food",
                    source = CARD,
                    note = "Chicken for the month"
                ),
                Transaction(
                    id = "7",
                    userId = "1",
                    date = LocalDate.now(),
                    type = INCOME,
                    amount = 20000.00,
                    category = "Salary",
                    source = ACCOUNT,
                    note = "Side gig"
                ),
                Transaction(
                    id = "8",
                    userId = "1",
                    date = LocalDate.now(),
                    type = EXPENSE,
                    amount = 5300.00,
                    category = "Social Life",
                    source = CARD,
                    note = "Clubbing with the boys",
                    description = "Unnecessary flexing..."
                ),
                Transaction(
                    id = "9",
                    userId = "1",
                    date = LocalDate.now(),
                    type = EXPENSE,
                    amount = 1200.00,
                    category = "Social Life",
                    source = CASH,
                    note = "Date Night"
                ),
                Transaction(
                    id = "10",
                    userId = "1",
                    date = LocalDate.now(),
                    type = EXPENSE,
                    amount = 500.00,
                    category = "Food",
                    source = CARD,
                    note = "Milk and eggs"
                )
            )
        }
    }
}
