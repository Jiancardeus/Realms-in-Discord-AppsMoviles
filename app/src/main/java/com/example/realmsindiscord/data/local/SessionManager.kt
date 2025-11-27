package com.example.realmsindiscord.data.local

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val context: Context
) {
    private val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveCurrentUser(username: String) {
        sharedPreferences.edit().putString("current_username", username).apply()
    }

    fun getCurrentUsername(): String? {
        return sharedPreferences.getString("current_username", null)
    }

    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return getCurrentUsername() != null
    }
}