package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.dtos.definitions.PayLaterApplicationType
import com.example.ibanking_kltn.dtos.requests.PayLaterApplicationRequest
import com.example.ibanking_kltn.data.repositories.PayLaterRepository
import com.example.ibanking_kltn.ui.uistates.PayLaterApplicationUiState
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
class PayLaterApplicationViewModel @Inject constructor(
    private val payLaterRepository: PayLaterRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PayLaterApplicationUiState())
    val uiState: StateFlow<PayLaterApplicationUiState> = _uiState.asStateFlow()


    fun init(
        type: PayLaterApplicationType,
    ) {

        _uiState.value = PayLaterApplicationUiState(
            applicationType = type
        )
    }


    fun onClickConfirm(
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
            _uiState.update {
                it.copy(
                    screenState = StateType.LOADING
                )
            }

        viewModelScope.launch {

                var request = PayLaterApplicationRequest(
                    type =  uiState.value.applicationType.name,
                    requestedCreditLimit =  uiState.value.requestLimit
                )
                if(uiState.value.reason != null){
                    request = request.copy(
                        reason = uiState.value.reason
                    )
                }
                val apiResult =payLaterRepository.submitApplication(request =  request)
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

    fun onChangeRequestLimit(amount: String) {
        val formatAmount = amount
            .replace(".", "")
            .replace(",", "")
        if (formatAmount == "") {
            _uiState.update {
                it.copy(requestLimit = 0L)
            }
            return
        }
        _uiState.update {
            it.copy(requestLimit = formatAmount.toLong())
        }
    }

    fun onChangeReason(reason: String) {
        _uiState.update {
            it.copy(reason = reason)
        }
    }


}