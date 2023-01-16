package com.dixitpatel.pokemondemo.ui.detail

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.dixitpatel.pokemondemo.R
import com.dixitpatel.pokemondemo.repository.DetailViewRepository
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
        @BindingAdapter("setPokemonName")
        fun TextView.setPokemonName(name: String?) {
            this.text =
                if (name.isNullOrEmpty()) "" else "${this.context.getString(R.string.pokemon_name_string)} $name"
        }

        // Bind Pokemon Height
        @JvmStatic
        @SuppressLint("SetTextI18n")
        @BindingAdapter("setPokemonHeight")
        fun TextView.setPokemonHeight(height: String?) {
            this.text =
                if (height.isNullOrEmpty()) "" else "${this.context.getString(R.string.pokemon_height)} $height"
        }

        // Bind Pokemon Weight
        @JvmStatic
        @SuppressLint("SetTextI18n")
        @BindingAdapter("setPokemonWeight")
        fun TextView.setPokemonWeight(weight: String?) {
            this.text =
                if (weight.isNullOrEmpty()) "" else "${this.context.getString(R.string.pokemon_weight)} $weight"
        }
    }

}