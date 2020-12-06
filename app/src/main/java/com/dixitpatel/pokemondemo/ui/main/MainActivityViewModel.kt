package com.dixitpatel.pokemondemo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dixitpatel.pokemondemo.constant.PAGING_SIZE
import com.dixitpatel.pokemondemo.model.Pokemon
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

class MainActivityViewModel @Inject constructor() : ViewModel()
{

    private val pokemonApiResponse = MutableLiveData<APIRequestResponseHandler<PokemonResponse?>>()

    fun pokemonApiResult(): MutableLiveData<APIRequestResponseHandler<PokemonResponse?>> = pokemonApiResponse

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

                            Timber.e(response.body().toString())
                        }
                    } else {
                        pokemonApiResponse.value = APIRequestResponseHandler.error(null,response.errorBody().toString())
                        Timber.e(response.errorBody().toString())
                    }
                }
            } catch (e: HttpException) {
                Timber.e("Exception ${e.message}")
                ///pokemonApiResponse.value = APIRequestResponseHandler.error(null,e.message.toString())
            } catch (e: Throwable) {
                Timber.e("Exception ${e.message}")
                //pokemonApiResponse.value = APIRequestResponseHandler.error(null,e.message.toString())
            }
        }
    }
}