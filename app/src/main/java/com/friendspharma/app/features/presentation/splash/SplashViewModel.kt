package com.friendspharma.app.features.presentation.splash

import androidx.lifecycle.ViewModel
import com.friendspharma.app.features.domain.services.SharedPreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val preferenceHelper: SharedPreferenceHelper) :
    ViewModel() {

    fun isLoggedIn(): Boolean {
        return !preferenceHelper.getUser().MOBILE_NO.isNullOrEmpty()
    }
}