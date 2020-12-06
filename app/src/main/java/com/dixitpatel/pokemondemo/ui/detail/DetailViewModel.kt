package com.dixitpatel.pokemondemo.ui.detail

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dixitpatel.pokemondemo.R
import com.dixitpatel.pokemondemo.constant.PAGING_SIZE
import com.dixitpatel.pokemondemo.model.PokemonInfo
import com.dixitpatel.pokemondemo.model.PokemonResponse
import com.dixitpatel.pokemondemo.network.APIRequestResponseHandler
import com.dixitpatel.pokemondemo.network.ApiInterface
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class DetailViewModel @Inject constructor() : ViewModel()
{
    companion object {

        @JvmStatic
        @SuppressLint("SetTextI18n")
        @BindingAdapter("pokemon_name")
        fun TextView.setPokemonName(name: String?) {
            this.text =
                if (name.isNullOrEmpty()) "" else "${this.context.getString(R.string.pokemon_name)} $name"
        }

        @JvmStatic
        @SuppressLint("SetTextI18n")
        @BindingAdapter("pokemon_height")
        fun TextView.setPokemonHeight(height: String?) {
            this.text =
                if (height.isNullOrEmpty()) "" else "${this.context.getString(R.string.pokemon_height)} $height"
        }

        @JvmStatic
        @SuppressLint("SetTextI18n")
        @BindingAdapter("pokemon_weight")
        fun TextView.setPokemonWeight(weight: String?) {
            this.text =
                if (weight.isNullOrEmpty()) "" else "${this.context.getString(R.string.pokemon_weight)} $weight"
        }
    }

    private val pokemonDetailApiResponse = MutableLiveData<APIRequestResponseHandler<PokemonInfo?>>()

    fun pokemonDetailApiResult(): MutableLiveData<APIRequestResponseHandler<PokemonInfo?>> = pokemonDetailApiResponse

    fun pokemonDetailApiCall(name:String, apiInterface : ApiInterface)
    {
        pokemonDetailApiResponse.value = APIRequestResponseHandler.loading(null)

        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<PokemonInfo> = apiInterface.fetchPokemonDetail(name)

            try {
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            pokemonDetailApiResponse.value = APIRequestResponseHandler.success(it)

                            Timber.e(response.body().toString())
                        }
                    } else {
                        pokemonDetailApiResponse.value = APIRequestResponseHandler.error(null,response.errorBody().toString())
                        Timber.e(response.errorBody().toString())
                    }
                }
            } catch (e: HttpException) {
                Timber.e("Exception ${e.message}")
                pokemonDetailApiResponse.value = APIRequestResponseHandler.error(null,e.message.toString())
            } catch (e: Throwable) {
                Timber.e("Exception ${e.message}")
                pokemonDetailApiResponse.value = APIRequestResponseHandler.error(null,e.message.toString())
            }
        }
    }

}