package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.ui.event.HandleDepositEffect
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HandleDepositViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val _uiEffect = MutableSharedFlow<HandleDepositEffect>()
    val uiEffect = _uiEffect.asSharedFlow()


    init {

        val txnRef = savedStateHandle.get<String>("vnp_TxnRef") ?: ""
        val responseCode = savedStateHandle.get<String>("vnp_ResponseCode") ?: ""
        if (txnRef.isNotEmpty() && responseCode.isNotEmpty()) {
            handleVNPayReturn(
                vnp_ResponseCode = responseCode,
                vnp_TxnRef = txnRef
            )
        }
    }

    private fun handleVNPayReturn(
        vnp_ResponseCode: String,
        vnp_TxnRef: String
    ) {
        viewModelScope.launch {
            val handleResult = transactionRepository.handleVNPayReturn(
                vnp_ResponseCode = vnp_ResponseCode, vnp_TxnRef = vnp_TxnRef
            )
            when (handleResult) {
                is ApiResult.Error -> {
                    _uiEffect.emit(
                        HandleDepositEffect.ShowSnackBar(
                            SnackBarUiState(
                                message = handleResult.message,
                                type = SnackBarType.ERROR
                            )
                        )
                    )
                    return@launch
                }

                is ApiResult.Success -> {
                    _uiEffect.emit(
                        HandleDepositEffect.NavigateToTransactionResult(
                            transactionId = handleResult.data.transactionId
                        )
                    )
                }
            }

        }
    }

}