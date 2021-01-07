package com.dixitpatel.pokemondemo.dagger.modules

import android.content.Context
import com.dixitpatel.pokemondemo.application.MyApplication
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CommonAppModule {

    @Singleton
    @Provides
    fun provideContext(application: MyApplication): Context {
        return application
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }
}