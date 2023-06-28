package com.romba.library.adsNetwork

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.LevelPlayBannerListener
import com.romba.library.AdConfig
import com.romba.library.AdConfig.Companion.TAG
import com.romba.library.AdConfig.Companion.ad_ironSource_app_key

class IronSourceNetwork(private val activity: AppCompatActivity) : AdNetwork {
    private var mbanner_impression: Int = 0
    override fun loadBannerAd(adContainer: LinearLayout, adBannerListener: AdBannerListener) {
        Log.d(TAG, "IRONSOURCE banner Choose")
        IronSource.init(activity, ad_ironSource_app_key, IronSource.AD_UNIT.BANNER)
        val bannerSize = ISBannerSize.BANNER
        bannerSize.isAdaptive = true
        val banner = IronSource.createBanner(activity, bannerSize)
        adContainer.addView(banner)
        banner.levelPlayBannerListener = object : LevelPlayBannerListener {
            override fun onAdLoaded(p0: AdInfo?) {
                adContainer.visibility = View.VISIBLE
                if (mbanner_impression >= AdConfig.ad_admob_banner_impression) {
                    mbanner_impression = 0
                    adBannerListener.delayAndLoadBanner(adContainer)
                } else {
                    mbanner_impression += 1

                }
                Log.d(TAG, "IRONSOURCE banner onBannerAdLoaded")
            }

            override fun onAdLoadFailed(p0: IronSourceError?) {
                adContainer.visibility = View.GONE
                Log.d(
                    TAG,
                    "IRONSOURCE banner onBannerAdLoadFailed: ${p0?.errorMessage}"
                )
                adBannerListener.handleAdRetry(adContainer)

            }

            override fun onAdClicked(p0: AdInfo?) {
                Log.d(TAG, "IRONSOURCE banner AdClicked")
                adContainer.visibility = View.GONE
                AdConfig.ad_click_limit_banner -= 1
                adBannerListener.delayAndLoadBanner(adContainer)

            }

            override fun onAdLeftApplication(p0: AdInfo?) {
                TODO("Not yet implemented")
            }

            override fun onAdScreenPresented(p0: AdInfo?) {
                TODO("Not yet implemented")
            }

            override fun onAdScreenDismissed(p0: AdInfo?) {
                TODO("Not yet implemented")
            }


        }
        IronSource.loadBanner(banner, AdConfig.ad_ironSource_banner_unit_id)
    }
    override fun loadInterstitialAd() {
        Log.i(TAG, "IronSource interstitial loadInterstitialAd")
    }
    override fun showInterstitialAd() {
        Log.i(TAG, "IronSource interstitial showInterstitialAd")
    }

    override fun onPause() {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }
}
