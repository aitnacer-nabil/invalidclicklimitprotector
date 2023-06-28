package com.romba.library.adsNetwork

import androidx.appcompat.app.AppCompatActivity
import com.romba.library.data.AdNetworkType

class AdNetworkBannerFactory {
    companion object {
        fun createAdNetwork(adNetworkType: AdNetworkType, activity: AppCompatActivity): AdNetwork {
            return when (adNetworkType) {
                AdNetworkType.ADMOB -> AdMobNetwork(activity)
                AdNetworkType.IRONSOURCE -> IronSourceNetwork(activity)
                AdNetworkType.UNITY -> UnityNetwork(activity)
            }

        }
    }
}