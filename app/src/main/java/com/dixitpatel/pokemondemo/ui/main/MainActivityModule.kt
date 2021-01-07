package com.dixitpatel.pokemondemo.ui.main

import androidx.lifecycle.ViewModelProvider
import com.dixitpatel.pokemondemo.repository.DetailViewRepository
import com.dixitpatel.pokemondemo.repository.MainViewRepository
import com.dixitpatel.pokemondemo.repository.Repository
import com.dixitpatel.pokemondemo.ui.detail.DetailViewModel
import com.dixitpatel.pokemondemo.utils.ViewModelProviderFactory
import dagger.Module
import dagger.Provides

/**
 *  Main Activity Module : MainActivity bind with ViewModel
 */
@Module
class MainActivityModule {

    @Provides
    fun providesMainViewRepository() : Repository {
        return MainViewRepository()
    }

    @Provides
    fun providesViewModel(mainViewRepository: MainViewRepository): MainActivityViewModel {
        return MainActivityViewModel(mainViewRepository)
    }

    @Provides
    fun provideViewModelProvider(viewModel: MainActivityViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(viewModel)
    }

}