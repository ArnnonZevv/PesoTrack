package edu.cit.pangan.pesotracker.utils

import android.content.Context
import android.content.Intent
import retrofit2.HttpException

class SessionManager(private val context: Context) {

    private val prefs = context.getSharedPreferences("pesotracker_prefs", Context.MODE_PRIVATE)

    fun saveSession(token: String, username: String, fullname: String) {
        prefs.edit()
            .putString("token",    token)
            .putString("username", username)
            .putString("fullname", fullname)
            .apply()
    }

    fun getToken():    String? = prefs.getString("token",    null)
    fun getUsername(): String? = prefs.getString("username", null)
    fun getFullname(): String? = prefs.getString("fullname", null)

    fun clearSession() = prefs.edit().clear().apply()

    fun isLoggedIn() = getToken() != null

    /**
     * Call this from any activity's catch block.
     * If the error is a 401, wipes the session and sends the user
     * back to LoginActivity so they can re-authenticate.
     */
    fun handleUnauthorized(e: Exception): Boolean {
        if (e is HttpException && e.code() == 401) {
            clearSession()
            val intent = Intent(context, edu.cit.pangan.pesotracker.ui.LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
            return true
        }
        return false
    }
}