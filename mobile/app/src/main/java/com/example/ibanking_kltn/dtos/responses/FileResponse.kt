package com.example.ibanking_kltn.dtos.responses

data class FileUploadResponse(
    val downloadUrl: String?,
    val objectKey: String,
    val originalFileContentType: String,
    val originalFileName: String,
    val publicUrl: String,
    val uploadUrl: String
)