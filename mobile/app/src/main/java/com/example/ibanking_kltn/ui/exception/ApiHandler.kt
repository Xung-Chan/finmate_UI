package com.example.ibanking_kltn.ui.exception

import com.example.ibanking_kltn.data.dtos.responses.ErrorResponse
import com.example.ibanking_soa.data.utils.ApiResult
import com.google.gson.Gson
import retrofit2.Response


suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>,
//    dictionary: ErrorDictionary
): ApiResult<T> {
    try {
        val response = apiCall()
        if (response.isSuccessful) {
            val apiResponse = response.body()
            if (apiResponse != null) {
                return ApiResult.Success(data = apiResponse)

            }
            return ApiResult.Error(message = "Response body is null")
        }
        val errorBody = response.errorBody()?.string()
        if (!errorBody.isNullOrEmpty()) {
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            return ApiResult.Error(errorResponse.message)
        } else {
            return ApiResult.Error("Unknown error occurred")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return ApiResult.Error("Unexpected error: ${e.message}")
    }
}