package com.dixitpatel.pokemondemo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dixitpatel.pokemondemo.constant.PAGING_SIZE
import com.dixitpatel.pokemondemo.model.PokemonResponse
import com.dixitpatel.pokemondemo.network.APIRequestResponseHandler
import com.dixitpatel.pokemondemo.network.ApiInterface
import com.dixitpatel.pokemondemo.repository.MainViewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

/**
 *  Main Activity ViewModel : ViewModel
 */
class MainActivityViewModel @Inject constructor(val mainViewRepository: MainViewRepository) : ViewModel()
{
    var isLoading = false


}