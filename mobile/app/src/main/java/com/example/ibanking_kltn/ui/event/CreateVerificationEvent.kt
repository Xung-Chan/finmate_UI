package com.example.ibanking_kltn.ui.event

import android.net.Uri
import com.example.ibanking_kltn.ui.uistates.FileInfo
import com.example.ibanking_kltn.ui.uistates.IdType
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class CreateVerificationEvent {
    object SubmitVerificationRequest : CreateVerificationEvent()
    data class AddFile(val uris: List<Uri>) : CreateVerificationEvent()
    data class DeleteFile(val file: FileInfo) : CreateVerificationEvent()
    data class SelectType(val idType: IdType): CreateVerificationEvent()
    data class ChangeInvoiceDisplayName(val name: String): CreateVerificationEvent()
    data class ChangeBusinessName(val name: String): CreateVerificationEvent()
    data class ChangeBusinessCode(val code: String): CreateVerificationEvent()
    data class ChangeBusinessAddress(val address: String): CreateVerificationEvent()
    data class ChangeContactEmail(val email: String): CreateVerificationEvent()
    data class ChangeContactPhone(val phone: String): CreateVerificationEvent()
    data class ChangeRepresentativeName(val name: String): CreateVerificationEvent()
    data class ChangeRepresentativeIdNumber(val id: String): CreateVerificationEvent()
}

sealed class CreateVerificationEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : CreateVerificationEffect()
    object SubmitSuccess : CreateVerificationEffect()
}