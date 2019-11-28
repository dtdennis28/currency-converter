package com.dtdennis.revolut.util

import android.content.SharedPreferences

class TestSharedPrefs : SharedPreferences {
    private val memPrefs = mutableMapOf<String, Any>()
    private val editor = object : SharedPreferences.Editor {
        override fun clear(): SharedPreferences.Editor {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun putLong(key: String?, value: Long): SharedPreferences.Editor {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun putInt(key: String?, value: Int): SharedPreferences.Editor {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun remove(key: String?): SharedPreferences.Editor =
            apply {
                if (memPrefs.containsKey(key!!)) memPrefs.remove(key)
            }

        override fun putBoolean(key: String?, value: Boolean): SharedPreferences.Editor {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun putStringSet(
            key: String?,
            values: MutableSet<String>?
        ): SharedPreferences.Editor {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun commit(): Boolean {
            return true
        }

        override fun putFloat(key: String?, value: Float): SharedPreferences.Editor {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun apply() {

        }

        override fun putString(key: String?, value: String?) =
            apply {
                memPrefs[key!!] = value!!
            }
    }

    override fun contains(key: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInt(key: String?, defValue: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAll(): MutableMap<String, *> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun edit(): SharedPreferences.Editor {
        return editor
    }

    override fun getLong(key: String?, defValue: Long): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getString(key: String?, defValue: String?): String? {
        return memPrefs[key!!] as? String ?: defValue
    }
}