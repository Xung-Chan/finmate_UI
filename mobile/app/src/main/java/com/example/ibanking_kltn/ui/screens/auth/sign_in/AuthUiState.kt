package com.example.ibanking_kltn.ui.screens.auth.sign_in

import com.example.ibanking_kltn.ui.uistates.StateType

data class AuthUiState(
    val loginState: StateType = StateType.NONE,
    val fullName: String? = null,
    val username: String="",
    val password: String="",
)

