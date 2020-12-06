package com.dixitpatel.pokemondemo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PokemonInfo(
  @SerializedName("id")
  @Expose val id: Int = 0,
  @SerializedName("name")
  @Expose val name: String = "",
  @SerializedName("height")
  @Expose val height: Int = 0,
  @SerializedName("weight")
  @Expose val weight: Int = 0,
  @SerializedName("base_experience")
  @Expose val experience: Int = 0,
  @SerializedName("types")
  @Expose val types: List<TypeResponse>,
  @SerializedName("abilities")
  @Expose val abilities: List<Abilities>
) {

  fun getIdString(): String = String.format("#%03d", id)
  fun getWeightString(): String = String.format("%.1f KG", weight.toFloat() / 10)
  fun getHeightString(): String = String.format("%.1f M", height.toFloat() / 10)

  data class TypeResponse(
    @SerializedName("slot")
    @Expose val slot: Int,
    @SerializedName("type")
    @Expose val type: Type
  )

  data class Abilities(
    @SerializedName("ability")
    @Expose val ability: Ability,
  )

  data class Ability(
    @SerializedName("name")
    @Expose val name: String,
    @SerializedName("url")
    @Expose val url: String
  )

  data class Type(
    @SerializedName("name")
    @Expose val name: String
  )
}
