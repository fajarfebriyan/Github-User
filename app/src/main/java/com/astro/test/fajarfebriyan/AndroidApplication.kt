package com.astro.test.fajarfebriyan

import android.app.Application
import com.astro.test.fajarfebriyan.core.di.component.AppComponent
import com.astro.test.fajarfebriyan.core.di.component.DaggerAppComponent
import com.orhanobut.hawk.Hawk
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class AndroidApplication : Application() , HasAndroidInjector {
    @Inject
    internal lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
        this.initializeProduction()
        this.initializeDevelopment()
    }

    private fun injectMembers() {
        component = DaggerAppComponent.builder()
            .application(this)
            .build()

        component.inject(this)
    }

    private fun initializeProduction() {
        Hawk.init(this).build()
    }

    private fun initializeDevelopment() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        lateinit var component: AppComponent
    }
}