package com.fourdevs.diuquestionbank.utilities

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class PreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences by lazy{
        context.getSharedPreferences(Constants.KEY_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    private val editor = sharedPreferences.edit()

    fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).commit()
    }

    fun putString(key: String, value: String) {
        editor.putString(key, value).commit()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getThemeBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, true)
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun clear() {
        editor.clear().commit()
    }

}