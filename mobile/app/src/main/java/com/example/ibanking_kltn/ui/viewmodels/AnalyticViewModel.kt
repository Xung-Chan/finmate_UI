package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.MoneyFlowType
import com.example.ibanking_kltn.data.dtos.requests.DistributionStatisticRequest
import com.example.ibanking_kltn.data.dtos.requests.TrendStatisticRequest
import com.example.ibanking_kltn.data.repositories.AiRepository
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.ui.uistates.AnalyticUiState
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
class AnalyticViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val aiRepository: AiRepository,

    ) : ViewModel() {
    private val _uiState = MutableStateFlow(AnalyticUiState())
    val uiState: StateFlow<AnalyticUiState> = _uiState.asStateFlow()

    fun init(
        onError: (String) -> Unit
    ) {
        clearState()
        loadDistributionStatistic(
            onError = onError
        )
        loadTrendStatistic(
            onError = onError
        )
    }

    fun clearState() {
        _uiState.value = AnalyticUiState()
    }


    fun loadDistributionStatistic(
        onError: (message: String) -> Unit
    ) {
        _uiState.update {
            it.copy(state = StateType.LOADING)
        }
        viewModelScope.launch {
            val request = DistributionStatisticRequest(
                referenceDate = uiState.value.selectedTime.toString()
            )
            repeat(3) {

                val apiResult = transactionRepository.getDistributionStatistic(
                    request = request
                )
                when (apiResult) {
                    is ApiResult.Success -> {
                        val totalValue = apiResult.data.distributions.sumOf { it.totalValue }
                        _uiState.update {
                            it.copy(
                                state = StateType.SUCCESS,
                                distributionStatistic = apiResult.data,
                                totalValue = totalValue,
                                initialedDistributionStatistic = true
                            )
                        }
                        return@launch

                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                state = StateType.FAILED(apiResult.message)
                            )
                        }

                        onError(apiResult.message)
                    }
                }
            }
            _uiState.update {
                it.copy(
                    initialedDistributionStatistic = true
                )
            }
        }
    }

    fun loadTrendStatistic(
        onError: (message: String) -> Unit
    ) {
        _uiState.update {
            it.copy(state = StateType.LOADING)
        }
        viewModelScope.launch {
            val request = TrendStatisticRequest(
                moneyFlowType = uiState.value.selectedFlowType.name,
                year = uiState.value.selectedTime.year
            )
            repeat(3) {

                val apiResult = transactionRepository.getTrendStatistic(
                    request = request
                )
                when (apiResult) {
                    is ApiResult.Success -> {

                        _uiState.update {
                            it.copy(
                                state = StateType.SUCCESS,
                                trendStatistic = apiResult.data,
                                initialedTrendStatistic = true
                            )
                        }
                        return@launch
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                state = StateType.FAILED(apiResult.message)
                            )
                        }

                        onError(apiResult.message)
                    }
                }
            }
            _uiState.update {
                it.copy(
                    initialedTrendStatistic = true
                )
            }
        }
    }

    fun onAnalyze(
        onError: (message: String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                analyzeState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            if (uiState.value.distributionStatistic?.analyticId == null) {
                onError("Vui lòng thống kê phân bổ trước khi phân tích")
                _uiState.update {
                    it.copy(
                        analyzeState = StateType.FAILED("Vui lòng thống kê phân bổ trước khi phân tích")
                    )
                }
                return@launch
            }
            val apiResult = aiRepository.getAnalytic(
                analyzeRequestId = uiState.value.distributionStatistic!!.analyticId
            )
            when (apiResult) {
                is ApiResult.Success -> {

                    _uiState.update {
                        it.copy(
                            analyzeState = StateType.SUCCESS,
                            analyzeResponse = apiResult.data,
                        )
                    }
                    return@launch
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            analyzeState = StateType.FAILED(apiResult.message)
                        )
                    }

                    onError(apiResult.message)
                }
            }

        }
    }

    fun onMinusMonth(
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                selectedTime = it.selectedTime.minusMonths(1)
            )
        }
        loadDistributionStatistic(
            onError = onError
        )

    }

    fun onPlusMonth(
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                selectedTime = it.selectedTime.plusMonths(1)
            )
        }
        loadDistributionStatistic(
            onError = onError
        )

    }

    fun onChangeMoneyFlowType(
        flowType: MoneyFlowType,
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                selectedFlowType = flowType
            )
        }
        loadTrendStatistic(
            onError = onError
        )

    }

}