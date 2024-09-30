package com.example.cointrack.ui.screens.addtransaction

import com.example.cointrack.MainCoroutineRule
import com.example.cointrack.repository.interactors.AuthInteractor
import com.example.cointrack.repository.interactors.CategoriesInteractor
import com.example.cointrack.repository.interactors.TransactionsInteractor
import com.example.cointrack.ui.screens.main.addtransaction.AddTransactionScreenViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddTransactionScreenViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    lateinit var authRepository: AuthInteractor

    @MockK
    lateinit var transactionsRepository: TransactionsInteractor

    @MockK
    lateinit var categoriesRepository: CategoriesInteractor

    private lateinit var sut: AddTransactionScreenViewModel

    @Before
    fun setUp() {

        MockKAnnotations.init(this, relaxUnitFun = true)

        sut = AddTransactionScreenViewModel(
            authRepository = authRepository,
            transactionsRepository = transactionsRepository,
            categoriesRepository = categoriesRepository
        )
    }

    @Test
    fun `onTransactionNoteChanged, changes the note of transaction`() =
        runTest {

            sut.onTransactionNoteChanged("New note")

            assertEquals(
                "New note",
                sut.transactionDraft.value.note
            )
        }
}