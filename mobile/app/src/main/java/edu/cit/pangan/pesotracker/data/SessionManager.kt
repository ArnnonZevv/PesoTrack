package edu.cit.pangan.pesotracker.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("pesotracker_session", Context.MODE_PRIVATE)

    fun saveSession(auth: AuthResponse) {
        prefs.edit()
            .putString("token", auth.token)
            .putLong("userId", auth.userId)
            .putString("username", auth.username)
            .putString("fullname", auth.fullname)
            .putString("role", auth.role)
            .apply()
    }

    fun getToken(): String? = prefs.getString("token", null)
    fun getFullname(): String? = prefs.getString("fullname", null)
    fun getUsername(): String? = prefs.getString("username", null)
    fun getRole(): String? = prefs.getString("role", null)

    fun isLoggedIn(): Boolean = getToken() != null

    fun clear() {
        prefs.edit().clear().apply()
    }
}
