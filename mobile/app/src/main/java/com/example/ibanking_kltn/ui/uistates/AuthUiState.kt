package com.example.ibanking_kltn.ui.uistates

data class AuthUiState(
    val loginState: StateType = StateType.NONE,
    val username: String="user1",
    val password: String="Userpassword@1",
    val isPasswordShow: Boolean = false,
)

