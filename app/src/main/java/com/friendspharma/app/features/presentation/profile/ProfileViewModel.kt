package com.friendspharma.app.features.presentation.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.friendspharma.app.features.domain.services.SharedPreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
    private val preferenceHelper: SharedPreferenceHelper
) :
    AndroidViewModel(application) {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        getUser()
    }

    private fun getUser() {
        val user = preferenceHelper.getUser()
        _state.update { it.copy(user = user) }
    }

    fun logOut(navToLogin: () -> Unit) {
        preferenceHelper.deleteAll()
        navToLogin()
    }

}