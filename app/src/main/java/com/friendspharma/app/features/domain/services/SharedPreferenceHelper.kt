package com.friendspharma.app.features.domain.services

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import com.friendspharma.app.features.data.remote.model.UserDto
import com.google.gson.Gson
import javax.inject.Inject

object LocalConstant {
    const val sharedPreferences = "sharedPreferences"
    const val token = "token"
    const val user = "user"
}

class SharedPreferenceHelper @Inject constructor(application: Application) : AndroidViewModel(
    application
) {
    private val preferences: SharedPreferences = application.getSharedPreferences(
        LocalConstant.sharedPreferences,
        MODE_PRIVATE
    )

    fun getToken(): String {
        return preferences.getString(LocalConstant.token, "") ?: ""
    }

    fun saveStringData(key: String, data: String) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(key, data)
        editor.apply()
    }

    fun getStringData(key: String): String {
        return preferences.getString(key, "") ?: ""
    }

    fun saveUser(user: UserDto) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(LocalConstant.user, Gson().toJson(user))
        editor.apply()
    }

    fun getUser(): UserDto {
        return Gson().fromJson(preferences.getString(LocalConstant.user, ""), UserDto::class.java)
            ?: UserDto()
    }

    fun deleteAll() {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.clear()
        editor.apply()
    }
}