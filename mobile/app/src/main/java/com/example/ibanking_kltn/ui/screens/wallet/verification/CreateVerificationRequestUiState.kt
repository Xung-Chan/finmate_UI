package com.example.ibanking_kltn.ui.screens.wallet.verification

import android.net.Uri
import com.example.ibanking_kltn.ui.uistates.StateType

data class CreateVerificationRequestUiState(
    val screenState: StateType = StateType.NONE,

    val documents: List<FileInfo> = emptyList(),

    val invoiceDisplayName: String = "Cty TNHH",
    val businessName: String = "Cty TNHH",
    val businessCode: String = "012345678912",
    val businessAddress: String = "Quận 7",
    val representativeName: String = "Duy Truong",
    val representativeIdType: String = "CCCD",
    val representativeIdNumber: String = "086204006206",
    val contactEmail: String = "nmdtruong18032004@gmail.com",
    val contactPhone: String = "0396371201",

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