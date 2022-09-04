package com.astro.test.fajarfebriyan.core.di.component

import android.app.Application
import com.astro.test.fajarfebriyan.AndroidApplication
import com.astro.test.fajarfebriyan.core.di.builder.ActivityBuilder
import com.astro.test.fajarfebriyan.core.di.module.AppModule
import com.astro.test.fajarfebriyan.core.di.module.RepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    RepositoryModule::class,
    ActivityBuilder::class
]) interface AppComponent {
    // Extension from Component Builder
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: AndroidApplication)
}