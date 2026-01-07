package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.PayLaterApplicationType
import com.example.ibanking_kltn.data.dtos.requests.PayLaterApplicationRequest
import com.example.ibanking_kltn.data.repositories.AuthRepository
import com.example.ibanking_kltn.data.repositories.PayLaterRepository
import com.example.ibanking_kltn.ui.uistates.PayLaterUiState
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
class PayLaterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val payLaterRepository: PayLaterRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PayLaterUiState())
    val uiState: StateFlow<PayLaterUiState> = _uiState.asStateFlow()


    fun init(
        onError: (message: String) -> Unit
    ) {
        clearState()
        loadUserInfo(
            onError = onError
        )
        loadPayLaterInfo(
            onError = onError
        )

    }

    fun clearState() {
        _uiState.value = PayLaterUiState()
    }

    fun loadUserInfo(
        onError: (message: String) -> Unit
    ) {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    screenState = StateType.LOADING
                )
            }

            repeat(3) {
                val apiResult = authRepository.getMyProfile()
                when (apiResult) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                screenState = StateType.SUCCESS,
                                userInfo = apiResult.data,
                                initialedUserInfo = true
                            )
                        }
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

    fun loadPayLaterInfo(
        onError: (message: String) -> Unit
    ) {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    screenState = StateType.LOADING
                )
            }

            repeat(3) {
                val apiResult = payLaterRepository.getMyPayLater()
                when (apiResult) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                screenState = StateType.SUCCESS,
                                payLaterInfo = apiResult.data,
                                initialedPayLaterInfo = true

                            )
                        }
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

    fun lockAccountRequest(
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            val request = PayLaterApplicationRequest(
                type = PayLaterApplicationType.SUSPEND_REQUEST.name,
            )
            val apiResult = payLaterRepository.submitApplication(request=request)
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

    fun unlockAccountRequest(
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {

        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            val request = PayLaterApplicationRequest(
                type = PayLaterApplicationType.ACTIVATION.name,
            )
            val apiResult = payLaterRepository.submitApplication(request=request)
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