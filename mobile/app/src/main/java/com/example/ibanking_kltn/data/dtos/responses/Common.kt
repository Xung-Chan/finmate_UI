package com.example.ibanking_kltn.data.dtos.responses



data class ErrorResponse(
    val error: String,
    val message: String,
    val settingCookie: Boolean,
    val status: Int,
//    val timestamp: String
)
data class PaginationResponse<T>(
    val contents: List<T>,
    val totalPages: Long,
    val currentPage: Long,
    val pageSize: Long,
    val totalElements: Long
)