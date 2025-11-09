package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.responses.LoginResponse

data class SettingUiState(
    val settingState: SettingState = SettingState.NONE,
)

sealed class SettingState {
    object SUCCESS : SettingState()
    data class FAILED(val message: String) : SettingState()
    object NONE : SettingState()
    object LOADING : SettingState()
}