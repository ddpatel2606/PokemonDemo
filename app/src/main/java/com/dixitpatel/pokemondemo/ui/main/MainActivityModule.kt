package com.dixitpatel.pokemondemo.ui.main

import androidx.lifecycle.ViewModelProvider
import com.dixitpatel.pokemondemo.ui.main.MainActivityViewModel
import com.dixitpatel.pokemondemo.utils.ViewModelProviderFactory
import dagger.Module
import dagger.Provides

/**
 *  Main Activity Module : MainActivity bind with ViewModel
 */
@Module
class MainActivityModule {

    @Provides
    fun providesViewModel(): MainActivityViewModel {
        return MainActivityViewModel()
    }

    @Provides
    fun provideViewModelProvider(viewModel: MainActivityViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(viewModel)
    }
}