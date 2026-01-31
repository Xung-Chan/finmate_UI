package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.ibanking_kltn.dtos.responses.TransactionHistoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow<TransactionHistoryResponse?>(null)
    val uiState: StateFlow<TransactionHistoryResponse?> = _uiState.asStateFlow()



    fun clearState() {
        _uiState.value = null
    }

    fun init(transaction: TransactionHistoryResponse) {
        _uiState.value = transaction
    }

}