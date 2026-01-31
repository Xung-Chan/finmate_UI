package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.dtos.definitions.NotificationType
import com.example.ibanking_kltn.dtos.definitions.Pagination
import com.example.ibanking_kltn.dtos.requests.NotificationRequest
import com.example.ibanking_kltn.dtos.responses.NotificationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationApi {
    @POST("/api/notifications/filter")
    suspend fun filterNotification(
        @Body request: NotificationRequest
    ): Response<Pagination<NotificationResponse>>

    @POST("/api/notifications/read/{type}")
    suspend fun readNotification(
        @Path("type") type: NotificationType,
    ): Response<Unit>



}