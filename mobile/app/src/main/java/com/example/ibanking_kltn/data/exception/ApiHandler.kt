package com.example.ibanking_kltn.data.exception

import com.example.ibanking_kltn.dtos.responses.ErrorResponse
import com.example.ibanking_soa.data.utils.ApiResult
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.URL


suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>,
): ApiResult<T> {
    try {
        if (!hasInternetConnection()) {
            return ApiResult.Error("Kết nối Internet bị gián đoạn. Vui lòng kiểm tra kết nối và thử lại.")
        }
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

private suspend fun hasInternetConnection(): Boolean {
    return try {
        val url = URL("https://clients3.google.com/generate_204")
        val available = withContext(Dispatchers.IO) {
            val connection = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 5000
                readTimeout = 5000
                requestMethod = "GET"
            }
            connection.connect()
            connection.responseCode == 204

        }
        return available
    } catch (e: Exception) {
        false
    }
}