package com.example.ibanking_kltn.ui.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ibanking_kltn.data.dtos.requests.FilterTransactionRequest
import com.example.ibanking_kltn.data.dtos.responses.TransactionHistoryResponse
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_soa.data.utils.ApiResult

class TransactionHistoryPagingSource(
    val api: TransactionRepository,
    val status: String?=null
) : PagingSource<Int, TransactionHistoryResponse>() {
    override fun getRefreshKey(state: PagingState<Int, TransactionHistoryResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TransactionHistoryResponse> {
        return try {
            val page = params.key ?: 1

            val apiResult = api.getTransactionHistory(
                request = FilterTransactionRequest(
                    page = page,
                    status = status
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
