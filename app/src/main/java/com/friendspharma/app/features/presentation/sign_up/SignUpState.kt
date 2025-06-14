package com.friendspharma.app.features.presentation.sign_up

data class SignUpState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val message: String = "",
    val isValidate: Boolean = false,
    val valid: Boolean = false,
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val rememberMe: Boolean = false,
    val mobile: String = "",
    val email: String = "",
    val address: String = "",
    val post: String = "",
    val district: String = "",
    val userType: String = "",
    val drugno: String = ""
)
