package com.dixitpatel.pokemondemo.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 *  Pokemon object with name and URL.
 */
@Parcelize
data class Pokemon(
  var page: Int = 0,
  @SerializedName("name")
  @Expose val name: String,
  @SerializedName("url")
  @Expose val url: String
) : Parcelable {

  fun getImageUrl(): String {
    val index = url.split("/".toRegex()).dropLast(1).last()
    return "https://pokeres.bastionbot.org/images/pokemon/$index.png"
  }
}
