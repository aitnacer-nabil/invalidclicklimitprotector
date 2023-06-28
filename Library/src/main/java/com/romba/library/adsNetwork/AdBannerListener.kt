package com.romba.library.adsNetwork

import android.widget.LinearLayout

interface AdBannerListener {
    fun delayAndLoadBanner(adContainer: LinearLayout)
    fun handleAdRetry(adContainer: LinearLayout)
}