package com.nandaadisaputra.storyapp.data.preference

import android.content.Context

class LoginPreference(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    fun put(key: String, value: String){
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String? {
        return preferences.getString(key, null)
    }

    fun put(key: String, value: Boolean){
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun logOut(){
        editor.clear()
        editor.apply()
    }
    companion object{
        const val PREFS_NAME = "login_pref"
        const val USERNAME= "username"
        const val EMAIL= "email"
        const val TOKEN = "token"
        const val PASSWORD= "password"
        const val IS_LOGIN= "login"
    }

}