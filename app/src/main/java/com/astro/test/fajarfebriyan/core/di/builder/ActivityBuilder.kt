package com.astro.test.fajarfebriyan.core.di.builder

import com.astro.test.fajarfebriyan.feature.SearchActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    internal abstract fun bindSearchActivity(): SearchActivity
}