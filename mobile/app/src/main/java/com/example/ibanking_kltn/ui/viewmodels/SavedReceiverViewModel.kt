package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.di.SavedReceiverManager
import com.example.ibanking_kltn.data.dtos.SavedReceiver
import com.example.ibanking_kltn.data.repositories.WalletRepository
import com.example.ibanking_kltn.ui.event.SavedReceiverEffect
import com.example.ibanking_kltn.ui.event.SavedReceiverEvent
import com.example.ibanking_kltn.ui.uistates.SavedReceiverUiState
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.SnackBarType
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
class SavedReceiverViewModel @Inject constructor(
    private val savedReceiverManager: SavedReceiverManager,
    private val walletRepository: WalletRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SavedReceiverUiState())
    val uiState: StateFlow<SavedReceiverUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<SavedReceiverEffect>()
    val uiEffect = _uiEffect.asSharedFlow()


    init {
        loadSavedReceivers()
    }

    fun onEvent(event: SavedReceiverEvent) {
        when (event) {
            SavedReceiverEvent.AddSavedReceiver -> onAddSavedReceiver()
            is SavedReceiverEvent.ChangeKeyword -> onChangeKeyword(event.keyword)
            is SavedReceiverEvent.ChangeMemorableName -> onChangeMemorableName(event.memorableName)
            is SavedReceiverEvent.ChangeToWalletNumber -> onChangeToWalletNumber(event.walletNumber)
            SavedReceiverEvent.ClearAddDialog -> onClearAddDialog()
            is SavedReceiverEvent.DeleteSavedReceiver -> onDeleteSavedReceiver(event.walletNumber)
            SavedReceiverEvent.DoneWalletNumber -> onDoneWalletNumber()
            is SavedReceiverEvent.SaveReceiver -> onSaveReceiver(event.savedReceiver)
            SavedReceiverEvent.Search -> onSearch()
            is SavedReceiverEvent.SelectSavedReceiver -> onSelectSavedReceiver(event.savedReceiver)
        }
    }

    private fun loadSavedReceivers() {
        val savedServices = savedReceiverManager.getAll()
        _uiState.update {
            it.copy(
                savedReceivers = savedServices
            )
        }
    }

    private fun onChangeKeyword(keyword: String) {
        _uiState.update {
            it.copy(
                keyword = keyword
            )
        }
    }

    private fun onChangeToWalletNumber(toWalletNumber: String) {
        _uiState.update {
            it.copy(
                toWalletNumber = toWalletNumber
            )
        }
    }

    private fun onChangeMemorableName(memorableName: String) {
        _uiState.update {
            it.copy(
                memorableName = memorableName
            )
        }
    }

    private fun onClearAddDialog() {
        _uiState.update {
            it.copy(
                toWalletNumber = "",
                toMerchantName = "",
                memorableName = "",
            )
        }
    }

    private fun onSearch() {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }
        viewModelScope.launch {

            delay(1000L)
            _uiState.update {
                it.copy(
                    screenState = StateType.SUCCESS,
                    savedReceivers = savedReceiverManager.getAll().filter { receiver ->
                        receiver.toMerchantName.contains(uiState.value.keyword) || receiver.memorableName.contains(
                            uiState.value.keyword
                        ) || receiver.toWalletNumber.contains(uiState.value.keyword)
                    }
                )
            }
        }


    }


    private fun onDoneWalletNumber(
    ) {
        if (uiState.value.toWalletNumber.isEmpty()) {
            _uiState.update {
                it.copy(
                    toMerchantName = "",
                )
            }
            viewModelScope.launch {
                _uiEffect.emit(
                    SavedReceiverEffect.ShowSnackBar(
                        snackBar = SnackBarUiState(
                            message = "Số ví không được để trống",
                            type = SnackBarType.ERROR
                        )
                    )
                )

            }
            return
        }
        _uiState.update {
            it.copy(screenState = StateType.LOADING)
        }
        viewModelScope.launch {
            val apiResult = walletRepository.getInfoByWalletNumber(
                walletNumber = uiState.value.toWalletNumber
            )
            when (apiResult) {
                is ApiResult.Success -> {
                    val walletResponse = apiResult.data
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS,
                            toMerchantName = walletResponse.merchantName
                        )
                    }
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            toMerchantName = "",
                        )
                    }
                    _uiEffect.emit(
                        SavedReceiverEffect.ShowSnackBar(
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

    private fun onAddSavedReceiver(
    ) {
        _uiState.update {
            it.copy(screenState = StateType.LOADING)
        }
        viewModelScope.launch {
            val status = savedReceiverManager.add(
                SavedReceiver(
                    memorableName = uiState.value.memorableName,
                    toWalletNumber = uiState.value.toWalletNumber,
                    toMerchantName = uiState.value.toMerchantName,
                )
            )
            if (!status) {
                val message = "Tối đa chỉ được lưu 20 người nhận"
                _uiState.update {
                    it.copy(screenState = StateType.FAILED(message))
                }
                _uiEffect.emit(
                    SavedReceiverEffect.ShowSnackBar(
                        snackBar = SnackBarUiState(
                            message = message,
                            type = SnackBarType.ERROR
                        )
                    )
                )
                return@launch
            }
            _uiState.update {
                it.copy(
                    screenState = StateType.SUCCESS,
                    toWalletNumber = "",
                    toMerchantName = "",
                    memorableName = "",
                )
            }
            loadSavedReceivers()
            _uiEffect.emit(
                SavedReceiverEffect.ShowSnackBar(
                    snackBar = SnackBarUiState(
                        message = "Lưu người nhận thành công",
                        type = SnackBarType.SUCCESS
                    )
                )
            )
        }
    }

    private fun onDeleteSavedReceiver(
        walletNumber: String,
    ) {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            savedReceiverManager.deleteByWalletNumber(
                walletNumber = walletNumber
            )
            loadSavedReceivers()
            _uiState.update {
                it.copy(
                    screenState = StateType.SUCCESS
                )
            }
            _uiEffect.emit(
                SavedReceiverEffect.ShowSnackBar(
                    snackBar = SnackBarUiState(
                        message = "Xóa người nhận thành công",
                        type = SnackBarType.SUCCESS
                    )
                )
            )
        }
    }

    private fun onSelectSavedReceiver(savedReceiver: SavedReceiver) {
        _uiState.update {
            it.copy(
                selectedSavedReceiver = savedReceiver
            )
        }
    }

    private fun onSaveReceiver(savedReceiver: SavedReceiver) {
        savedReceiverManager.add(savedReceiver)
    }

}