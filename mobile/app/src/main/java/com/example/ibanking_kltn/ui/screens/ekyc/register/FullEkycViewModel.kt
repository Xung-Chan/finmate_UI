package com.example.ibanking_kltn.ui.screens.ekyc.register

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.repositories.EkycRepository
import com.example.ibanking_kltn.dtos.requests.RegisterEkycRequest
import com.example.ibanking_soa.data.utils.ApiResult
import com.vnptit.idg.sdk.utils.KeyResultConstants
import com.vnptit.idg.sdk.utils.SDKEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FullEkycViewModel @Inject constructor(
    private val ekycRepository: EkycRepository,
) : ViewModel() {

//    private val _uiState = MutableStateFlow(FullEkycUiState())
//    val uiState: StateFlow<FullEkycUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<FullEkycEffect>()
    val uiEffect = _uiEffect.asSharedFlow()


    fun onEvent(event: FullEkycEvent) {
        when (event) {
            is FullEkycEvent.HandleEkycResult -> handleEkycResult(event.activityResult)
        }

    }


    private fun handleEkycResult(result: ActivityResult) {
        viewModelScope.launch {
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val data = result.data!!

                if (!parseFaceCompareResult(data)) {
                    _uiEffect.emit(
                        FullEkycEffect.EkycFailed(
                            message = "Khuôn mặt không khớp với ảnh trên CMND/CCCD"
                        )
                    )
                    return@launch
                }

                if (!parseLivenessResult(data)) {
                    _uiEffect.emit(
                        FullEkycEffect.EkycFailed(
                            message = "Khuôn mặt không đạt yêu cầu liveness"
                        )
                    )
                    return@launch

                }

                val lastStep = data.getStringExtra(KeyResultConstants.LAST_STEP)
                val isCompleted = lastStep == SDKEnum.LastStepEnum.Done.value

                if (!isCompleted) {
                    _uiEffect.emit(
                        FullEkycEffect.EkycFailed(
                            message = "eKYC chưa hoàn tất"
                        )
                    )
                    return@launch
                }

                val ocrData = parseOcrData(data)
                val ekycImages = parseImages(data)

                if (ocrData == null) {
                    _uiEffect.emit(
                        FullEkycEffect.EkycFailed(
                            message = "Không thể lấy dữ liệu OCR từ eKYC"
                        )
                    )
                    return@launch
                }

                if (ekycImages == null) {
                    _uiEffect.emit(
                        FullEkycEffect.EkycFailed(
                            message = "Không thể lấy hình ảnh từ eKYC"
                        )
                    )
                    return@launch
                }

                val request = RegisterEkycRequest(
                    cardId = ocrData.cardId,
                    fullName = ocrData.fullName,
                    dateOfBirth = ocrData.dateOfBirth,
                    address = ocrData.recentLocation,
                )

                val result = ekycRepository.registerEkyc(request = request)
                when (result) {
                    is ApiResult.Error -> {
                        _uiEffect.emit(
                            FullEkycEffect.EkycFailed(
                                message = result.message
                            )
                        )
                    }

                    is ApiResult.Success -> {
                        _uiEffect.emit(FullEkycEffect.EkycCompleted)
                    }
                }

            } else {
                _uiEffect.emit(
                    FullEkycEffect.EkycFailed(
                        message = "eKYC thất bại hoặc bị hủy"
                    )
                )
            }
        }
    }


    private fun parseOcrData(data: Intent): OcrData? {
        val ocrResultString = data.getStringExtra(KeyResultConstants.OCR_RESULT) ?: return null

        return try {
            val jsonRoot = JSONObject(ocrResultString)

            if (jsonRoot.has("object")) {
                val objectData = jsonRoot.getJSONObject("object")
                val dayOfBirthString = objectData.optString("birth_day", "")
                val formattedDate = convertDateFormat(dayOfBirthString)

                OcrData(
                    cardId = objectData.optString("id", ""),
                    fullName = objectData.optString("name", ""),
                    dateOfBirth = formattedDate,
                    gender = objectData.optString("gender", ""),
                    originLocation = objectData.optString("origin_location", ""),
                    recentLocation = objectData.optString("recent_location", ""),
                    issueDate = objectData.optString("issue_date", ""),
                    validDate = objectData.optString("valid_date", ""),
                    cardType = objectData.optString("card_type", ""),
                )
            } else null
        } catch (e: Exception) {
            Log.e("EkycViewModel", "Error parsing OCR data", e)
            null
        }
    }

    /**
     * Convert date format from dd/MM/yyyy to yyyy-MM-dd
     * Example: "18/03/2004" -> "2004-03-18"
     */
    private fun convertDateFormat(dateString: String): String {
        return try {
            if (dateString.isEmpty()) return ""

            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val date = inputFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e: Exception) {
            Log.e("EkycViewModel", "Error converting date format: $dateString", e)
            dateString // Return original string if conversion fails
        }
    }

    /**
     * Parse face compare result
     */
    private fun parseFaceCompareResult(data: Intent): Boolean {
        val compareResultString =
            data.getStringExtra(KeyResultConstants.COMPARE_FACE_RESULT) ?: return false
        val jsonRoot = JSONObject(compareResultString)

        if (jsonRoot.has("object")) {
            val objectData = jsonRoot.getJSONObject("object")
            val msg = objectData.optString("msg", "")
            if (msg == "MATCH") {
                return true
            }
        }
        return false
    }

    /**
     * Parse liveness result
     */
    private fun parseLivenessResult(data: Intent): Boolean {
        val livenessResultString =
            data.getStringExtra(KeyResultConstants.LIVENESS_CARD_FRONT_RESULT) ?: return false
        val jsonRoot = JSONObject(livenessResultString)
        if (jsonRoot.has("object")) {
            val objectData = jsonRoot.getJSONObject("object")
            val isLive = objectData.optString("liveness", "")
            if (isLive == "success") {
                return true
            }
        }
        return false
    }

    /**
     * Parse images paths and hashes
     */
    private fun parseImages(data: Intent): EkycImages? {
        if (data.getStringExtra(KeyResultConstants.HASH_IMAGE_FRONT) == null) {
            return null
        }
        return EkycImages(
            frontCardPath = data.getStringExtra(KeyResultConstants.PATH_IMAGE_FRONT_FULL) ?: "",
            backCardPath = data.getStringExtra(KeyResultConstants.PATH_IMAGE_BACK_FULL) ?: "",
            faceNearPath = data.getStringExtra(KeyResultConstants.PATH_IMAGE_FACE_NEAR_FULL) ?: "",
            faceFarPath = data.getStringExtra(KeyResultConstants.PATH_IMAGE_FACE_FAR_FULL) ?: "",
            hashFront = data.getStringExtra(KeyResultConstants.HASH_IMAGE_FRONT) ?: "",
            hashBack = data.getStringExtra(KeyResultConstants.HASH_IMAGE_BACK) ?: "",
            hashFaceNear = data.getStringExtra(KeyResultConstants.HASH_IMAGE_FACE_NEAR) ?: "",
            hashFaceFar = data.getStringExtra(KeyResultConstants.HASH_IMAGE_FACE_FAR) ?: ""
        )
    }

}