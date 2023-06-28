package com.romba.library.data

import android.content.Context
import android.content.SharedPreferences

class SharedPref {
    private var ctx: Context? = null
    private var custom_prefence: SharedPreferences? = null

    constructor(context: Context?) {
        this.ctx = context


        custom_prefence = context?.getSharedPreferences("android-ads-sdk", Context.MODE_PRIVATE)
    }


    // Preference for first launch
    fun setIntersCounter(counter: Int) {
        custom_prefence!!.edit().putInt("INTERS_COUNT", counter).apply()
    }

    fun getIntersCounter(): Int {
        return custom_prefence!!.getInt("INTERS_COUNT", 0)
    }

    fun clearIntersCounter() {
        custom_prefence!!.edit().putInt("INTERS_COUNT", 0).apply()
    }
}