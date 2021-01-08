package com.dixitpatel.pokemondemo.ui.detail

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dixitpatel.pokemondemo.R
import com.dixitpatel.pokemondemo.model.PokemonInfo
import com.dixitpatel.pokemondemo.network.APIRequestResponseHandler
import com.dixitpatel.pokemondemo.network.ApiInterface
import com.dixitpatel.pokemondemo.repository.DetailViewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

/**
 *  Detail Activity ViewModel : ViewModel
 */
class DetailViewModel @Inject constructor(val detailViewRepository: DetailViewRepository) : ViewModel()
{
    companion object {

        // Bind Pokemon name
        @JvmStatic
        @SuppressLint("SetTextI18n")
        @BindingAdapter("pokemon_name")
        fun TextView.setPokemonName(name: String?) {
            this.text =
                if (name.isNullOrEmpty()) "" else "${this.context.getString(R.string.pokemon_name_string)} $name"
        }

        // Bind Pokemon Height
        @JvmStatic
        @SuppressLint("SetTextI18n")
        @BindingAdapter("pokemon_height")
        fun TextView.setPokemonHeight(height: String?) {
            this.text =
                if (height.isNullOrEmpty()) "" else "${this.context.getString(R.string.pokemon_height)} $height"
        }

        // Bind Pokemon Weight
        @JvmStatic
        @SuppressLint("SetTextI18n")
        @BindingAdapter("pokemon_weight")
        fun TextView.setPokemonWeight(weight: String?) {
            this.text =
                if (weight.isNullOrEmpty()) "" else "${this.context.getString(R.string.pokemon_weight)} $weight"
        }
    }

}