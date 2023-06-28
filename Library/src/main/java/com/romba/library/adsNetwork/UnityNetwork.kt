package com.romba.library.adsNetwork

import android.app.Activity
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.romba.library.AdConfig
import com.romba.library.AdConfig.Companion.TAG
import com.romba.library.AdConfig.Companion.ad_unity_interstitial_unit_id
import com.unity3d.ads.*
import com.unity3d.ads.UnityAds.*
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize


class UnityNetwork (private val activity: AppCompatActivity) : AdNetwork {
 private lateinit var loadListener: IUnityAdsLoadListener
 private lateinit var showListener: IUnityAdsShowListener
    override fun loadBannerAd(adContainer: LinearLayout, adBannerListener: AdBannerListener) {
        Log.d(AdConfig.TAG, "UNITY banner Choose")
        val bottomBanner = BannerView(activity, AdConfig.ad_unity_banner_unit_id,
            getUnityBannerSize())
        bottomBanner.listener = object  : BannerView.IListener{
            override fun onBannerLoaded(bannerAdView: BannerView?) {
                adContainer.visibility = View.VISIBLE
                Log.d(AdConfig.TAG, "UNITY banner onBannerAdLoaded")
            }

            override fun onBannerClick(bannerAdView: BannerView?) {
                Log.d(AdConfig.TAG, "UNITY banner AdClicked")
                adContainer.visibility = View.GONE
                AdConfig.ad_click_limit_banner -= 1
                adBannerListener.delayAndLoadBanner(adContainer)

            }

            override fun onBannerFailedToLoad(
                bannerAdView: BannerView?,
                errorInfo: BannerErrorInfo?
            ) {
                adContainer.visibility = View.GONE
                Log.d(
                    AdConfig.TAG,
                    "UNITY banner onBannerAdLoadFailed: ${errorInfo?.errorMessage}"
                )
                adBannerListener.handleAdRetry(adContainer)
            }

            override fun onBannerLeftApplication(bannerView: BannerView?) {

            }


        }
        bottomBanner.load()
        adContainer.addView(bottomBanner)
    }

    override fun loadInterstitialAd() {
        UnityAds.load(ad_unity_interstitial_unit_id,object  : IUnityAdsLoadListener{
            override fun onUnityAdsAdLoaded(placementId: String?) {
                showInterstitialAd()
            }

            override fun onUnityAdsFailedToLoad(
                placementId: String?,
                error: UnityAdsLoadError?,
                message: String?
            ) {
                Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
            }

        })

    }

    override fun showInterstitialAd() {

        UnityAds.show(activity, ad_unity_interstitial_unit_id,object : IUnityAdsShowListener{
            override fun onUnityAdsShowFailure(
                placementId: String?,
                error: UnityAdsShowError?,
                message: String?
            ) {
                Log.e("UnityAdsExample", "Unity Ads failed to show ad for " + placementId + " with error: [" + error + "] " + message);
            }

            override fun onUnityAdsShowStart(placementId: String?) {
                Log.v("UnityAdsExample", "onUnityAdsShowStart: " + placementId);

            }

            override fun onUnityAdsShowClick(placementId: String?) {
                Log.v("UnityAdsExample", "onUnityAdsShowStart: " + placementId);
            }

            override fun onUnityAdsShowComplete(
                placementId: String?,
                state: UnityAdsShowCompletionState?
            ) {
                Log.v("UnityAdsExample", "onUnityAdsShowComplete: " + placementId);
            }

        })

    }

    override fun onPause() {

    }

    override fun onResume() {

    }

    override fun onDestroy() {

    }

    private fun getUnityBannerSize(): UnityBannerSize? {
        val display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        return UnityBannerSize(adWidth, 50)
    }


}