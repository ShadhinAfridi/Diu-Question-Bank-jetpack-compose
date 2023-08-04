package com.fourdevs.diuquestionbank.repository

import com.fourdevs.diuquestionbank.utilities.PreferenceManager
import javax.inject.Inject

class CommonRepository @Inject constructor(
    private val preferenceManager: PreferenceManager,
) {
    //Shared Preferences
    fun putString(key: String, value: String) {
        preferenceManager.putString(key, value)
    }

    fun getSting(key: String): String {
        return preferenceManager.getString(key)!!
    }

    fun putBoolean(key: String, value: Boolean) {
        preferenceManager.putBoolean(key, value)
    }

    fun getBoolean(key: String): Boolean {
        return preferenceManager.getBoolean(key)
    }

    fun clearPreferences() {
        preferenceManager.clear()
    }


}