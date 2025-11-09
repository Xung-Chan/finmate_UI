package com.example.ibanking_kltn.ui.exception

import android.util.Log
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
        if (response.errorBody() != null) {
            val errorResponse =
                Gson().fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
            Log.e("err", errorResponse.message)
            return ApiResult.Error(errorResponse.message)
        }
//        val code = response.code()
//        val errorDetail = dictionary.fromCode(code)
//        if (errorDetail == null) {
//            return ApiResult.Error("Unexpected error: status: ${response.code()}, message: ${response.message()}")
//        }
//        return ApiResult.Error(errorDetail.message)
        return ApiResult.Error("Unexpected error: ${response.code()} ${response.message()}")
    } catch (e: Exception) {
        e.printStackTrace()
        return ApiResult.Error("Unexpected error: ${e.message}")
    }
}