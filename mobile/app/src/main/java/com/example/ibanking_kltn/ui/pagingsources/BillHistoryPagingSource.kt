package com.example.ibanking_kltn.ui.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ibanking_kltn.dtos.requests.FilterBillParam
import com.example.ibanking_kltn.dtos.requests.FilterBillRequest
import com.example.ibanking_kltn.dtos.responses.BillResponse
import com.example.ibanking_kltn.data.repositories.BillRepository
import com.example.ibanking_soa.data.utils.ApiResult


class BillHistoryPagingSource(
    val api: BillRepository,
    val filterPara: FilterBillParam = FilterBillParam()
) : PagingSource<Int, BillResponse>() {
    override fun getRefreshKey(state: PagingState<Int, BillResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(
        params: PagingSource.LoadParams<Int>
    ): PagingSource.LoadResult<Int, BillResponse> {
        val page = params.key ?: 0

        var request = FilterBillRequest(
            page = page,
            sortBy = filterPara.sortBy
        )

        if (filterPara.status != null) {
            request = request.copy(status = filterPara.status.name)
        }

        return when (val apiResult = api.filterBill(request)) {
            is ApiResult.Error ->
                PagingSource.LoadResult.Error(Exception(apiResult.message))

            is ApiResult.Success -> {
                val response = apiResult.data
                PagingSource.LoadResult.Page(
                    data = response.contents,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (page >= response.totalPages - 1) null else page + 1
                )
            }
        }
    }

}
