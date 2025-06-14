package com.friendspharma.app.features.presentation.login

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friendspharma.app.core.util.Async
import com.friendspharma.app.core.util.Common
import com.friendspharma.app.features.NavigationActions
import com.friendspharma.app.features.data.remote.model.UserDto
import com.friendspharma.app.features.domain.services.SharedPreferenceHelper
import com.friendspharma.app.features.domain.use_case.GetTokenUseCase
import com.friendspharma.app.features.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val loginUseCase: LoginUseCase,
    private val preferenceHelper: SharedPreferenceHelper
) :
    ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun mobileChanged(mobile: String) {
        _state.update { it.copy(mobile = mobile) }
        validate()
    }

    fun closeSnackBar() {
        _state.update { it.copy(message = "") }
    }

//    fun rememberMeChanged(remember: Boolean) {
//        _state.update { it.copy(rememberMe = remember) }
//    }

    private fun validate() {
        if (Common.isValidMobile(state.value.mobile) && state.value.password.isNotEmpty() && state.value.password.length > 2 && state.value.password.length < 7) {
            _state.update { it.copy(valid = true) }
        } else {
            _state.update { it.copy(valid = false) }
        }
    }

    fun login(
        mobileFocusRequester: FocusRequester,
        navAction: NavigationActions,
        context: Context,
        passwordFocusRequester: FocusRequester,
    ) {
        _state.update { it.copy(isValidate = true) }
        if (!Common.isValidMobile(state.value.mobile)) {
            mobileFocusRequester.requestFocus()
        } else if (state.value.password.isEmpty() || state.value.password.length < 3 || state.value.password.length > 6) {
            passwordFocusRequester.requestFocus()
        } else {
            loginUseCase.invoke(
                userName = "88" + state.value.mobile,
                password = state.value.password
            ).onEach { result ->
                when (result) {
                    is Async.Success -> {
                        _state.update { it.copy(isLoading = false) }

                        if (!result.data?.data.isNullOrEmpty()) {
                            preferenceHelper.saveUser(result.data?.data?.get(0) ?: UserDto())
                            navAction.navToMain()
                            Toast.makeText(
                                context,
                                result.data?.message ?: "Success",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Invalid User",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    is Async.Error -> _state.update { it.copy(isLoading = false) }
                    is Async.Loading -> _state.update { it.copy(isLoading = true) }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun passwordChanged(pass: String) {
        _state.update { it.copy(password = pass) }
        validate()
    }

    fun showPassword() {
        _state.update { it.copy(showPassword = !state.value.showPassword) }
    }

}