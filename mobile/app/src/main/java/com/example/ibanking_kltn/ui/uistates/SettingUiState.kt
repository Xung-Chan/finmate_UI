package com.example.ibanking_kltn.ui.uistates

data class SettingUiState(
    val screenState: StateType = StateType.NONE,
    val isEnableBiometric: Boolean = false,
    val username: String = "",
    val confirmPassword: String = "",
)
