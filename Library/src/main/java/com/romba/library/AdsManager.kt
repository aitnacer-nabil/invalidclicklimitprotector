package com.romba.library


import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.ironsource.mediationsdk.IronSource
import com.romba.library.AdConfig.Companion.ad_max_click
import com.romba.library.AdConfig.Companion.ad_max_retry
import com.romba.library.AdConfig.Companion.TAG
import com.romba.library.AdConfig.Companion.adNetworks
import com.romba.library.AdConfig.Companion.ad_admob_banner_unit_id
import com.romba.library.AdConfig.Companion.ad_click_limit_banner
import com.romba.library.AdConfig.Companion.ad_click_limit_timer
import com.romba.library.AdConfig.Companion.ad_delay_timer
import com.romba.library.AdConfig.Companion.ad_ironSource_app_key
import com.romba.library.AdConfig.Companion.ad_network
import com.romba.library.AdConfig.Companion.ad_unity_game_id
import com.romba.library.AdConfig.Companion.only_one_ad_source
import com.romba.library.AdConfig.Companion.stop_ads_admob
import com.romba.library.adsNetwork.AdBannerListener
import com.romba.library.adsNetwork.AdNetwork
import com.romba.library.adsNetwork.AdNetworkBannerFactory
import com.romba.library.data.AdNetworkType
import com.romba.library.data.SharedPref
import com.unity3d.ads.UnityAds


class AdsManager(
    val activity: AppCompatActivity
) : AdBannerListener {
    private var admob_ad_view: AdView? = null
    private var currentAdIndexBanner: Int = 1
    private var currentAdIndexInterstitialAd: Int = 0;
    private var retryCount: Int = 1;
    private var adNetwork: AdNetwork? = null
    private lateinit var sharedPref: SharedPref

    init {
        adNetworks = arrayOf(
            AdNetworkType.ADMOB,
            AdNetworkType.IRONSOURCE,
            AdNetworkType.UNITY
        )
    }

    fun init() {
        if (only_one_ad_source) {
            adNetworks = emptyArray()
            adNetworks = arrayOf(ad_network)
        }
        sharedPref = SharedPref(activity)
        if (adNetworks.contains(AdNetworkType.ADMOB)) {
            if (stop_ads_admob) return
            Log.d(TAG, "ADMOB init")
            MobileAds.initialize(activity) {}
            MobileAds.setRequestConfiguration(
                RequestConfiguration.Builder()
                    .setTestDeviceIds(listOf(activity.getString(R.string.test_device_id)))
                    .build()
            )

        }

        if (adNetworks.contains(AdNetworkType.IRONSOURCE)) {
            Log.d(TAG, "IRONSOURCE init")
            IronSource.init(activity, ad_ironSource_app_key)
        }
        if (adNetworks.contains(AdNetworkType.UNITY)) {
            Log.d(TAG, "UNITY init")
            UnityAds.initialize(activity, ad_unity_game_id, AdConfig.unity_test_mode)
        }
    }

    fun loadBannerAds(
        enable: Boolean,
        ad_index: Int,
        ad_container: LinearLayout
    ) {
        // Check if banner ads are enabled and if the ad network and unit ID are available
        if (!AdConfig.ad_enable || !enable || ad_admob_banner_unit_id.isEmpty()) return

        // Check if the ad click limit for the banner has reached zero or below

        if (ad_click_limit_banner <= 0) {
            Log.d(TAG, "Click Limit Start delay for 2 Min")
            ad_click_limit_banner = ad_max_click
            currentAdIndexBanner = 0;
            delay(currentAdIndexBanner, ad_container, ad_click_limit_timer)
            return
        }

        // Hide the ad container and remove any existing views
        ad_container.visibility = View.GONE
        ad_container.removeAllViews()
        // Load the banner ad asynchronously
        ad_container.post {
            val adNetworkType = adNetworks.getOrNull(ad_index)
            adNetwork =
                adNetworkType?.let { AdNetworkBannerFactory.createAdNetwork(it, activity) }

            if (adNetwork != null) {

                adNetwork?.loadBannerAd(ad_container, this)
            } else {
                currentAdIndexBanner = 0
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun loadInterstitialAd(
        enable: Boolean,


        ) {
        // Check if banner ads are enabled and if the ad network and unit ID are available
        if (!AdConfig.ad_enable || !enable || ad_admob_banner_unit_id.isEmpty()) return

        val adNetworkType = adNetworks.getOrNull(currentAdIndexInterstitialAd)
        adNetwork =
            adNetworkType?.let { AdNetworkBannerFactory.createAdNetwork(it, activity) }

        if (adNetwork != null) {
            adNetwork?.loadInterstitialAd()
            currentAdIndexInterstitialAd++

        } else {
            currentAdIndexInterstitialAd = 0
        }

    }


    override fun delayAndLoadBanner(adContainer: LinearLayout) {
        currentAdIndexBanner = (currentAdIndexBanner + 1) % adNetworks.size
        delay(currentAdIndexBanner, adContainer, ad_delay_timer)
    }

    override fun handleAdRetry(adContainer: LinearLayout) {
        Log.d(TAG, "Ads Retry $retryCount")
        if (retryCount < ad_max_retry) {
            delay(currentAdIndexBanner, adContainer, ad_delay_timer)
            retryCount += 1
        } else {
            retryCount = 0
            delayAndLoadBanner(adContainer)
        }
    }

    private fun delay(adIndex: Int, adContainer: LinearLayout, timer: Long) {
        Handler(activity.mainLooper).postDelayed({
            adIndex
            loadBannerAds(enable = true, adIndex, adContainer)
        }, timer)
    }

    fun onPause() {
        adNetwork?.onPause()
    }

    fun onResume() {
        adNetwork?.onResume()
    }

    fun onDestroy() {
        adNetwork?.onDestroy()
    }
    fun displayAdInspector(){
        MobileAds.openAdInspector(activity) {
            // Error will be non-null if ad inspector closed due to an error.
        }
    }
}
