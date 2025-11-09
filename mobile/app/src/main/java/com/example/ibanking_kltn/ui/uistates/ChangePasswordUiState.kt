package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.responses.LoginResponse

data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val errorMessage: String? = null,
    val changePasswordState: ChangePasswordState = ChangePasswordState.NONE,
)

sealed class ChangePasswordState {
    data class SUCCESS(val message: String) : ChangePasswordState()
    data class FAILED(val message: String) : ChangePasswordState()
    object NONE : ChangePasswordState()
    object CHANGING : ChangePasswordState()
}