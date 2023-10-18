package com.bnuttin.s700pos.datastore

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

const val PREFERENCE_NAME = "S700POSPreferences"
const val PREF_CURRENCY = "PREF_CURRENCY"
const val PREF_SELLER_NAME = "PREF_SELLER_NAME"
const val PREF_BACKEND_URL = "PREF_BACKEND_URL"

class PrefRepository(private val context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()
    private val gson = Gson()

    private fun String.put(long: Long) {
        editor.putLong(this, long)
        editor.commit()
    }

    private fun String.put(int: Int) {
        editor.putInt(this, int)
        editor.commit()
    }

    private fun String.put(string: String) {
        editor.putString(this, string)
        editor.commit()
    }

    private fun String.put(boolean: Boolean) {
        editor.putBoolean(this, boolean)
        editor.commit()
    }

    private fun String.getLong() = pref.getLong(this, 0)

    private fun String.getInt() = pref.getInt(this, 0)

    private fun String.getString() = pref.getString(this, "")!!

    private fun String.getBoolean() = pref.getBoolean(this, false)

    fun setSellerName(name: String) {
        PREF_SELLER_NAME.put(name)
    }

    fun getSellerName() = PREF_SELLER_NAME.getString()

    fun setCurrency(currency: String) {
        PREF_CURRENCY.put(currency)
    }

    fun getCurrency() = PREF_CURRENCY.getString()

    fun setBackendUrl(url: String) {
        PREF_BACKEND_URL.put(url)
    }

    fun getBackendUrl() = PREF_BACKEND_URL.getString()

    fun clearData() {
        editor.clear()
        editor.commit()
    }
}