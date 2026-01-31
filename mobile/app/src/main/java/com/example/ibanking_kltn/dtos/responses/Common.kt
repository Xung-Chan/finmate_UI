package com.example.ibanking_kltn.dtos.responses



data class ErrorResponse(
    val error: String,
    val message: String,
    val settingCookie: Boolean,
    val status: Int,
//    val timestamp: String
)