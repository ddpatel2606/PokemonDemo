package com.dixitpatel.pokemondemo.repository

import androidx.lifecycle.MutableLiveData
import com.dixitpatel.pokemondemo.model.PokemonInfo
import com.dixitpatel.pokemondemo.network.APIRequestResponseHandler
import com.dixitpatel.pokemondemo.network.ApiInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

/**
 *  DetailViewRepository class to call detail stuff API.
 */
class DetailViewRepository @Inject constructor() : Repository
{
    private val pokemonDetailApiResponse = MutableLiveData<APIRequestResponseHandler<PokemonInfo?>>()

    fun pokemonDetailApiResult(): MutableLiveData<APIRequestResponseHandler<PokemonInfo?>> = pokemonDetailApiResponse

    // Pokemon Detail Api Call
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
                        }
                    } else {
                        pokemonDetailApiResponse.value = APIRequestResponseHandler.error(null,response.errorBody().toString())
                        Timber.e(response.errorBody().toString())
                    }
                }
            } catch (e: HttpException) {
                Timber.e("Exception ${e.message}")
            } catch (e: Throwable) {
                Timber.e("Exception ${e.message}")
            }
        }
    }

}