package com.astro.test.fajarfebriyan.core.di.module

import com.astro.test.fajarfebriyan.feature.SearchRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    internal fun provideSearchRepository(dataSource: SearchRepository.Network): SearchRepository = dataSource
}