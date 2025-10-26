package com.example.ibanking_kltn.utils

import android.util.Log
import com.example.ibanking_kltn.data.dtos.responses.ApiResponse
import com.example.ibanking_kltn.data.dtos.responses.ErrorResponse
import com.example.ibanking_soa.data.utils.ApiResult
import com.google.gson.Gson
import retrofit2.Response


suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            val apiResponse = response.body()
            if (apiResponse != null) {
                ApiResult.Success(apiResponse)
            } else {
                ApiResult.Error("Response body is null")
            }
        } else {
            response.errorBody()?.let {
                val errorResponse = Gson().fromJson(it.string(), ErrorResponse::class.java)
                Log.e("err", errorResponse.message)
                ApiResult.Error(errorResponse.message)
            } ?: ApiResult.Error("Unknown error")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ApiResult.Error("Unexpected error: ${e.message}")
    }
}