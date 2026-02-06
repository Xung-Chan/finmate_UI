package com.example.ibanking_kltn.ui.screens.profile

import android.net.Uri
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class MyProfileEvent {
    data class UploadAvatar(val uri: Uri) : MyProfileEvent()
    object RetryProfileAndVerification : MyProfileEvent()
    object SavedQrSuccess : MyProfileEvent()
    object NavigateVerifyRequest : MyProfileEvent()
}

sealed class MyProfileEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : MyProfileEffect()
    object  NavigateToVerificationRequest : MyProfileEffect()
}