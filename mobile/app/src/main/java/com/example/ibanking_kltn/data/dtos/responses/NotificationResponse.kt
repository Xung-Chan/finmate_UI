package com.example.ibanking_kltn.data.dtos.responses

import java.time.LocalDateTime
import java.util.UUID


data class NotificationResponse(
    val id: UUID,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val isRead: Boolean
)
