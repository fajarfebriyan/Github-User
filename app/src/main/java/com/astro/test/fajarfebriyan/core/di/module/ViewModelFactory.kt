package com.astro.test.fajarfebriyan.core.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(private val viewModelMap: Map<Class<out ViewModel>,
        @JvmSuppressWildcards Provider<ViewModel>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val map = viewModelMap[modelClass] ?:
        viewModelMap.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key) }?.value ?:
        throw IllegalArgumentException("Unknown ViewModel class $modelClass")

        return try { map.get() as T }
        catch (e: Exception) { throw RuntimeException(e) }
    }
}