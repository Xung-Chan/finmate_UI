package com.example.ibanking_kltn.ui.uistates

data class SettingUiState(
    val screenState: StateType = StateType.NONE,
    val isEnableBiometric: Boolean = false,
    val fullName: String = "",
    val avatarUrl: Any? = null,
    val confirmPassword: String = "",
)
