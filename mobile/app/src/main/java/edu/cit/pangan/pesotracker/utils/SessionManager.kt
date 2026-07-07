package edu.cit.pangan.pesotracker.utils

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("pesotracker_prefs", Context.MODE_PRIVATE)

    fun saveSession(token: String, username: String, fullname: String) {
        prefs.edit()
            .putString("token", token)
            .putString("username", username)
            .putString("fullname", fullname)
            .apply()
    }

    fun getToken(): String?    = prefs.getString("token", null)
    fun getUsername(): String? = prefs.getString("username", null)
    fun getFullname(): String? = prefs.getString("fullname", null)

    fun clearSession() = prefs.edit().clear().apply()

    fun isLoggedIn() = getToken() != null
}