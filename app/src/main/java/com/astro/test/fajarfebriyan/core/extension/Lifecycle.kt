package com.astro.test.fajarfebriyan.core.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.astro.test.fajarfebriyan.core.exception.Failure
import kotlin.reflect.KFunction1

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: KFunction1<T, Unit>) =
    liveData.observe(this, Observer(body))

fun <L : LiveData<Failure>> LifecycleOwner.failure(
    liveData: L, body: KFunction1<@ParameterName(name = "failure") Failure, Unit>
) = liveData.observe(this, Observer(body))