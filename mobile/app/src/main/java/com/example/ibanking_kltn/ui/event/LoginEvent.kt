package com.example.ibanking_kltn.ui.event

import androidx.fragment.app.FragmentActivity
import com.example.ibanking_kltn.dtos.definitions.RequestOtpPurpose
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class LoginEvent {
    data class ChangeUsername(val username: String) : LoginEvent()
    data class ChangePassword(val password: String) : LoginEvent()
    data class RequestOtp(val purpose: RequestOtpPurpose) : LoginEvent()
    data class LoginBiometric(val fragmentActivity: FragmentActivity) : LoginEvent()
    object Login : LoginEvent()
    object DeleteLastestLogin : LoginEvent()
}

sealed class LoginEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : LoginEffect()
    data class RequestOtp(val purpose: RequestOtpPurpose) : LoginEffect()
    object LoginSuccess : LoginEffect()
}