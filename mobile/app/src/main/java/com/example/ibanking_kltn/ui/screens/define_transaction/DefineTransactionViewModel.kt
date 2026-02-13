package com.example.ibanking_kltn.ui.screens.define_transaction

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.repositories.AiRepository
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.dtos.requests.DefineTransactionRequest
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DefineTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val aiRepository: AiRepository,

    ) : ViewModel() {
    private val _uiState = MutableStateFlow(DefineTransactionUiState())
    val uiState: StateFlow<DefineTransactionUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<DefineTransactionEffect>()
    val uiEffect = _uiEffect.asSharedFlow()


    fun onEvent(event: DefineTransactionEvent) {
        when (event) {
            is DefineTransactionEvent.UpdateTransactionId -> {
                _uiState.update { it.copy(transactionId = event.transactionId) }
            }

            is DefineTransactionEvent.UpdateDestinationAccountNumber -> {
                _uiState.update { it.copy(toAccountNumber = event.accountNumber) }
            }

            is DefineTransactionEvent.UpdateDestinationAccountName -> {
                _uiState.update { it.copy(toMerchantName = event.accountName) }
            }

            is DefineTransactionEvent.UpdateAmount -> {
                val amountValue = event.amount.filter { it.isDigit() }.toLongOrNull() ?: 0L
                _uiState.update { it.copy(amount = amountValue) }
            }

            is DefineTransactionEvent.UpdateDescription -> {
                _uiState.update { it.copy(description = event.description) }
            }

            is DefineTransactionEvent.UpdateDateTime -> {
                _uiState.update { it.copy(transactionDateTime = event.dateTime) }
            }

            is DefineTransactionEvent.SelectImage -> {
                handleImageSelection(event.uri)
            }

            DefineTransactionEvent.ClearImage -> {
                _uiState.update { it.copy(selectedImageUri = null) }
            }

            DefineTransactionEvent.SubmitTransaction -> {
                submitTransaction()
            }

            DefineTransactionEvent.ClearForm -> {
                clearForm()
            }
        }
    }

    private fun handleImageSelection(uri: Uri) {
        _uiState.update {
            it.copy(
                selectedImageUri = uri,
                isProcessingImage = true
            )
        }

        viewModelScope.launch {
            val apiResult = aiRepository.extractTransaction(uri)
            when (apiResult) {
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isProcessingImage = false,
                        )
                    }
                    _uiEffect.emit(
                        DefineTransactionEffect.ShowSnackBar(
                            SnackBarUiState(
                                message = apiResult.message,
                                type = SnackBarType.ERROR
                            )
                        )
                    )
                }

                is ApiResult.Success -> {
                    val extractedData = apiResult.data
                    _uiState.update {
                        it.copy(
                            toAccountNumber = extractedData.destinationAccountNumber,
                            toMerchantName = extractedData.destinationAccountName,
                            amount = extractedData.amount,
                            description = extractedData.transactionDescription,
                            transactionDateTime = extractedData.transactionDateTime,
                            isProcessingImage = false,
                        )
                    }
                }
            }
        }
    }

    private fun submitTransaction() {
        _uiState.update {
            it.copy(screenState = StateType.LOADING)
        }
        viewModelScope.launch {
            val request = DefineTransactionRequest(
                amount = uiState.value.amount,
                destinationAccountName = uiState.value.toMerchantName,
                destinationAccountNumber = uiState.value.toAccountNumber,
                transactionDateTime = uiState.value.transactionDateTime,
                transactionDescription = uiState.value.description,
                transactionId = uiState.value.transactionId,
            )
            val result = transactionRepository.defineTransaction(request)
            when (result) {
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(screenState = StateType.FAILED(result.message))
                    }
                    _uiEffect.emit(
                        DefineTransactionEffect.ShowSnackBar(
                            SnackBarUiState(
                                message = result.message,
                                type = SnackBarType.ERROR
                            )
                        )
                    )
                }

                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(screenState = StateType.SUCCESS)
                    }
                    _uiEffect.emit(
                        DefineTransactionEffect.DefineSuccess
                    )
                }
            }
        }
    }

    private fun clearForm() {
        _uiState.update {
            it.copy(
                toAccountNumber = "",
                toMerchantName = "",
                amount = 0L,
                description = "",
                transactionDateTime = "",
                selectedImageUri = null
            )
        }
    }

}
