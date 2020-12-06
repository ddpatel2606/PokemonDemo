package com.dixitpatel.pokemondemo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlin.random.Random

data class PokemonInfo(
  @SerializedName("id")
  @Expose val id: Int,
  @SerializedName("name")
  @Expose val name: String,
  @SerializedName("height")
  @Expose val height: Int,
  @SerializedName("weight")
  @Expose val weight: Int,
  @SerializedName("base_experience")
  @Expose val experience: Int,
  @SerializedName("types")
  @Expose val types: List<TypeResponse>,
  val hp: Int = Random.nextInt(maxHp),
  val attack: Int = Random.nextInt(maxAttack),
  val defense: Int = Random.nextInt(maxDefense),
  val speed: Int = Random.nextInt(maxSpeed),
  val exp: Int = Random.nextInt(maxExp)
) {

  fun getIdString(): String = String.format("#%03d", id)
  fun getWeightString(): String = String.format("%.1f KG", weight.toFloat() / 10)
  fun getHeightString(): String = String.format("%.1f M", height.toFloat() / 10)
  fun getHpString(): String = "$hp/$maxHp"
  fun getAttackString(): String = "$attack/$maxAttack"
  fun getDefenseString(): String = "$defense/$maxDefense"
  fun getSpeedString(): String = "$speed/$maxSpeed"
  fun getExpString(): String = "$exp/$maxExp"

  data class TypeResponse(
    @SerializedName("slot")
    @Expose val slot: Int,
    @SerializedName("type")
    @Expose val type: Type
  )

  data class Type(
    @SerializedName("name")
    @Expose val name: String
  )

  companion object {
    const val maxHp = 300
    const val maxAttack = 300
    const val maxDefense = 300
    const val maxSpeed = 300
    const val maxExp = 1000
  }
}
