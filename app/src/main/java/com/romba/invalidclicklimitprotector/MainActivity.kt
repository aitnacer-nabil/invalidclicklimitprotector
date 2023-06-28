package com.romba.invalidclicklimitprotector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.romba.invalidclicklimitprotector.databinding.ActivityMainBinding
import com.romba.library.AdConfig
import com.romba.library.AdsManager

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adsManager: AdsManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
         adsManager = AdsManager(this)
        adsManager.init()
        adsManager.loadBannerAds(true,0,binding.bannerAds);
        binding.button.setOnClickListener {
           adsManager.displayAdInspector()
        }
        binding.btnInter.setOnClickListener {
            adsManager.loadInterstitialAd(true)
        }

    }

    override fun onPause() {
        adsManager?.onPause()
        Log.d(AdConfig.TAG,"On Pause ")
        super.onPause()
    }

    override fun onResume() {
        adsManager?.onResume()
        Log.d(AdConfig.TAG,"    onResume")
        super.onResume()

    }

    override fun onDestroy() {
        adsManager?.onDestroy()
        Log.d(AdConfig.TAG,"On Destroy ")
        super.onDestroy()

    }
}