package com.example.ibanking_kltn.ui.uistates

data class AuthUiState(
    val loginState: StateType = StateType.NONE,
    val fullName: String? = null,
    val username: String="",
    val password: String="",
)

