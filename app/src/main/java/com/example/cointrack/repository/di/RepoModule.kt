package com.example.cointrack.repository.di

import com.example.cointrack.repository.implementations.AuthRepository
import com.example.cointrack.repository.implementations.TransactionsRepository
import com.example.cointrack.repository.interactors.AuthInteractor
import com.example.cointrack.repository.interactors.TransactionsInteractor
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
    fun provideTransactionsRepository(): TransactionsInteractor { return TransactionsRepository() }
}