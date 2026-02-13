package com.example.ibanking_kltn.ui.screens.ekyc.register

/**
 * Data class chứa thông tin OCR từ CCCD/CMND
 */
data class OcrData(
    val cardId: String = "",
    val fullName: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val originLocation: String = "",
    val recentLocation: String = "",
    val issueDate: String = "",
    val validDate: String = "",
    val cardType: String = "",
)

data class EkycImages(
    val frontCardPath: String = "",
    val backCardPath: String = "",
    val faceNearPath: String = "",
    val faceFarPath: String = "",
    val hashFront: String = "",
    val hashBack: String = "",
    val hashFaceNear: String = "",
    val hashFaceFar: String = ""
)

data class FullEkycUiState(
    val ocrData: OcrData? = null,
    val ekycImages: EkycImages? = null,
)
