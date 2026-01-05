package com.example.ibanking_kltn.ui.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ibanking_kltn.data.dtos.AccountType
import com.example.ibanking_kltn.data.dtos.SortOption
import com.example.ibanking_kltn.data.dtos.requests.FilterTransactionPara
import com.example.ibanking_kltn.data.dtos.requests.FilterTransactionRequest
import com.example.ibanking_kltn.data.dtos.responses.TransactionHistoryResponse
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_soa.data.utils.ApiResult
import java.time.LocalDate

class TransactionHistoryPagingSource(
    val api: TransactionRepository,
    val filterPara: FilterTransactionPara = FilterTransactionPara(
        fromDate = LocalDate.now().minusMonths(1),
        toDate = LocalDate.now()
    )
) : PagingSource<Int, TransactionHistoryResponse>() {
    override fun getRefreshKey(state: PagingState<Int, TransactionHistoryResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TransactionHistoryResponse> {
        return try {
            val page = params.key ?: 0

            val sortBy = when (filterPara.sortBy) {
                SortOption.NEWEST -> "processed_at_desc"
                SortOption.OLDEST -> "processed_at_asc"
            }
            val  request = FilterTransactionRequest(
                page = page,
                fromDate =filterPara.fromDate.toString(),
                toDate = filterPara.toDate.toString(),
                accountType = filterPara.accountType?.name?: AccountType.WALLET.name,
                status = filterPara.status?.name,
                type = filterPara.type?.name,
                sortBy = sortBy,
            )
            val apiResult = api.getTransactionHistory(
               request
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
