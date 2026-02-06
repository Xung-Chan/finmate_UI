package com.example.ibanking_kltn.ui.screens.auth.change_password

import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class ChangePasswordEvent {
    object ConfirmChangePassword : ChangePasswordEvent()
    data class ChangeOldPassword(val oldPassword: String) : ChangePasswordEvent()
    data class ChangeNewPassword(val newPassword: String) : ChangePasswordEvent()
}

sealed class ChangePasswordEffect {
    object ChangePasswordSuccess : ChangePasswordEffect()
    data class ShowSnackBar(val snackBar: SnackBarUiState) : ChangePasswordEffect()
}