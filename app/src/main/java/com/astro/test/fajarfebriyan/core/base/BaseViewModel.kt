package com.astro.test.fajarfebriyan.core.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.astro.test.fajarfebriyan.core.exception.Failure
import com.astro.test.fajarfebriyan.core.util.common.LoadingState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject

open class BaseViewModel @Inject constructor() : ViewModel() {

    internal var io: CoroutineDispatcher = Dispatchers.IO
    internal var main: CoroutineDispatcher = Dispatchers.Main

    internal val loadingState = MutableLiveData<LoadingState>()
    internal var failure: MutableLiveData<Failure> = MutableLiveData()

    protected fun handleFailure(failure: Failure) {
        Timber.d("handleFailure %s", failure)
        this.failure.postValue(failure)
    }
}

class NoViewModel : ViewModel()