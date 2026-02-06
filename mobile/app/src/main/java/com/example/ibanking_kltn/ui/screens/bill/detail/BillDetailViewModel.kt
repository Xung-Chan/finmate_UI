package com.example.ibanking_kltn.ui.screens.bill.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.repositories.BillRepository
import com.example.ibanking_kltn.dtos.requests.CancelBillRequest
import com.example.ibanking_kltn.dtos.responses.BillResponse
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class BillDetailViewModel @Inject constructor(
    private val billRepository: BillRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<BillDetailUiState>(BillDetailUiState())
    val uiState: StateFlow<BillDetailUiState> = _uiState.asStateFlow()



    fun clearState() {
        _uiState.value = BillDetailUiState()
    }

    fun init(bill: BillResponse) {
        _uiState.value = BillDetailUiState(
            screenState = StateType.NONE,
            bill = bill
        )
    }


    fun onCancelBill(
        onSuccess: ()->Unit,
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            val request = CancelBillRequest(
                qrIdentifier = _uiState.value.bill!!.qrIdentifier
            )
            val apiResult = billRepository.cancelBill(request)
            when (apiResult) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS,
                        )
                    }
                    onSuccess()
                    return@launch
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.FAILED(apiResult.message)
                        )
                    }
                    onError(apiResult.message)
                }
            }


        }

    }
}