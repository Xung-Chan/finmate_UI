package com.example.ibanking_kltn.ui.screens.ekyc.transaction_verify

import androidx.activity.result.ActivityResult
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState

/**
 * MVI Intent - Đại diện cho các hành động người dùng có thể thực hiện trong eKYC flow
 */
sealed interface VerifyTransactionEkycEvent {
    data class HandleEkycResult(val activityResult: ActivityResult) : VerifyTransactionEkycEvent
}

sealed class VerifyTransactionEkycEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) :VerifyTransactionEkycEffect()
    data class VerifyFailed(val message: String) : VerifyTransactionEkycEffect()
    data class BackToConfirm(val hashedData: String) : VerifyTransactionEkycEffect()
}