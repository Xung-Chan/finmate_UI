package com.example.ibanking_kltn.data.dtos.requests

data class NotificationRequest(
    val fromDate: String,
    val toDate: String,
    val type: String
)