package com.bnuttin.s700pos.datastore

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

const val PREFERENCE_NAME = "MY_APP_PREF"
const val PREF_LOGGED_IN = "PREF_LOGGED_IN"
const val PREF_IS_LANGUAGE_SELECTED = "PREF_IS_LANGUAGE_SELECTED"
const val PREF_CURRENT_SELECTED_LANGUAGE = "PREF_CURRENT_SELECTED_LANGUAGE"
const val PREF_CONTACT_EMAIL = "PREF_CONTACT_EMAIL"
const val PREF_SHARE_MESSAGE = "PREF_SHARE_MESSAGE"
const val PREF_MINIMUM_APP_VERSION = "PREF_MINIMUM_APP_VERSION"

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

    fun setShareMsg(msg: String) {
        PREF_SHARE_MESSAGE.put(msg)
    }

    fun getShareMsg() = PREF_SHARE_MESSAGE.getString()

    fun clearData() {
        editor.clear()
        editor.commit()
    }
}