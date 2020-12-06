package com.dixitpatel.pokemondemo.application

import androidx.appcompat.app.AppCompatDelegate
import com.dixitpatel.pokemondemo.BuildConfig
import com.dixitpatel.pokemondemo.dagger.components.DaggerMainAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

/** MyApplication class that provides support for using dispatching Dagger injectors.  */
class MyApplication : DaggerApplication()
{
    companion object {
        @JvmStatic
        @get:Synchronized
        lateinit var instance: MyApplication

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun applicationInjector(): AndroidInjector<MyApplication?>? {
        return DaggerMainAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}