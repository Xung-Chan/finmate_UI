package com.example.ibanking_kltn.ui.screens.auth.change_password

import com.example.ibanking_kltn.ui.uistates.StateType


data class ChangePasswordUiState(
    val screenState: StateType = StateType.NONE,

    val oldPassword: String = "",
    val newPassword: String = "",
    val isValidNewPassword: Boolean = false,

    )
