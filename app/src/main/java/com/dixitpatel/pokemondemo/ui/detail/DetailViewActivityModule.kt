package com.dixitpatel.pokemondemo.ui.detail

import androidx.lifecycle.ViewModelProvider
import com.dixitpatel.pokemondemo.utils.ViewModelProviderFactory
import dagger.Module
import dagger.Provides

/**
 *  Detail Activity Module : DetailActivity bind with ViewModel
 */
@Module
class DetailViewActivityModule {

    @Provides
    fun providesViewModel(): DetailViewModel {
        return DetailViewModel()
    }

    @Provides
    fun provideViewModelProvider(viewModel: DetailViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(viewModel)
    }
}