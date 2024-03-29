package com.dixitpatel.pokemondemo.network

import com.dixitpatel.pokemondemo.model.PokemonInfo
import com.dixitpatel.pokemondemo.model.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *  All Network Calling Apis are define here with Coroutine function.
 */
interface ApiInterface {

    // Fetch Pokemon list API
    @GET("pokemon")
    suspend fun fetchPokemonList(@Query("limit") limit: Int = 20,
                                 @Query("offset") offset: Int = 0): Response<PokemonResponse>

    // Fetch Pokemon Details API
    @GET("pokemon/{name}")
    suspend fun fetchPokemonDetail(@Path("name") name: String): Response<PokemonInfo>

}