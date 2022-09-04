package com.astro.test.fajarfebriyan.core.di.module

import android.app.Application
import android.content.Context
import com.astro.test.fajarfebriyan.BuildConfig
import com.astro.test.fajarfebriyan.core.interactor.LocalCase
import com.astro.test.fajarfebriyan.data.network.client.ApiHeaderInterceptor
import com.astro.test.fajarfebriyan.data.network.client.RestClient
import com.astro.test.fajarfebriyan.data.preferences.PreferenceHelper
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    internal fun provideAppPrefHelper(): PreferenceHelper.Preference = PreferenceHelper.Preference()

    @Provides
    @Singleton
    internal fun providePrefHelper(preference: PreferenceHelper.Preference): PreferenceHelper = preference

    @Provides
    @Singleton
    internal fun provideHttpClient(
        headerInterceptor: ApiHeaderInterceptor,
        httpInterceptor: HttpLoggingInterceptor,
        context: Context
    ): OkHttpClient {
        val client: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            client.addInterceptor(httpInterceptor)
            //client.addInterceptor(ChuckInterceptor(context))
        }
        return client
            .addInterceptor(headerInterceptor)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    }

    @Provides
    @Singleton
    internal fun provideApiHeaderInterceptor(preferenceHelper: PreferenceHelper): ApiHeaderInterceptor {
        return ApiHeaderInterceptor(preferenceHelper)
    }

    @Provides
    @Singleton
    internal fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
    }

    @Provides
    @Singleton
    internal fun provideRestClient(
        okHttpClient: OkHttpClient,
        retrofitBuilder: Retrofit.Builder,
        preferenceLocalCase: LocalCase
    ): RestClient {
        return RestClient(okHttpClient, retrofitBuilder, preferenceLocalCase)
    }

}