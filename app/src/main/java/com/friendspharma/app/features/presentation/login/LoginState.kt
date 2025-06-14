package com.friendspharma.app.features.presentation.login

data class LoginState(
    val isLoading: Boolean = false,
    val mobile: String = "",
    val message: String = "",
    val isValidate: Boolean = false,
    val valid: Boolean = false,
    val rememberMe: Boolean = false,
    val password: String = "",
    val showPassword: Boolean = false
)
