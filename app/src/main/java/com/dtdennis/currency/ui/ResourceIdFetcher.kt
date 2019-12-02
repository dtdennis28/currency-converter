package com.dtdennis.currency.ui

import android.content.Context

object ResourceIdFetcher {
    fun getDrawableResIdFromResName(context: Context, name: String, defaultValue: Int = 0): Int {
        val resources = context.resources

        println("Getting $name")

        return try {
            val id = resources.getIdentifier(
                name, "drawable",
                context.packageName
            )

            println("Int is $id")

            return if (id != 0) id else defaultValue
        } catch (error: Throwable) {
            defaultValue
        }
    }
}