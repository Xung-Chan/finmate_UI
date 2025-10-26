package com.example.ibanking_kltn.data.dtos.responses


data class ApiResponse<T>(
    val success: Boolean,
    val id: String,
//    val status: Int,
//    val message: String,
    val data: T
)

data class ErrorResponse(
    val message: String,
    val service: String,
    val stack: String,
    val status: Int,
    val success: Boolean,
    val title: String
)

data class LoginResponse(
    val access_token: String,
    val refresh_token: String,
    val id_token: String,
    val expires_in: Int

)