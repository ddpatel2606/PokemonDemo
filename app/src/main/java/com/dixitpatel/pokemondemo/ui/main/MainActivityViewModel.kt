package com.dixitpatel.pokemondemo.ui.main

import androidx.lifecycle.ViewModel
import com.dixitpatel.pokemondemo.repository.MainViewRepository
import javax.inject.Inject

/**
 *  Main Activity ViewModel : ViewModel
 */
class MainActivityViewModel @Inject constructor(val mainViewRepository: MainViewRepository) : ViewModel()
{
    var isLoading = false


}