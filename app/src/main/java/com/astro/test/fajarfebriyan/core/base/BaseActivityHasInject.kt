package com.astro.test.fajarfebriyan.core.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class BaseActivityHasInject<VDB : ViewDataBinding, VM : ViewModel> :
    BaseActivity<VDB, VM>() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var model: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    /**
     * override setupOnCreate with abstract setupViewModel()
     * setupViewModel only on Activity that has Inject
     * keyword final makes it can't be overridden in child
     */
    final override fun setupOnCreate(savedInstanceState: Bundle?) {
        onPreCreateView(savedInstanceState)
        setupViewModel()
        collectExtras(intent.extras)

        setupLifecycleOwner()
        setupViewUI()

        onPostCreateView(savedInstanceState)
    }

    abstract fun setupViewModel()
}
