package com.dtdennis.currency.data.user

import android.content.SharedPreferences
import com.dtdennis.currency.core.user.UserBaseline
import com.google.gson.Gson
import javax.inject.Inject

private const val PREF_NAME = "com.dtdennis.currency.BASELINE"

class UserBaselineStorage @Inject constructor(private val sharedPrefs: SharedPreferences) {
    private val gson = Gson()

    fun setBaseline(baseline: UserBaseline) {
        sharedPrefs.edit().putString(PREF_NAME, gson.toJson(baseline)).apply()
    }

    fun getBaseline(default: UserBaseline): UserBaseline {
        val baselineJson = sharedPrefs.getString(PREF_NAME, null) ?: return default
        return gson.fromJson(baselineJson, UserBaseline::class.java)
    }
}