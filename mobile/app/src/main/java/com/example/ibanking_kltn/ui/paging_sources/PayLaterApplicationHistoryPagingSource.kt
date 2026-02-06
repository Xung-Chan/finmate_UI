package com.example.ibanking_kltn.ui.paging_sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ibanking_kltn.dtos.requests.FilterPayLaterApplicationPara
import com.example.ibanking_kltn.dtos.requests.FilterPayLaterApplicationRequest
import com.example.ibanking_kltn.dtos.responses.PayLaterApplicationResponse
import com.example.ibanking_kltn.data.repositories.PayLaterRepository
import com.example.ibanking_soa.data.utils.ApiResult
import java.time.LocalDate

class PayLaterApplicationHistoryPagingSource(
    val api: PayLaterRepository,
    val filterPara: FilterPayLaterApplicationPara =FilterPayLaterApplicationPara (
        fromDate = LocalDate.now().minusMonths(1),
        toDate = LocalDate.now()
    )
) : PagingSource<Int, PayLaterApplicationResponse>() {
    override fun getRefreshKey(state: PagingState<Int, PayLaterApplicationResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PayLaterApplicationResponse> {
            val page = params.key ?: 0

            val request = FilterPayLaterApplicationRequest(
                page = page,
                fromDate = filterPara.fromDate.toString(),
                toDate = filterPara.toDate.toString(),
                status = filterPara.status?.name,
                type = filterPara.type?.name,
            )

            val apiResult = api.filterApplication(
                request = request
            )
            return when (apiResult) {
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
    }


}
