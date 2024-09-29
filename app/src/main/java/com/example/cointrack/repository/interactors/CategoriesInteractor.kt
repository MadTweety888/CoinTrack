package com.example.cointrack.repository.interactors

import com.example.cointrack.util.Resource
import kotlinx.coroutines.flow.Flow

interface CategoriesInteractor {

    fun getCategories(userId: String): Flow<Resource<List<String>>>

    fun createCategory(userId: String, category: String): Flow<Resource<Unit>>
}