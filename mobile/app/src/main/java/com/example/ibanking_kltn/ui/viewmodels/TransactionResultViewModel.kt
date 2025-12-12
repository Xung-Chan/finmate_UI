package com.example.ibanking_kltn.ui.viewmodels

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
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionResultUiState())
    val uiState: StateFlow<TransactionResultUiState> = _uiState.asStateFlow()

    fun init(
        service: String,
        amount: Long,
        toMerchantName: String,
        status: TransactionStatus
    ){
        _uiState.value= TransactionResultUiState(
            service = service,
            amount = amount,
            toMerchantName = toMerchantName,
            status = status
        )
    }

}