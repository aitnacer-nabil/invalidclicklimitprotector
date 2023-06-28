package com.romba.library

import com.romba.library.adsNetwork.Admob
import com.romba.library.adsNetwork.Iron
import com.romba.library.adsNetwork.Unity
import com.romba.library.adsNetwork.getAdsOptionFromName
import com.romba.library.data.AdNetworkType

class AdConfig {
    companion object {
        var unity_test_mode = true
        var ad_enable = true
        var only_one_ad_source = true
        var ad_option_name = "Admob"
        const val TAG = "ads Library"
        var ad_max_retry = 2
        var ad_max_click = 3

        var ad_admob_banner_impression = 2
        var stop_ads_admob = false

        var adNetworks: Array<AdNetworkType> = emptyArray()

        // for only one Source
        var ad_network = when(getAdsOptionFromName(ad_option_name)){
            Admob -> AdNetworkType.ADMOB
            Unity -> AdNetworkType.UNITY
            Iron -> AdNetworkType.IRONSOURCE
            else -> AdNetworkType.ADMOB

        }
        var ad_click_limit_banner = ad_max_click
        var retry_every_ad_networks = 2
        //stop showing ads for 2 min
        var ad_click_limit_timer =120000L
        var ad_delay_timer = 10000L

        var ad_admob_banner_unit_id = "ca-app-pub-1292491593775639/7076937833"
        var ad_admob_interstitial_unit_id =  "ca-app-pub-3940256099942544/1033173712"

        var ad_ironSource_app_key = "1a5830d05"
        var ad_ironSource_banner_unit_id = "DefaultBanner"
        var ad_ironSource_interstitial_unit_id = "DefaultInterstitialxx"


        var ad_unity_game_id = "5310491"
        var ad_unity_banner_unit_id = "banner_android"
        var ad_unity_interstitial_unit_id = "wall_interstital_water"
    }
}