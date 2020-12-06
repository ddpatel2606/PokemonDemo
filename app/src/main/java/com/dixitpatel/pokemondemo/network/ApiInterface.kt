package com.dixitpatel.pokemondemo.network

import com.dixitpatel.pokemondemo.model.PokemonInfo
import com.dixitpatel.pokemondemo.model.PokemonResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *  All Network Calling Apis are define here with Coroutine function.
 */
interface ApiInterface {

    @GET("pokemon")
    suspend fun fetchPokemonList(@Query("limit") limit: Int = 20,
                                 @Query("offset") offset: Int = 0): Response<PokemonResponse>

    @GET("pokemon/{name}")
    suspend fun fetchPokemonDetail(@Path("name") name: String): Response<PokemonInfo>

}