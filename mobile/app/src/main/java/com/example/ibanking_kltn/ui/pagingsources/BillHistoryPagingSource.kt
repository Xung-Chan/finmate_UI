package com.example.ibanking_kltn.ui.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ibanking_kltn.data.dtos.requests.FilterBillParam
import com.example.ibanking_kltn.data.dtos.requests.FilterBillRequest
import com.example.ibanking_kltn.data.dtos.responses.BillResponse
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

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BillResponse> {
        return try {
            val page = params.key ?: 1

            val apiResult = api.filterBill(
                request = FilterBillRequest(
                    page = page,
                    status = filterPara.status,
                    sortBy = filterPara.sortBy
                ),
            )
            when (apiResult) {
                is ApiResult.Error -> {
                    LoadResult.Error(Exception(apiResult.message))
                }

                is ApiResult.Success -> {
                    val response = apiResult.data
                    LoadResult.Page(
                        data = response.contents,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (page >= response.totalPages) null else page + 1
                    )

                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}
