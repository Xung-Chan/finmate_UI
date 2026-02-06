package com.example.ibanking_kltn.ui.paging_sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ibanking_kltn.data.repositories.SpendingRepository
import com.example.ibanking_kltn.dtos.responses.SpendingRecordResponse
import com.example.ibanking_soa.data.utils.ApiResult

class SpendingHistoryPagingSource(
    val api: SpendingRepository,
    val monthlySpending: String
) : PagingSource<Int, SpendingRecordResponse>() {
    override fun getRefreshKey(state: PagingState<Int, SpendingRecordResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SpendingRecordResponse> {
        return try {
            val page = params.key ?: 0
            val apiResult = api.getTransactionsByMonthlySpending(
                monthlySpending = monthlySpending,
                page = page,
                size = 10
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
