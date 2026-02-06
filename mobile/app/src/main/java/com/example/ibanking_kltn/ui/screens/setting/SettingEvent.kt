package com.example.ibanking_kltn.ui.event

import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class SettingEvent {
    object SwitchBiometric : SettingEvent()
    object NavigateToChangePasswordScreen : SettingEvent()
    object NavigateToMyProfile : SettingEvent()
    object Logout: SettingEvent()
    data class ChangeConfirmPassword(val confirmPassword: String) : SettingEvent()
}

sealed class SettingEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : SettingEffect()
    object NavigateToChangePasswordScreen : SettingEffect()
    object NavigateToMyProfile : SettingEffect()
    object Logout: SettingEffect()
}