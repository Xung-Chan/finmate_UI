package com.example.ibanking_kltn.ui.screens.auth

import com.example.ibanking_kltn.data.dtos.responses.LoginResponse

data class AuthUiState(
    val loginState: LoginState = LoginState.NONE,
    val username: String="",
    val password: String="",
    val isPasswordShow: Boolean = false,

)

sealed class LoginState {
    data class SUCCESS(val data: LoginResponse) : LoginState()
    data class FAILED(val message: String) : LoginState()
    object NONE : LoginState()
    object LOGGING : LoginState()
}