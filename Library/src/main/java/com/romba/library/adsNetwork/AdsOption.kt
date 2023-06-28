package com.romba.library.adsNetwork

sealed class AdsOption(val name: String)

object Admob : AdsOption("Admob")
object Unity : AdsOption("Unity")
object Iron : AdsOption("Iron")
object None : AdsOption("None")

fun getAdsOptionFromName(name: String): AdsOption = when (name) {
    "Admob" -> Admob
    "Unity" -> Unity
    "Iron" -> Iron
    else -> None

}

