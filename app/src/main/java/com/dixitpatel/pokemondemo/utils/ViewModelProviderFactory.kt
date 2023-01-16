package com.dixitpatel.pokemondemo.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 *  ViewModelFactory where all viewModel will be generated.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelProviderFactory<V>(private val viewModel: V) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((viewModel as ViewModel).javaClass)) {
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}