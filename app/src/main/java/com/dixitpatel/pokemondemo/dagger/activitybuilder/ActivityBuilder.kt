package com.dixitpatel.pokemondemo.dagger.activitybuilder

import com.dixitpatel.pokemondemo.ui.detail.DetailViewActivity
import com.dixitpatel.pokemondemo.ui.detail.DetailViewActivityModule
import com.dixitpatel.pokemondemo.ui.main.MainActivity
import com.dixitpatel.pokemondemo.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * This is Dagger Activity Builder Which activities will be used in app must be add
 * in Dagger Module.
 *
 */
@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity?

    @ContributesAndroidInjector(modules = [DetailViewActivityModule::class])
    abstract fun contributeDetailActivity(): DetailViewActivity?

}