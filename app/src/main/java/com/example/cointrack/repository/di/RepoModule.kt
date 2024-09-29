package com.example.cointrack.repository.di

import com.example.cointrack.repository.implementations.AuthRepository
import com.example.cointrack.repository.implementations.CategoriesRepository
import com.example.cointrack.repository.implementations.TransactionsRepository
import com.example.cointrack.repository.implementations.UserDataRepository
import com.example.cointrack.repository.interactors.AuthInteractor
import com.example.cointrack.repository.interactors.CategoriesInteractor
import com.example.cointrack.repository.interactors.TransactionsInteractor
import com.example.cointrack.repository.interactors.UserDataInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class RepoModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthInteractor { return AuthRepository() }

    @Provides
    @Singleton
    fun provideCategoriesRepository(): CategoriesInteractor { return CategoriesRepository() }

    @Provides
    @Singleton
    fun provideTransactionsRepository(): TransactionsInteractor { return TransactionsRepository() }

    @Provides
    @Singleton
    fun provideUserDataRepository(): UserDataInteractor { return UserDataRepository() }
}