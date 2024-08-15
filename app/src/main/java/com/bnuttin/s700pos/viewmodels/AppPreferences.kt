package com.bnuttin.s700pos.viewmodels

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit

object AppPreferences {
    private var sharedPreferences: SharedPreferences? = null

    fun setup(context: Context) {
        sharedPreferences = context.getSharedPreferences("S700POS.sharedprefs", MODE_PRIVATE)
    }

    var storeName : String?
        get() = Key.STORENAME.getString() ?: "Chicago store"
        set(value) = Key.STORENAME.setString(value)

    var sellerName : String?
        get() = Key.SELLERNAME.getString() ?: "Jenny Rosen"
        set(value) = Key.SELLERNAME.setString(value)

    var brandName: String?
        get() = Key.BRANDNAME.getString() ?: "Stripe 360"
        set(value) = Key.BRANDNAME.setString(value)

    var backendUrl: String?
        get() = Key.BACKENDURL.getString() ?: "https://stripe360.stripedemos.com/"
        set(value) = Key.BACKENDURL.setString(value)

    var backendValid: Boolean?
        get() = Key.BACKENDVALID.getBoolean()
        set(value) = Key.BACKENDVALID.setBoolean(value)

    var currency: String?
        get() = Key.CURRENCY.getString() ?: "USD"
        set(value) = Key.CURRENCY.setString(value)

    var orderIdPrefix: String?
        get() = Key.ORDERIDPREFIX.getString() ?: "PRESS"
        set(value) = Key.ORDERIDPREFIX.setString(value)

    var taxPercentage: Float?
        get() = Key.TAXPERCENTAGE.getFloat() ?: 10.0F
        set(value) = Key.TAXPERCENTAGE.setFloat(value)

    private enum class Key {
        STORENAME, SELLERNAME, BRANDNAME, BACKENDURL, BACKENDVALID, CURRENCY, ORDERIDPREFIX, TAXPERCENTAGE;

        fun getBoolean(): Boolean? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getBoolean(name, false) else null
        fun getFloat(): Float? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getFloat(name, 0f) else null
        fun getInt(): Int? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getInt(name, 0) else null
        fun getLong(): Long? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getLong(name, 0) else null
        fun getString(): String? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getString(name, "") else null

        fun setBoolean(value: Boolean?) = value?.let { sharedPreferences!!.edit { putBoolean(name, value) } } ?: remove()
        fun setFloat(value: Float?) = value?.let { sharedPreferences!!.edit { putFloat(name, value) } } ?: remove()
        fun setInt(value: Int?) = value?.let { sharedPreferences!!.edit { putInt(name, value) } } ?: remove()
        fun setLong(value: Long?) = value?.let { sharedPreferences!!.edit { putLong(name, value) } } ?: remove()
        fun setString(value: String?) = value?.let { sharedPreferences!!.edit { putString(name, value) } } ?: remove()

        fun exists(): Boolean = sharedPreferences!!.contains(name)
        fun remove() = sharedPreferences!!.edit { remove(name) }
    }
}