package com.example.ibanking_kltn.dtos.requests

import com.example.ibanking_kltn.dtos.definitions.PresignedFileType

data class PresignUploadRequest(
    val contentType: String,
    val fileName: String,
    val fileType: PresignedFileType
)