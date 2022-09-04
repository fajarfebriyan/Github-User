package com.astro.test.fajarfebriyan.data.network.client

import com.astro.test.fajarfebriyan.data.preferences.PreferenceHelper
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class ApiHeaderInterceptor @Inject constructor(
    private var preferenceHelper: PreferenceHelper
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .build()

        val response = chain.proceed(request)
        if (response.code == 500) {
            Timber.d("ApiHeaderInterceptor 500: %s", response)
        }
        if (response.code == 404) {
            Timber.d("ApiHeaderInterceptor 404: %s", response)
        }
//        if (response.code == 401 && !preferenceHelper.getAccessToken().isNullOrBlank()) {
//            //val eventBus = EventBus.getDefault()
//            //eventBus.post(EventBusModel("unauthorized"))
//        }
        return response
    }
}