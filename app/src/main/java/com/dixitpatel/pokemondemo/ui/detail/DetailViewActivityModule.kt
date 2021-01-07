package com.dixitpatel.pokemondemo.ui.detail

import androidx.lifecycle.ViewModelProvider
import com.dixitpatel.pokemondemo.repository.DetailViewRepository
import com.dixitpatel.pokemondemo.repository.Repository
import com.dixitpatel.pokemondemo.utils.ViewModelProviderFactory
import dagger.Module
import dagger.Provides

/**
 *  Detail Activity Module : DetailActivity bind with ViewModel
 */
@Module
class DetailViewActivityModule {

    @Provides
    fun providesViewModel(detailViewRepository: DetailViewRepository): DetailViewModel {
        return DetailViewModel(detailViewRepository)
    }

    @Provides
    fun provideViewModelProvider(viewModel: DetailViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(viewModel)
    }

    @Provides
    fun providesDetailViewRepository() : Repository {
        return DetailViewRepository()
    }
}