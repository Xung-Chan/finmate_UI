package com.example.ibanking_kltn.ui.screens.ekyc.register

import androidx.activity.result.ActivityResult
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState

/**
 * MVI Intent - Đại diện cho các hành động người dùng có thể thực hiện trong eKYC flow
 */
sealed interface FullEkycEvent {
    data class HandleEkycResult(val activityResult: ActivityResult) : FullEkycEvent
}

sealed class FullEkycEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : FullEkycEffect()
    data class EkycFailed(val message: String) : FullEkycEffect()
    object EkycCompleted : FullEkycEffect()


}