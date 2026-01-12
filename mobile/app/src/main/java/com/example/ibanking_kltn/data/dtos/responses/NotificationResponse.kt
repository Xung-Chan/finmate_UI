package com.example.ibanking_kltn.data.dtos.responses


data class NotificationResponse(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val isRead: Boolean
)
