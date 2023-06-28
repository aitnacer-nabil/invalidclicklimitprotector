package com.romba.library.adsNetwork

import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.romba.library.AdConfig
import com.romba.library.AdConfig.Companion.ad_admob_banner_impression
import com.romba.library.AdConfig.Companion.TAG
import com.romba.library.AdConfig.Companion.ad_admob_banner_unit_id
import com.romba.library.AdConfig.Companion.ad_admob_interstitial_unit_id

class AdMobNetwork(private val activity: AppCompatActivity) : AdNetwork {
    private var mInterstitialAd: InterstitialAd? = null
    private var mbanner_impression: Int = 0
    private var adView: AdView? = null
    override fun loadBannerAd(adContainer: LinearLayout, adBannerListener: AdBannerListener) {
        if (AdConfig.stop_ads_admob) {
            adBannerListener.handleAdRetry(adContainer)
            return
        }
        Log.d(TAG, "AdMob banner Choose")
        val adRequest = AdRequest.Builder().build()
        adView = AdView(activity)
        adView?.adUnitId = ad_admob_banner_unit_id
        adContainer.addView(adView)
        adView?.adListener = object : AdListener() {
            override fun onAdLoaded() {
                adContainer.visibility = View.VISIBLE
                Log.d(TAG, "ADMOB banner onAdLoaded")
            }

            override fun onAdClicked() {
                Log.d(TAG, "ADMOB banner AdClicked")
                adContainer.visibility = View.GONE
                AdConfig.ad_click_limit_banner -= 1
                adBannerListener.delayAndLoadBanner(adContainer)
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                adContainer.visibility = View.GONE
                Log.d(TAG, "ADMOB banner onAdFailedToLoad: ${p0.message}")
                adBannerListener.handleAdRetry(adContainer)
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Log.d(TAG, "ADMOB banner AdImpression")
                if (mbanner_impression >= ad_admob_banner_impression) {
                    mbanner_impression = 0
                    adBannerListener.delayAndLoadBanner(adContainer)
                } else {
                    mbanner_impression += 1

                }

            }
        }
        adView?.setAdSize(getAdmobBannerSize())
        adView?.loadAd(adRequest)


    }

    override fun loadInterstitialAd() {
        if (AdConfig.stop_ads_admob) return
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            activity,
            ad_admob_interstitial_unit_id,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, "Admob InterstitialAd Failed ${adError.message}")
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Admob InterstitialAd was loaded.")
                    mInterstitialAd = interstitialAd
                    showInterstitialAd()
                }
            })

    }

    override fun showInterstitialAd() {
        Log.d(TAG, "Admob InterstitialAd start to showing.")
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Admob InterstitialAd was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                Log.d(TAG, "Admob InterstitialAd dismissed fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                Log.e(TAG, "Admob InterstitialAd failed to show fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Admob InterstitialAd recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Admob InterstitialAd showed fullscreen content.")
            }
        }
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(activity)
        } else {
            Log.d("TAG", "The Admob InterstitialAd  ad wasn't ready yet.")
        }
    }

    private fun getAdmobBannerSize(): AdSize {
        val display: Display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }

    override fun onPause() {
       adView?.pause()
    }

    override fun onResume() {
        adView?.resume()
    }

    override fun onDestroy() {
        adView?.destroy()
    }


}
