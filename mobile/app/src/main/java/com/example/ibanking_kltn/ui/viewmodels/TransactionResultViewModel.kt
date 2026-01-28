package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.ibanking_kltn.data.dtos.TransactionStatus
import com.example.ibanking_kltn.ui.uistates.TransactionResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class TransactionResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionResultUiState())
    val uiState: StateFlow<TransactionResultUiState> = _uiState.asStateFlow()

    init {
        val status = savedStateHandle.get<String>("status")
        val amount = savedStateHandle.get<Long>("amount") ?: 0L
        val service = savedStateHandle.get<String>("service") ?: ""
        if (!status.isNullOrEmpty() && amount > 0L && service.isNotEmpty()) {
            _uiState.value = TransactionResultUiState(
                service = service,
                amount = amount,
                status = TransactionStatus.valueOf(status),
            )
        }

    }


}