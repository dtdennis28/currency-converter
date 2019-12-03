package com.dtdennis.currency.ui.util

import android.content.Context

object ResourceIdFetcher {
    fun getDrawableResIdFromResName(context: Context, name: String, defaultValue: Int = 0): Int {
        val resources = context.resources

        return try {
            val id = resources.getIdentifier(
                name, "drawable",
                context.packageName
            )

            return if (id != 0) id else defaultValue
        } catch (error: Throwable) {
            defaultValue
        }
    }
}