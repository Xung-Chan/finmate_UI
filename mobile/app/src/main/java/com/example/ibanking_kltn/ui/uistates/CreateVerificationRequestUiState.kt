package com.example.ibanking_kltn.ui.uistates

import android.net.Uri

data class CreateVerificationRequestUiState(
    val screenState: StateType = StateType.NONE,

    val documents: List<FileInfo> = emptyList(),

    val invoiceDisplayName: String = "",
    val businessName: String = "",
    val businessCode: String = "",
    val businessAddress: String = "",
    val representativeName: String = "",
    val representativeIdType: String = "",
    val representativeIdNumber: String = "",
    val contactEmail: String = "",
    val contactPhone: String = "",

    ) {
    val isConfirmEnabled: Boolean
        get() =
            documents.isNotEmpty()
                    && invoiceDisplayName.isNotBlank()
                    && businessName.isNotBlank()
                    && businessCode.isNotBlank()
                    && businessAddress.isNotBlank()
                    && representativeName.isNotBlank()
                    && representativeIdType.isNotBlank()
                    && representativeIdNumber.isNotBlank()
                    && contactEmail.isNotBlank()
                    && contactPhone.isNotBlank()
}

enum class IdType {
    CCCD,
    PASSPORT
}

data class FileInfo(
    val uri: Uri,
    val fileName: String,
    val extension: String,
    val size: Long
)