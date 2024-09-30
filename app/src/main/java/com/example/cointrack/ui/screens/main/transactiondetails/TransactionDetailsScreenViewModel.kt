package com.example.cointrack.ui.screens.main.transactiondetails

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cointrack.domain.enums.TransactionType
import com.example.cointrack.domain.enums.TransactionsSource
import com.example.cointrack.domain.enums.TransactionsSource.UNKNOWN
import com.example.cointrack.domain.models.Transaction
import com.example.cointrack.domain.models.TransactionDraft
import com.example.cointrack.repository.interactors.AuthInteractor
import com.example.cointrack.repository.interactors.CategoriesInteractor
import com.example.cointrack.repository.interactors.TransactionsInteractor
import com.example.cointrack.ui.navigation.NavigationArguments.SELECTED_TRANSACTION_ID_NAV_ARGUMENT
import com.example.cointrack.util.Resource
import com.example.cointrack.util.extensions.tryUpdatingDoubleState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TransactionDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthInteractor,
    private val transactionsRepository: TransactionsInteractor,
    private val categoriesRepository: CategoriesInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    private val transactionId = savedStateHandle.get<String>(SELECTED_TRANSACTION_ID_NAV_ARGUMENT)

    private val transactionDetails = mutableStateOf<Transaction?>(null)
    private val editingTransaction = mutableStateOf(TransactionDraft())
    val displayedTransactionDetails = derivedStateOf {

        if (isEditing.value) {

            transactionDetails.value.updateWithDraft(editingTransaction.value)

        } else {

            transactionDetails.value
        }
    }

    val isEditing = mutableStateOf(false)

    val transactionAmountTextInput = mutableStateOf("")

    val categories = mutableStateOf<List<String>>(emptyList())

    val isSelectCategoryDialogVisible = mutableStateOf(false)
    val isAddCategoryDialogVisible = mutableStateOf(false)

    val isSaveButtonVisible = derivedStateOf {

        isEditing.value &&
                editingTransaction.value.isDifferent(from = transactionDetails.value) &&
                editingTransaction.value.note.isNotBlank() &&
                editingTransaction.value.category.isNotBlank() &&
                editingTransaction.value.source != UNKNOWN &&
                editingTransaction.value.amount != null &&
                editingTransaction.value.date != null
    }

    val isLoading = mutableStateOf(false)
    val isLoadingCategories = mutableStateOf(false)
    val isError = mutableStateOf<String?>(null)
    val isErrorLoadingCategories = mutableStateOf<String?>(null)

    init {

        fetchTransactionDetails()
    }

    private fun fetchTransactionDetails() = viewModelScope.launch {

        transactionId?.let { id ->

            transactionsRepository.getTransactionDetails(id).collect { result ->

                when (result) {

                    is Resource.Success -> handleFetchTransactionDetailsSuccess(result.data)
                    is Resource.Error   -> handleFetchTransactionDetailsError(result.message)
                    is Resource.Loading -> handleFetchTransactionDetailsLoading(result.isLoading)
                }
            }
        }
    }

    //region Fetch Transaction Details Helpers

    private fun handleFetchTransactionDetailsSuccess(transactionDetails: Transaction?) {

        this.transactionDetails.value = transactionDetails
        transactionAmountTextInput.value = transactionDetails?.amount?.toString() ?: ""
    }

    private fun handleFetchTransactionDetailsError(error: String?) {

        isError.value = error
    }

    private fun handleFetchTransactionDetailsLoading(isLoading: Boolean) {

        this.isLoading.value = isLoading
    }

    //endregion

    fun onNavigateBackIconClicked() {

        navigateBack()
    }

    fun onEditButtonClicked() {

        switchToEditMode()
    }

    private fun switchToEditMode() {

        editingTransaction.value = transactionDetails.value.toDraft()
        isEditing.value = true
    }

    fun onStopEditingClicked() {

        switchToDetailsMode()
    }

    private fun switchToDetailsMode() {

        transactionAmountTextInput.value = transactionDetails.value?.amount?.toString() ?: ""
        isEditing.value = false
    }

    fun onTransactionTypeChanged(type: TransactionType) {

        editingTransaction.value = editingTransaction.value.copy(type = type)
    }

    fun onTransactionDateChanged(date: LocalDateTime?) {

        editingTransaction.value = editingTransaction.value.copy(date = date)
    }

    fun onTransactionAmountChanged(amount: String) {

        transactionAmountTextInput.value = amount

        amount.tryUpdatingDoubleState { parsedValue ->

            editingTransaction.value = editingTransaction.value.copy(amount = parsedValue)
        }
    }

    //region Categories Logic

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

        editingTransaction.value = editingTransaction.value.copy(category = category)
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

    //endregion

    fun onTransactionSourceChanged(source: TransactionsSource) {

        editingTransaction.value = editingTransaction.value.copy(source = source)
    }

    fun onTransactionNoteChanged(note: String) {

        editingTransaction.value = editingTransaction.value.copy(note = note)
    }

    fun onTransactionDescriptionChanged(description: String) {

        editingTransaction.value = editingTransaction.value.copy(description = description)
    }

    fun onSaveButtonClicked() {

        updateTransaction()
    }

    private fun updateTransaction() = viewModelScope.launch {

        val userId = authRepository.getUserId()

        if (userId.isBlank()) return@launch

        displayedTransactionDetails.value?.let { updatedTransaction ->

            transactionsRepository.updateTransaction(updatedTransaction).collect { result ->

                when (result) {

                    is Resource.Success -> handleUpdateTransactionSuccess()
                    is Resource.Error   -> handleUpdateTransactionError(result.message)
                    is Resource.Loading -> handleUpdateTransactionLoading(result.isLoading)
                }
            }
        }
    }


    //region Create Transaction Helpers

    private fun handleUpdateTransactionSuccess() {

        switchToDetailsMode()
    }

    private fun handleUpdateTransactionError(error: String?) {

        isError.value = error
    }

    private fun handleUpdateTransactionLoading(isLoading: Boolean) {

        this.isLoading.value = isLoading
    }

    //endregion

    private fun navigateBack() = viewModelScope.launch {

        events.emit(Events.NavigateBack)
    }

    sealed class Events {

        object NavigateBack: Events()
    }

    //region Util Functions

    private fun Transaction?.toDraft(): TransactionDraft {

        return this?.let {

            TransactionDraft(
                type        = type,
                date        = date,
                amount      = amount,
                category    = category,
                source      = source,
                note        = note,
                description = description
            )

        } ?: TransactionDraft()
    }

    private fun Transaction?.updateWithDraft(draft: TransactionDraft): Transaction? {

        return this?.copy(
            type        = draft.type,
            date        = draft.date,
            amount      = draft.amount ?: this.amount,
            category    = draft.category,
            source      = draft.source,
            note        = draft.note,
            description = draft.description
        )
    }

    private fun TransactionDraft.isDifferent(from: Transaction?): Boolean {

        return this.source != from?.source ||
               this.amount != from.amount ||
               this.category != from.category ||
               this.note != from.note ||
               this.description != from.description ||
               this.type != from.type ||
               this.date != from.date
    }

    //endregion
}