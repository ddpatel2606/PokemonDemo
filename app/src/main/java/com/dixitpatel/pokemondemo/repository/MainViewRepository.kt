package com.dixitpatel.pokemondemo.repository

import androidx.lifecycle.MutableLiveData
import com.dixitpatel.pokemondemo.constant.PAGING_SIZE
import com.dixitpatel.pokemondemo.model.PokemonResponse
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
 *  MainViewRepository class to call Main pokemon API.
 */
class MainViewRepository @Inject constructor() : Repository
{
    private val pokemonApiResponse = MutableLiveData<APIRequestResponseHandler<PokemonResponse?>>()

    fun pokemonApiResult(): MutableLiveData<APIRequestResponseHandler<PokemonResponse?>> = pokemonApiResponse

    // Pokemon listing API
    fun pokemonApiCall(page:Int, apiInterface : ApiInterface)
    {
        pokemonApiResponse.value = APIRequestResponseHandler.loading(null)

        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<PokemonResponse> = apiInterface.fetchPokemonList(PAGING_SIZE,page)

            try {
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            pokemonApiResponse.value = APIRequestResponseHandler.success(it)
                        }
                    } else {
                        pokemonApiResponse.value = APIRequestResponseHandler.error(null,response.errorBody().toString())
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