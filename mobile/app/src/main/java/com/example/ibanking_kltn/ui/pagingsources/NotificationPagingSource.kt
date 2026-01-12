package com.example.ibanking_kltn.ui.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ibanking_kltn.data.dtos.NotificationType
import com.example.ibanking_kltn.data.dtos.requests.NotificationRequest
import com.example.ibanking_kltn.data.dtos.responses.NotificationResponse
import com.example.ibanking_kltn.data.repositories.NotificationRepository
import com.example.ibanking_soa.data.utils.ApiResult
import java.time.LocalDate

class NotificationPagingSource(
    val api: NotificationRepository,
    val notificationType: NotificationType= NotificationType.SYSTEM
) : PagingSource<Int, NotificationResponse>() {
    override fun getRefreshKey(state: PagingState<Int,NotificationResponse>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationResponse> {
        return try {
            val page = params.key ?: 0


            val request = NotificationRequest(
                fromDate = LocalDate.now().minusMonths(3).toString(),
                toDate = LocalDate.now().toString(),
                type = notificationType.name,
                page = page,
                size = 10
            )

            val apiResult = api.filterNotifications(
               request=request
            )
            when (apiResult) {
                is ApiResult.Error -> {
                    LoadResult.Error(Exception(apiResult.message))
                }

                is ApiResult.Success -> {
                    val response = apiResult.data
                    LoadResult.Page(
                        data = response.contents,
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = if (page >= response.totalPages - 1) null else page + 1
                    )

                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}
