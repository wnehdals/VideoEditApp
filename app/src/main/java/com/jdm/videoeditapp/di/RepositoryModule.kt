package com.jdm.videoeditapp.di

import com.jdm.videoeditapp.datasource.local.LocalDataSource
import com.jdm.videoeditapp.repository.SourceRepository
import com.jdm.videoeditapp.repository.SourceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideStoreRepository(localDataSource: LocalDataSource): SourceRepository {
        return SourceRepositoryImpl(localDataSource)
    }
}