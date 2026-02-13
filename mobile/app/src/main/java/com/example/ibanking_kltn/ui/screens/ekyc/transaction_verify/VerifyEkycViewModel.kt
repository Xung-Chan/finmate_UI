//package com.example.ibanking_kltn.ui.screens.ekyc.transaction_verify
//
//import android.app.Activity
//import android.content.Intent
//import android.util.Log
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.ibanking_kltn.data.repositories.EkycRepository
//import com.example.ibanking_kltn.dtos.definitions.EkycPurpose
//import com.example.ibanking_kltn.dtos.definitions.NavKey
//import com.example.ibanking_kltn.ui.screens.ekyc.register.EkycEffect
//import com.example.ibanking_kltn.ui.screens.ekyc.register.EkycEvent
//import com.example.ibanking_kltn.ui.screens.ekyc.register.EkycImages
//import com.example.ibanking_kltn.ui.screens.ekyc.register.EkycStep
//import com.example.ibanking_kltn.ui.screens.ekyc.register.FullEkycUiState
//import com.example.ibanking_kltn.ui.screens.ekyc.register.FaceCompareResult
//import com.example.ibanking_kltn.ui.screens.ekyc.register.LivenessResult
//import com.example.ibanking_kltn.ui.screens.ekyc.register.OcrData
//import com.vnptit.idg.sdk.utils.KeyResultConstants
//import com.vnptit.idg.sdk.utils.SDKEnum
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asSharedFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import org.json.JSONObject
//import javax.inject.Inject
//
//@HiltViewModel
//class VerifyEkycViewModel @Inject constructor(
//    private val ekycRepository: EkycRepository,
//    savedStateHandle: SavedStateHandle
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(FullEkycUiState())
//    val uiState: StateFlow<FullEkycUiState> = _uiState.asStateFlow()
//
//    private val _uiEffect = MutableSharedFlow<EkycEffect>()
//    val uiEffect = _uiEffect.asSharedFlow()
//
//    init {
//        val purpose = savedStateHandle.get<EkycPurpose>(NavKey.EKYC_PURPOSE.name)
//        if (purpose == null) {
//            _uiState.update {
//                it.copy(
//                    errorMessage = "Mục đích eKYC không xác định",
//                    ekycStep = EkycStep.Failed
//                )
//            }
//        } else {
//            when (purpose) {
//                EkycPurpose.REGISTER -> startFullEkyc()
//                EkycPurpose.TRANSACTION -> {
//
//                }
//            }
//        }
//    }
//
//    fun onEvent(intent: EkycEvent) {
//        when (intent) {
//            is EkycEvent.StartFullEkyc -> startFullEkyc()
//            is EkycEvent.StartFaceCapture -> startFaceCapture()
//            is EkycEvent.CancelEkyc -> cancelEkyc()
//            is EkycEvent.RetryEkyc -> retryEkyc()
//            is EkycEvent.ConfirmOcrData -> confirmOcrData()
//            is EkycEvent.ConfirmFaceMatch -> confirmFaceMatch()
//            is EkycEvent.DismissError -> dismissError()
//            is EkycEvent.DismissSuccess -> dismissSuccess()
//        }
//    }
//
//    private fun startFullEkyc() {
//        _uiState.update {
//            it.copy(
//                isLoading = true,
//                loadingMessage = "Đang khởi động eKYC...",
//                ekycStep = EkycStep.Initial,
//                errorMessage = null
//            )
//        }
//    }
//
//    private fun startFaceCapture() {
//        _uiState.update {
//            it.copy(
//                isLoading = true,
//                loadingMessage = "Đang khởi động camera...",
//                ekycStep = EkycStep.FaceCapture,
//                errorMessage = null
//            )
//        }
//    }
//
//    private fun cancelEkyc() {
//        _uiState.update {
//            it.copy(
//                isLoading = false,
//                ekycStep = EkycStep.Initial,
//                errorMessage = "Đã hủy eKYC"
//            )
//        }
//    }
//
//    private fun retryEkyc() {
//        _uiState.update {
//            FullEkycUiState() // Reset về trạng thái ban đầu
//        }
//    }
//
//    private fun confirmOcrData() {
//        viewModelScope.launch {
//            _uiState.update {
//                it.copy(
//                    isLoading = true,
//                    loadingMessage = "Đang xác nhận dữ liệu...",
//                    isOcrDataValid = true
//                )
//            }
//            // TODO: Call API để confirm OCR data
//        }
//    }
//
//    private fun confirmFaceMatch() {
//        viewModelScope.launch {
//            _uiState.update {
//                it.copy(
//                    isLoading = true,
//                    loadingMessage = "Đang xác nhận khuôn mặt...",
//                    isFaceMatched = true
//                )
//            }
//            // TODO: Call API để confirm face match
//        }
//    }
//
//    private fun dismissError() {
//        _uiState.update {
//            it.copy(errorMessage = null)
//        }
//    }
//
//    private fun dismissSuccess() {
//        _uiState.update {
//            it.copy(successMessage = null)
//        }
//    }
//
//    /**
//     * Xử lý kết quả từ VNPT eKYC SDK
//     */
//    fun handleEkycResult(resultCode: Int, data: Intent?) {
//        if (data == null) {
//            _uiState.update {
//                it.copy(
//                    isLoading = false,
//                    errorMessage = "Không nhận được dữ liệu từ eKYC",
//                    ekycStep = EkycStep.Failed
//                )
//            }
//            return
//        }
//
//        when (resultCode) {
//            Activity.RESULT_OK -> handleSuccessResult(data)
//            Activity.RESULT_CANCELED -> handleCancelResult(data)
//            else -> handleErrorResult(resultCode)
//        }
//    }
//
//    private fun handleSuccessResult(data: Intent) {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true, loadingMessage = "Đang xử lý kết quả...") }
//
//            try {
//                // Parse OCR data
//                val ocrData = parseOcrData(data)
//
//                // Parse face compare result
//                val faceCompareResult = parseFaceCompareResult(data)
//
//                // Parse liveness results
//                val livenessCardFront = parseLivenessResult(
//                    data.getStringExtra(KeyResultConstants.LIVENESS_CARD_FRONT_RESULT)
//                )
//                val livenessCardBack = parseLivenessResult(
//                    data.getStringExtra(KeyResultConstants.LIVENESS_CARD_BACK_RESULT)
//                )
//                val livenessFace = parseLivenessResult(
//                    data.getStringExtra(KeyResultConstants.LIVENESS_FACE_RESULT)
//                )
//
//                // Parse images
//                val ekycImages = parseImages(data)
//
//                // Check last step
//                val lastStep = data.getStringExtra(KeyResultConstants.LAST_STEP)
//                val isCompleted = lastStep == SDKEnum.LastStepEnum.Done.value
//
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        ekycStep = if (isCompleted) EkycStep.Completed else EkycStep.ReviewData,
//                        isEkycCompleted = isCompleted,
//                        ocrData = ocrData,
//                        faceCompareResult = faceCompareResult,
//                        isFaceMatched = faceCompareResult?.isMatched ?: false,
//                        livenessCardFront = livenessCardFront,
//                        livenessCardBack = livenessCardBack,
//                        livenessFace = livenessFace,
//                        ekycImages = ekycImages,
//                        lastStep = lastStep,
//                        maskedFaceDetected = data.getStringExtra(KeyResultConstants.MASKED_FACE_RESULT) == "true",
//                        successMessage = if (isCompleted) "eKYC hoàn tất thành công!" else null,
//                        errorMessage = if (!isCompleted) "Chưa hoàn tất eKYC" else null
//                    )
//                }
//
//                logEkycResult(data)
//            } catch (e: Exception) {
//                Log.e("EkycViewModel", "Error processing eKYC result", e)
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        errorMessage = "Lỗi xử lý kết quả eKYC: ${e.message}",
//                        ekycStep = EkycStep.Failed
//                    )
//                }
//            }
//        }
//    }
//
//    private fun handleCancelResult(data: Intent) {
//        val lastStep = data.getStringExtra(KeyResultConstants.LAST_STEP)
//        _uiState.update {
//            it.copy(
//                isLoading = false,
//                errorMessage = "eKYC bị hủy bởi người dùng",
//                ekycStep = EkycStep.Failed,
//                lastStep = lastStep
//            )
//        }
//        logEkycResult(data)
//    }
//
//    private fun handleErrorResult(resultCode: Int) {
//        _uiState.update {
//            it.copy(
//                isLoading = false,
//                errorMessage = "eKYC thất bại - Mã lỗi: $resultCode",
//                ekycStep = EkycStep.Failed
//            )
//        }
//    }
//
//    /**
//     * Parse OCR data từ Intent result
//     */
//    private fun parseOcrData(data: Intent): OcrData? {
//        val ocrResultString = data.getStringExtra(KeyResultConstants.OCR_RESULT) ?: return null
//
//        return try {
//            val jsonRoot = JSONObject(ocrResultString)
//
//            if (jsonRoot.has("object")) {
//                val objectData = jsonRoot.getJSONObject("object")
//                OcrData(
//                    cardId = objectData.optString("id", ""),
//                    fullName = objectData.optString("name", ""),
//                    birthDay = objectData.optString("birth_day", ""),
//                    gender = objectData.optString("gender", ""),
//                    nationality = objectData.optString("nationality", ""),
//                    originLocation = objectData.optString("origin_location", ""),
//                    recentLocation = objectData.optString("recent_location", ""),
//                    issueDate = objectData.optString("issue_date", ""),
//                    validDate = objectData.optString("valid_date", ""),
//                    cardType = objectData.optString("card_type", ""),
//                    features = objectData.optString("features", "")
//                )
//            } else null
//        } catch (e: Exception) {
//            Log.e("EkycViewModel", "Error parsing OCR data", e)
//            null
//        }
//    }
//
//    /**
//     * Parse face compare result
//     */
//    private fun parseFaceCompareResult(data: Intent): FaceCompareResult? {
//        val compareResultString = data.getStringExtra(KeyResultConstants.COMPARE_FACE_RESULT)
//
//        if (compareResultString.isNullOrEmpty() || compareResultString == "NULL") {
//            return null
//        }
//
//        return try {
//            // Check if it's a JSON or simple boolean string
//            if (compareResultString.startsWith("{")) {
//                val json = JSONObject(compareResultString)
//                FaceCompareResult(
//                    isMatched = json.optBoolean("matched", false),
//                    similarity = json.optDouble("similarity", 0.0),
//                    message = json.optString("message", "")
//                )
//            } else {
//                // Simple boolean string
//                FaceCompareResult(
//                    isMatched = compareResultString.equals("true", ignoreCase = true),
//                    similarity = if (compareResultString.equals(
//                            "true",
//                            ignoreCase = true
//                        )
//                    ) 1.0 else 0.0,
//                    message = ""
//                )
//            }
//        } catch (e: Exception) {
//            Log.e("EkycViewModel", "Error parsing face compare result", e)
//            null
//        }
//    }
//
//    /**
//     * Parse liveness result
//     */
//    private fun parseLivenessResult(livenessString: String?): LivenessResult? {
//        if (livenessString.isNullOrEmpty() || livenessString == "NULL") {
//            return null
//        }
//
//        return try {
//            val json = JSONObject(livenessString)
//            LivenessResult(
//                isPassed = json.optBoolean("passed", false),
//                score = json.optDouble("score", 0.0),
//                message = json.optString("message", "")
//            )
//        } catch (e: Exception) {
//            Log.e("EkycViewModel", "Error parsing liveness result", e)
//            null
//        }
//    }
//
//    /**
//     * Parse images paths and hashes
//     */
//    private fun parseImages(data: Intent): EkycImages {
//        return EkycImages(
//            frontCardPath = data.getStringExtra(KeyResultConstants.PATH_IMAGE_FRONT_FULL) ?: "",
//            backCardPath = data.getStringExtra(KeyResultConstants.PATH_IMAGE_BACK_FULL) ?: "",
//            faceNearPath = data.getStringExtra(KeyResultConstants.PATH_IMAGE_FACE_NEAR_FULL) ?: "",
//            faceFarPath = data.getStringExtra(KeyResultConstants.PATH_IMAGE_FACE_FAR_FULL) ?: "",
//            hashFront = data.getStringExtra(KeyResultConstants.HASH_IMAGE_FRONT) ?: "",
//            hashBack = data.getStringExtra(KeyResultConstants.HASH_IMAGE_BACK) ?: "",
//            hashFaceNear = data.getStringExtra(KeyResultConstants.HASH_IMAGE_FACE_NEAR) ?: "",
//            hashFaceFar = data.getStringExtra(KeyResultConstants.HASH_IMAGE_FACE_FAR) ?: ""
//        )
//    }
//
//    /**
//     * Log chi tiết kết quả eKYC
//     */
//    private fun logEkycResult(data: Intent) {
//        fun get(key: String): String = data.getStringExtra(key) ?: "NULL"
//
//        Log.d("EkycViewModel", "========== EKYC RESULT ==========")
//        Log.d("EkycViewModel", "QR_CODE_RESULT = ${get(KeyResultConstants.QR_CODE_RESULT)}")
//        Log.d("EkycViewModel", "OCR_RESULT = ${get(KeyResultConstants.OCR_RESULT)}")
//        Log.d(
//            "EkycViewModel",
//            "COMPARE_FACE_RESULT = ${get(KeyResultConstants.COMPARE_FACE_RESULT)}"
//        )
//        Log.d(
//            "EkycViewModel",
//            "LIVENESS_FACE_RESULT = ${get(KeyResultConstants.LIVENESS_FACE_RESULT)}"
//        )
//        Log.d("EkycViewModel", "LAST_STEP = ${get(KeyResultConstants.LAST_STEP)}")
//        Log.d("EkycViewModel", "=================================")
//    }
//}