package com.dtdennis.currency.ui.util

import android.content.Context
import android.preference.PreferenceManager
import com.dtdennis.currency.ui.entities.UserBaseline
import com.google.gson.Gson
import javax.inject.Inject

private const val PREF_NAME = "com.dtdennis.currency.BASELINE"
class UserBaselineStorage @Inject constructor(context: Context) {
    private val gson = Gson()
    private val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun setBaseline(baseline: UserBaseline) {
        sharedPrefs.edit().putString(PREF_NAME, gson.toJson(baseline)).apply()
    }

    fun getBaseline(default: UserBaseline): UserBaseline {
        val baselineJson = sharedPrefs.getString(PREF_NAME, null) ?: return default
        return gson.fromJson(baselineJson, UserBaseline::class.java)
    }
}