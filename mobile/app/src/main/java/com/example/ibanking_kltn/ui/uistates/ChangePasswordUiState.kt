package com.example.ibanking_kltn.ui.uistates



data class ChangePasswordUiState(
    val screenState: StateType = StateType.NONE,

    val isShowOldPassword: Boolean = false,
    val isShowNewPassword: Boolean = false,
    val oldPassword: String = "",
    val newPassword: String = "",
    val isValidNewPassword: Boolean = false,

    )
