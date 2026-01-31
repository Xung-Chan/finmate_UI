package com.example.ibanking_kltn.ui.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ibanking_kltn.dtos.definitions.SortOption
import com.example.ibanking_kltn.dtos.requests.FilterBillingCyclesRequest
import com.example.ibanking_kltn.dtos.responses.BillingCycleResonse
import com.example.ibanking_kltn.data.repositories.PayLaterRepository
import com.example.ibanking_soa.data.utils.ApiResult

class BillingCyclePagingSource(
    val api: PayLaterRepository,
    val sortOption: SortOption = SortOption.NEWEST
) : PagingSource<Int, BillingCycleResonse>() {
    override fun getRefreshKey(state: PagingState<Int, BillingCycleResonse>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BillingCycleResonse> {
        return try {
            val page = params.key ?: 0

            val sortBy = when (sortOption) {
                SortOption.NEWEST -> "due_date_desc"
                SortOption.OLDEST -> "due_date_asc"
            }

            val request = FilterBillingCyclesRequest(
                sortBy = sortBy
            )

            val apiResult = api.filterBillingCycles(
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
