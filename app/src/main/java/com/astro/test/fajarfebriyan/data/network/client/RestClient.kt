package com.astro.test.fajarfebriyan.data.network.client

import com.astro.test.fajarfebriyan.BuildConfig
import com.astro.test.fajarfebriyan.core.interactor.LocalCase
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RestClient @Inject constructor(
    private val baseClient: OkHttpClient,
    private val retrofitBuilder: Retrofit.Builder,
    private val preferenceLocalCase: LocalCase
) {
    var defaultBaseUrl: String = BuildConfig.BASE_URL

    fun getDefaultRetrofit(): Retrofit {
        val client: OkHttpClient = baseClient.newBuilder()
            .readTimeout(45, TimeUnit.SECONDS)
            .connectTimeout(45, TimeUnit.SECONDS)
            .build()

        return retrofitBuilder.baseUrl(defaultBaseUrl).client(client).build()
    }

    private fun getInterceptorWithHeader(headers: Map<String, String>): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()
            for ((key, value) in headers) {
                builder.addHeader(key, value)
            }
            builder.method(original.method, original.body)
            chain.proceed(builder.build())
        }
    }
}