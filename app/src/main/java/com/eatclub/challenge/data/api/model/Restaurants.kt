package com.eatclub.challenge.data.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Restaurants(
    @SerializedName("restaurants") val restaurants: List<Restaurant>? = null,
)

@Parcelize
data class Restaurant(
    @SerializedName("address1") val address: String? = null,
    @SerializedName("close") val closing: String? = null,
    @SerializedName("cuisines") val cuisines: List<String>? = emptyList(),
    @SerializedName("deals") val deals: List<Deal>? = emptyList(),
    @SerializedName("imageLink") val imageLink: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("objectId") val objectId: String? = null,
    @SerializedName("open") val opening: String? = null,
    @SerializedName("suburb") val suburb: String? = null,
) : Parcelable

@Parcelize
data class Deal(
    @SerializedName("close") val closing: String? = null,
    @SerializedName("dineIn") val dineIn: Boolean? = null,
    @SerializedName("discount") val discount: Int? = null,
    @SerializedName("end") val end: String? = null,
    @SerializedName("lightning") val lightning: Boolean? = null,
    @SerializedName("objectId") val objectId: String? = null,
    @SerializedName("open") val opening: String? = null,
    @SerializedName("qtyLeft") val qtyLeft: Int? = null,
    @SerializedName("start") val start: String? = null,
) : Parcelable
