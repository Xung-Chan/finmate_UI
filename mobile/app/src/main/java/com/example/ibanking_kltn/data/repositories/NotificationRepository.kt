package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.NotificationApi
import com.example.ibanking_kltn.dtos.definitions.NotificationType
import com.example.ibanking_kltn.dtos.definitions.Pagination
import com.example.ibanking_kltn.dtos.requests.NotificationRequest
import com.example.ibanking_kltn.dtos.responses.NotificationResponse
import com.example.ibanking_kltn.data.exception.safeApiCall
import com.example.ibanking_soa.data.utils.ApiResult
import jakarta.inject.Inject

class NotificationRepository @Inject constructor(
    private val notificationApi: NotificationApi
) {
    suspend fun filterNotifications(
        request: NotificationRequest
    ): ApiResult<Pagination<NotificationResponse>> {
        return safeApiCall(
            apiCall = {
                notificationApi.filterNotification(
                    request = request
                )
            }
        )
//        delay(1000L)
//        return ApiResult.Success(
//            data = Pagination(
//                contents = listOf(),
//                totalPages = 10,
//                currentPage = request.page,
//                pageSize = request.size,
//                totalElements = 100,
//            )
//        )
    }

    suspend fun markAsReadNotification(
        type: NotificationType
    ): ApiResult<Unit> {
        return safeApiCall(
            apiCall = {
                notificationApi.readNotification(
                    type = type
                )
            }
        )
//        delay(1000L)
//        return ApiResult.Success(
//            data = Unit
//        )
    }

}