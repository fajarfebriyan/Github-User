package com.astro.test.fajarfebriyan.core.extension

import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T> Call<T>.await(): T = suspendCancellableCoroutine { cont ->
    cont.invokeOnCancellation { cancel() }

    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>?, t: Throwable) {
            cont.resumeWithException(t)
        }

        override fun onResponse(call: Call<T>?, response: Response<T>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    cont.resume(it)
                }
            } else {
                cont.resumeWithException(HttpException(response))
            }
        }
    })
}