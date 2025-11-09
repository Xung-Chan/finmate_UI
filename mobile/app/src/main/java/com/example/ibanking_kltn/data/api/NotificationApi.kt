package com.example.ibanking_kltn.data.api

import com.example.ibanking_kltn.data.dtos.requests.NotificationRequest
import com.example.ibanking_kltn.data.dtos.responses.NotificationResponse
import com.example.ibanking_kltn.data.dtos.responses.PaginationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

enum class NotificationType {
    SYSTEM,
    PERSONAL,
}
interface NotificationApi {
    @POST("/api/notifications/filter")
    suspend fun filterNotification(
        @Body request: NotificationRequest
    ): Response<PaginationResponse<NotificationResponse>>

    @POST("/api/notifications/read/{type}")
    suspend fun readNotification(
       @Path("type") type: NotificationType,
    ): Response<Unit>



}