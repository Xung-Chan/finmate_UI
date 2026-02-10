package com.example.ibanking_kltn.ui.screens.spending.spending_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.repositories.SpendingRepository
import com.example.ibanking_kltn.dtos.definitions.Screens
import com.example.ibanking_kltn.dtos.requests.SpendingSnapshotRequest
import com.example.ibanking_kltn.dtos.responses.SpendingSnapshotResponse
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_kltn.utils.formatterDateString
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SpendingManagementViewModel @Inject constructor(
    private val spendingRepository: SpendingRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SpendingUiState())
    val uiState: StateFlow<SpendingUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<SpendingManagementEffect>()
    val uiEffect = _uiEffect.asSharedFlow()


    init {
        reinitSpendingSnapshots()
    }

    fun onEvent(event: SpendingManagementEvent) {
        when (event) {
            is SpendingManagementEvent.CreateSpendingSnapshot -> createSpendingSnapshot(
                snapshotName = event.snapshotName,
                budgetAmount = event.budgetAmount,
                monthlySpending = formatterDateString(event.monthlySpending, "yyyy/MM")
            )

            is SpendingManagementEvent.UpdateSpendingSnapshot -> updateSpendingSnapshot(
                snapshotId = event.snapshotId,
                snapshotName = event.snapshotName,
                budgetAmount = event.budgetAmount,
                monthlySpending = formatterDateString(event.monthlySpending, "yyyy/MM")
            )

            is SpendingManagementEvent.DeleteSpendingSnapshot -> deleteSpendingSnapshot(event.snapshotId)
            is SpendingManagementEvent.RetryInitSpendingSnapshots -> reinitSpendingSnapshots()
            is SpendingManagementEvent.RefreshSpendingSnapshots -> reloadSpendingSnapshots()
            is SpendingManagementEvent.NavigateToDetail -> navigateToDetail(event.snapshotId)
            SpendingManagementEvent.ChangeAddDialogState -> changeAddDialogState()
            is SpendingManagementEvent.ChangeEditDialogState -> changeEditDialogState(event.snapshotId)
            is SpendingManagementEvent.ChangeDeleteDialogState -> changeDeleteDialogState(event.snapshotId)
            SpendingManagementEvent.NavigateToCategoryManagement -> navigateToCategoryManagement()
        }
    }

    private fun changeAddDialogState() {
        _uiState.update {
            it.copy(
                isShowAddDialog = !it.isShowAddDialog
            )
        }
    }

    private fun navigateToCategoryManagement() {
        viewModelScope.launch {
            _uiEffect.emit(
                SpendingManagementEffect.NavigateToCategoryManagement
            )
        }
    }

    private fun changeEditDialogState(snapshotId: String?) {
        _uiState.update {
            it.copy(
                isShowEditDialog = !it.isShowEditDialog,
                selectedSnapshotId = snapshotId
            )
        }
    }

    private fun changeDeleteDialogState(snapshotId: String?) {
        _uiState.update {
            it.copy(
                isShowDeleteDialog = !it.isShowDeleteDialog,
                selectedSnapshotId = snapshotId
            )
        }
    }

    private fun reinitSpendingSnapshots() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    screenState = SpendingManagementState.INIT
                )
            }
            val result = loadAllSpendingSnapshots(3)
            when (result) {
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = SpendingManagementState.INIT_FAILED
                        )
                    }
                    _uiEffect.emit(
                        SpendingManagementEffect.ShowSnackBar(
                            snackBar = SnackBarUiState(
                                message = result.message,
                                type = SnackBarType.ERROR
                            )
                        )
                    )
                }

                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            screenState = SpendingManagementState.NONE,
                            spendingSnapshots = result.data
                        )
                    }
                }
            }
        }
    }

    private fun reloadSpendingSnapshots(
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    screenState = SpendingManagementState.REFRESHING
                )
            }
            val result = loadAllSpendingSnapshots(1)
            when (result) {
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = SpendingManagementState.NONE
                        )
                    }
                    _uiEffect.emit(
                        SpendingManagementEffect.ShowSnackBar(
                            snackBar = SnackBarUiState(
                                message = result.message,
                                type = SnackBarType.ERROR
                            )
                        )
                    )
                }

                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            screenState = SpendingManagementState.NONE,
                            spendingSnapshots = result.data
                        )
                    }
                }
            }
        }
    }

    private suspend fun loadAllSpendingSnapshots(
        retry: Int
    ): ApiResult<List<SpendingSnapshotResponse>> {
        var message = ""
        repeat(retry) {
            val apiResult = spendingRepository.getAllSpending()
            when (apiResult) {
                is ApiResult.Success -> {
                    return apiResult
                }

                is ApiResult.Error -> {
                    message = apiResult.message
                    delay(1000L)
                }
            }
        }
        return ApiResult.Error(
            message = message
        )
    }


    private fun createSpendingSnapshot(
        snapshotName: String,
        budgetAmount: Long,
        monthlySpending: String
    ) {

        _uiState.update {
            it.copy(
                screenState = SpendingManagementState.LOADING
            )
        }
        viewModelScope.launch {
            if (snapshotName.isEmpty() || budgetAmount <= 0L) {
                _uiState.update {
                    it.copy(
                        screenState = SpendingManagementState.NONE
                    )
                }
                _uiEffect.emit(
                    SpendingManagementEffect.ShowSnackBar(
                        snackBar = SnackBarUiState(
                            message = "Vui lòng điền đầy đủ thông tin",
                            type = SnackBarType.WARNING
                        )
                    )
                )
                return@launch
            }

            val request = SpendingSnapshotRequest(
                snapshotName = snapshotName,
                budgetAmount = budgetAmount.toBigDecimal(),
                monthlySpending = monthlySpending
            )
            val apiResult = spendingRepository.createSpendingSnapshot(
                request = request
            )
            when (apiResult) {
                is ApiResult.Success -> {
                    _uiEffect.emit(
                        SpendingManagementEffect.ShowSnackBar(
                            snackBar = SnackBarUiState(
                                message = "Tạo quỹ chi tiêu thành công",
                                type = SnackBarType.SUCCESS
                            )
                        )
                    )
                    changeAddDialogState()
                    reloadSpendingSnapshots()
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = SpendingManagementState.NONE
                        )
                    }
                    _uiEffect.emit(
                        SpendingManagementEffect.ShowSnackBar(
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

    private fun updateSpendingSnapshot(
        snapshotId: String,
        snapshotName: String,
        budgetAmount: Long,
        monthlySpending: String
    ) {
        _uiState.update {
            it.copy(
                screenState = SpendingManagementState.LOADING
            )
        }
        viewModelScope.launch {
            if (snapshotName.isEmpty() || budgetAmount <= 0L) {
                _uiState.update {
                    it.copy(
                        screenState = SpendingManagementState.NONE
                    )
                }
                _uiEffect.emit(
                    SpendingManagementEffect.ShowSnackBar(
                        snackBar = SnackBarUiState(
                            message = "Vui lòng điền đầy đủ thông tin",
                            type = SnackBarType.WARNING
                        )
                    )
                )
                return@launch
            }

            val request = SpendingSnapshotRequest(
                snapshotName = snapshotName,
                budgetAmount = budgetAmount.toBigDecimal(),
                monthlySpending = monthlySpending
            )
            val apiResult = spendingRepository.updateSpendingSnapshot(
                snapshotId = snapshotId,
                request = request
            )
            when (apiResult) {
                is ApiResult.Success -> {
                    _uiEffect.emit(
                        SpendingManagementEffect.ShowSnackBar(
                            snackBar = SnackBarUiState(
                                message = "Cập nhật quỹ chi tiêu thành công",
                                type = SnackBarType.SUCCESS
                            )
                        )
                    )
                    changeEditDialogState(null)
                    reloadSpendingSnapshots()
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = SpendingManagementState.NONE
                        )
                    }
                    _uiEffect.emit(
                        SpendingManagementEffect.ShowSnackBar(
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

    private fun deleteSpendingSnapshot(snapshotId: String) {
        _uiState.update {
            it.copy(
                screenState = SpendingManagementState.LOADING
            )
        }
        viewModelScope.launch {
            val apiResult = spendingRepository.deleteSpendingSnapshot(snapshotId)
            when (apiResult) {
                is ApiResult.Success -> {
                    _uiEffect.emit(
                        SpendingManagementEffect.ShowSnackBar(
                            snackBar = SnackBarUiState(
                                message = "Xóa quỹ chi tiêu thành công",
                                type = SnackBarType.SUCCESS
                            )
                        )
                    )
                    changeDeleteDialogState(null)
                    reloadSpendingSnapshots()
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = SpendingManagementState.NONE
                        )
                    }
                    _uiEffect.emit(
                        SpendingManagementEffect.ShowSnackBar(
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

    private fun navigateToDetail(snapshotId: String) {
        viewModelScope.launch {
            _uiEffect.emit(
                SpendingManagementEffect.NavigateToDetail(
                    route = "${Screens.SpendingSnapshotDetail.name}/$snapshotId"
                )
            )
        }
    }
}