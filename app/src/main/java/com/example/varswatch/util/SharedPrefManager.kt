package com.example.varswatch.util

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val PREF_NAME = "UserPrefs"
        private const val KEY_USER_EMAIL = "user_email"
    }

    fun checkEmail():Boolean {
        return getEmail()!=null
    }

    fun saveEmail(email: String) {
        sharedPreferences.edit().putString(KEY_USER_EMAIL, email).apply()
    }

    fun getEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }

    fun clearEmail() {
        sharedPreferences.edit().remove(KEY_USER_EMAIL).apply()
    }
}
