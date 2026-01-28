package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.MoneyFlowType
import com.example.ibanking_kltn.data.dtos.requests.DistributionStatisticRequest
import com.example.ibanking_kltn.data.dtos.requests.TrendStatisticRequest
import com.example.ibanking_kltn.data.repositories.AiRepository
import com.example.ibanking_kltn.data.repositories.TransactionRepository
import com.example.ibanking_kltn.data.usecase.GetDistributionStatisticUC
import com.example.ibanking_kltn.data.usecase.GetTrendStatisticUC
import com.example.ibanking_kltn.ui.event.AnalyticEffect
import com.example.ibanking_kltn.ui.event.AnalyticEvent
import com.example.ibanking_kltn.ui.uistates.AnalyticUiState
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
class AnalyticViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val aiRepository: AiRepository,
    private val getDistributionStatisticUC: GetDistributionStatisticUC,
    private val getTrendStatisticUC: GetTrendStatisticUC,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AnalyticUiState())
    val uiState: StateFlow<AnalyticUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<AnalyticEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        initData()
    }


    fun onEvent(event: AnalyticEvent) {
        when (event) {
            is AnalyticEvent.ChangeMoneyFlowType -> onChangeMoneyFlowType(event.flowType)
            AnalyticEvent.RetryLoadData -> retryLoadData()
            AnalyticEvent.Analyze -> onAnalyze()
            AnalyticEvent.MinusMonth -> onMinusMonth()
            AnalyticEvent.PlusMonth -> onPlusMonth()
        }
    }


    private fun retryLoadData() {
        initData()
    }

    private fun initData() {
        _uiState.update {
            it.copy(
                initState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            val trendStatisticUC = getTrendStatisticUC()
            val distributionStatisticUC = getDistributionStatisticUC()
            if (trendStatisticUC is ApiResult.Error) {
                _uiState.update {
                    it.copy(
                        initState = StateType.FAILED(trendStatisticUC.message)
                    )
                }
                _uiEffect.emit(
                    AnalyticEffect.ShowSnackBar(
                        snackBar = SnackBarUiState(
                            message = trendStatisticUC.message,
                            type = SnackBarType.ERROR
                        )
                    )
                )
                return@launch
            }
            if (distributionStatisticUC is ApiResult.Error) {
                _uiState.update {
                    it.copy(
                        initState = StateType.FAILED(distributionStatisticUC.message)
                    )
                }
                _uiEffect.emit(
                    AnalyticEffect.ShowSnackBar(
                        snackBar = SnackBarUiState(
                            message = distributionStatisticUC.message,
                            type = SnackBarType.ERROR
                        )
                    )
                )
                return@launch
            }
            val totalValue = (distributionStatisticUC as ApiResult.Success).data.distributions.sumOf { it.totalValue }

            _uiState.update {
                it.copy(
                    initState = StateType.SUCCESS,
                    trendStatistic = (trendStatisticUC as ApiResult.Success).data,
                    distributionStatistic = distributionStatisticUC.data,
                    totalValue = totalValue
                )
            }
        }
    }

    private fun loadDistributionStatistic(
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

                        _uiEffect.emit(
                            AnalyticEffect.ShowSnackBar(
                                snackBar = SnackBarUiState(
                                    message = apiResult.message,
                                    type = SnackBarType.ERROR
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    private fun loadTrendStatistic(
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

                        _uiEffect.emit(
                            AnalyticEffect.ShowSnackBar(
                                snackBar = SnackBarUiState(
                                    message = apiResult.message,
                                    type = SnackBarType.ERROR
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    private fun onAnalyze(
    ) {
        _uiState.update {
            it.copy(
                analyzeState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            if (uiState.value.distributionStatistic?.analyticId == null) {
                _uiEffect.emit(
                    AnalyticEffect.ShowSnackBar(
                        snackBar = SnackBarUiState(
                            message = "Vui lòng thống kê phân bổ trước khi phân tích",
                            type = SnackBarType.ERROR
                        )
                    )
                )
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

                    _uiEffect.emit(
                        AnalyticEffect.ShowSnackBar(
                            snackBar = SnackBarUiState(
                                message = apiResult.message,
                                type = SnackBarType.ERROR
                            )
                        )
                    )
                }
            }

        }
    }

    private fun onMinusMonth(
    ) {
        _uiState.update {
            it.copy(
                selectedTime = it.selectedTime.minusMonths(1)
            )
        }
        loadDistributionStatistic(
        )

    }

    private fun onPlusMonth(
    ) {
        _uiState.update {
            it.copy(
                selectedTime = it.selectedTime.plusMonths(1)
            )
        }
        loadDistributionStatistic()

    }

    private fun onChangeMoneyFlowType(
        flowType: MoneyFlowType,
    ) {
        _uiState.update {
            it.copy(
                selectedFlowType = flowType
            )
        }
        loadTrendStatistic(
        )

    }

}