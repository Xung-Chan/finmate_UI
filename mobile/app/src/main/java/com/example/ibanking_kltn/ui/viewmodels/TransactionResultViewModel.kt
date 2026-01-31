package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.usecase.GetTransactionUC
import com.example.ibanking_kltn.dtos.definitions.NavKey
import com.example.ibanking_kltn.ui.event.TransactionResultEffect
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.uistates.TransactionResultUiState
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TransactionResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTransactionUC: GetTransactionUC
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionResultUiState())
    val uiState: StateFlow<TransactionResultUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<TransactionResultEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        val transactionId = savedStateHandle.get<String>(NavKey.TRANSACTION_ID.name)
        viewModelScope.launch {
            if (transactionId == null) {
                _uiEffect.emit(
                    TransactionResultEffect.InitFailed
                )
                return@launch
            }
            val result = getTransactionUC(transactionId = transactionId)
            when (result) {
                is ApiResult.Error -> {
                    _uiEffect.emit(
                        TransactionResultEffect.ShowSnackBar(
                            snackBar = SnackBarUiState(
                                message = result.message,
                                type = SnackBarType.ERROR
                            )
                        )
                    )
                }

                is ApiResult.Success -> {
                    val transaction = result.data
                    _uiState.value = TransactionResultUiState(
                        service = transaction.transactionType.serviceName,
                        amount = transaction.amount.toLong(),
                        status =transaction.status
                    )
                }
            }
        }

    }


}