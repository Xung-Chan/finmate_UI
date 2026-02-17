package com.example.ibanking_kltn.ui.screens.ekyc.transaction_verify

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vnptit.idg.sdk.utils.KeyResultConstants
import com.vnptit.idg.sdk.utils.SDKEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyTransactionEkycViewModel @Inject constructor(
) : ViewModel() {
    private val _uiEffect = MutableSharedFlow<VerifyTransactionEkycEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    fun onEvent(event: VerifyTransactionEkycEvent) {
        when (event) {
            is VerifyTransactionEkycEvent.HandleEkycResult -> handleEkycResult(event.activityResult)
        }
    }

    private fun handleEkycResult(result: ActivityResult) {
        viewModelScope.launch {
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val data = result.data!!


                val lastStep = data.getStringExtra(KeyResultConstants.LAST_STEP)
                val isCompleted = lastStep == SDKEnum.LastStepEnum.Done.value

                if (!isCompleted) {
                    _uiEffect.emit(
                        VerifyTransactionEkycEffect.VerifyFailed(
                            message = "eKYC chưa hoàn tất"
                        )
                    )
                    return@launch
                }

                val hashedData = data.getStringExtra(KeyResultConstants.HASH_IMAGE_FACE_NEAR)
                if (hashedData.isNullOrEmpty()) {
                    _uiEffect.emit(
                        VerifyTransactionEkycEffect.VerifyFailed(
                            message = "Không nhận được dữ liệu khuôn mặt"
                        )
                    )
                    return@launch
                }
                _uiEffect.emit(
                    VerifyTransactionEkycEffect.BackToConfirm(
                        hashedData = hashedData
                    )
                )


            } else {
                _uiEffect.emit(
                    VerifyTransactionEkycEffect.VerifyFailed(
                        message = "eKYC thất bại hoặc bị hủy"
                    )
                )
            }
        }
    }
}