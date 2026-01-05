package com.example.ibanking_kltn.data.repositories

import com.example.ibanking_kltn.data.api.AiApi
import com.example.ibanking_kltn.data.dtos.responses.AnalyzeResponse
import com.example.ibanking_soa.data.utils.ApiResult
import jakarta.inject.Inject
import kotlinx.coroutines.delay

class AiRepository @Inject constructor(
    private val aiApi: AiApi
) {
    suspend fun getAnalytic(analyzeRequestId: String): ApiResult<AnalyzeResponse> {
//       return safeApiCall(
//           apiCall = { aiApi.getAnalyze(
//               analyzeRequestId = analyzeRequestId
//           ) }
//       )
        delay(15000L)
        return ApiResult.Success(
            AnalyzeResponse(
                xu_huong = "Chi tiêu của bạn đang tăng dần qua các tháng. Tháng 10 bạn tiêu 3.5 triệu, và dự kiến tháng sau có thể lên khoảng 3.8 triệu.",
                ty_trong = "Ăn uống chiếm 43%, cao hơn 29% so với các tháng gần đây. Di chuyển 23%, tăng 20%. Mua sắm 34%, cũng tăng 20%.",
                canh_bao = "Tháng này chi tiêu ăn uống cao hơn bình thường.', 'Tỷ trọng mua sắm lệch khoảng 20% so với các tháng trước."
            )
        )
    }


}