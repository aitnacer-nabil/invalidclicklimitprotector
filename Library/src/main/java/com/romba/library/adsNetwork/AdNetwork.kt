package com.romba.library.adsNetwork

import android.widget.LinearLayout

interface AdNetwork {
    fun loadBannerAd(adContainer: LinearLayout, adBannerListener: AdBannerListener)
    fun loadInterstitialAd()
    fun showInterstitialAd()
    fun onPause()
    fun onResume()
    fun onDestroy()
}