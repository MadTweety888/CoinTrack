package com.example.cointrack.ui.screens.main.addtransaction

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cointrack.domain.enums.TransactionType
import com.example.cointrack.domain.enums.TransactionsSource
import com.example.cointrack.domain.enums.TransactionsSource.UNKNOWN
import com.example.cointrack.domain.models.TransactionDraft
import com.example.cointrack.repository.interactors.AuthInteractor
import com.example.cointrack.repository.interactors.CategoriesInteractor
import com.example.cointrack.repository.interactors.TransactionsInteractor
import com.example.cointrack.util.Resource
import com.example.cointrack.util.extentions.tryUpdatingDoubleState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTransactionScreenViewModel @Inject constructor(
    private val authRepository: AuthInteractor,
    private val transactionsRepository: TransactionsInteractor,
    private val categoriesRepository: CategoriesInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val transactionDraft = mutableStateOf(TransactionDraft())

    val transactionAmountTextInput = mutableStateOf("")

    val categories = mutableStateOf<List<String>>(emptyList())

    val isSelectCategoryDialogVisible = mutableStateOf(false)
    val isAddCategoryDialogVisible = mutableStateOf(false)

    val isSaveButtonVisible = derivedStateOf {

        transactionDraft.value.note.isNotBlank() &&
                transactionDraft.value.category.isNotBlank() &&
                transactionDraft.value.source != UNKNOWN &&
                transactionDraft.value.amount != null
    }

    val isLoading = mutableStateOf(false)
    val isLoadingCategories = mutableStateOf(false)
    val isError = mutableStateOf<String?>(null)
    val isErrorLoadingCategories = mutableStateOf<String?>(null)

    fun onNavigateBackIconClicked() {

        navigateBack()
    }

    fun onTransactionTypeChanged(type: TransactionType) {

        transactionDraft.value = transactionDraft.value.copy(type = type)
    }

    fun onTransactionAmountChanged(amount: String) {

        transactionAmountTextInput.value = amount

        amount.tryUpdatingDoubleState { parsedValue ->

            transactionDraft.value = transactionDraft.value.copy(amount = parsedValue)
        }
    }

    fun onCategorySectionClicked() {

        isSelectCategoryDialogVisible.value = true
        fetchCategories()
    }

    private fun fetchCategories() = viewModelScope.launch {

        categoriesRepository.getCategories(authRepository.getUserId()).collect { result ->

            when (result) {

                is Resource.Success -> handleFetchCategoriesSuccess(result.data)
                is Resource.Error   -> handleFetchCategoriesError(result.message)
                is Resource.Loading -> handleFetchCategoriesLoading(result.isLoading)
            }
        }
    }

    //region Fetch Categories Helpers

    private fun handleFetchCategoriesSuccess(categories: List<String>?) {

        this.categories.value = categories ?: emptyList()
    }

    private fun handleFetchCategoriesError(error: String?) {

        isErrorLoadingCategories.value = error
    }

    private fun handleFetchCategoriesLoading(isLoading: Boolean) {

        isLoadingCategories.value = isLoading
    }

    //endregion

    fun onSelectCategoryDialogDismissed() {

        isSelectCategoryDialogVisible.value = false
    }

    fun onSelectCategoryDialogConfirmed(category: String) {

        transactionDraft.value = transactionDraft.value.copy(category = category)
        isSelectCategoryDialogVisible.value =  false
    }

    fun onAddCategoryClicked() {

        isAddCategoryDialogVisible.value = true
    }

    fun onAddCategoryDialogDismissed() {

        isAddCategoryDialogVisible.value = false
    }

    fun onAddCategoryDialogConfirmed(category: String) {

        isAddCategoryDialogVisible.value = false
        createCategory(category)
    }

    private fun createCategory(category: String) = viewModelScope.launch {

        val userId = authRepository.getUserId()

        if (userId.isBlank()) return@launch

        categoriesRepository.createCategory(userId, category).collect { result ->

            when (result) {

                is Resource.Success -> { /* NO ACTION */ }
                is Resource.Error   -> { isError.value = result.message }
                is Resource.Loading -> { /* NO ACTION */ }
            }
        }
    }

    fun onTransactionSourceChanged(source: TransactionsSource) {

        transactionDraft.value = transactionDraft.value.copy(source = source)
    }

    fun onTransactionNoteChanged(note: String) {

        transactionDraft.value = transactionDraft.value.copy(note = note)
    }

    fun onTransactionDescriptionChanged(description: String) {

        transactionDraft.value = transactionDraft.value.copy(description = description)
    }

    fun onSaveButtonClicked() {

        createTransaction()
    }

    private fun createTransaction() = viewModelScope.launch {

        val userId = authRepository.getUserId()

        if (userId.isBlank()) return@launch

        transactionsRepository.createTransaction(userId, transactionDraft.value).collect { result ->

            when (result) {

                is Resource.Success -> handleCreateTransactionSuccess()
                is Resource.Error   -> handleCreateTransactionError(result.message)
                is Resource.Loading -> handleCreateTransactionLoading(result.isLoading)
            }
        }
    }


    //region Create Transaction Helpers

    private fun handleCreateTransactionSuccess() {

        navigateBack()
    }

    private fun handleCreateTransactionError(error: String?) {

        isError.value = error
    }

    private fun handleCreateTransactionLoading(isLoading: Boolean) {

        this.isLoading.value = isLoading
    }

    //endregion

    private fun navigateBack() = viewModelScope.launch {

        events.emit(Events.NavigateBack)
    }

    sealed class Events {

        object NavigateBack: Events()
    }
}