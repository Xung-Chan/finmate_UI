package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.di.SavedReceiverManager
import com.example.ibanking_kltn.data.dtos.SavedReceiver
import com.example.ibanking_kltn.data.repositories.WalletRepository
import com.example.ibanking_kltn.ui.uistates.SavedReceiverUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun init() {
        clear()
        loadSavedReceivers()
    }

    fun clear() {
        _uiState.value = SavedReceiverUiState()
    }

    fun loadSavedReceivers() {
        val savedServices = savedReceiverManager.getAll()
        _uiState.update {
            it.copy(
                savedReceivers = savedServices
            )
        }
    }

    fun onChangeKeyword(keyword: String) {
        _uiState.update {
            it.copy(
                keyword = keyword
            )
        }
    }

    fun onChangeToWalletNumber(toWalletNumber: String) {
        _uiState.update {
            it.copy(
                toWalletNumber = toWalletNumber
            )
        }
    }

    fun onChangeMemorableName(memorableName: String) {
        _uiState.update {
            it.copy(
                memorableName = memorableName
            )
        }
    }

    fun onClearAddDialog() {
        _uiState.update {
            it.copy(
                toWalletNumber = "",
                toMerchantName = "",
                memorableName = "",
            )
        }
    }

    fun onSearch() {
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


    fun onDoneWalletNumber(
        onError: (String) -> Unit
    ) {
        if (uiState.value.toWalletNumber.isEmpty()) {
            _uiState.update {
                it.copy(
                    toMerchantName = "",
                )
            }
            onError("Số ví phải gồm 10 chữ số")
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
                    onError(apiResult.message)
                }
            }
        }
    }

    fun onAddSavedReceiver(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
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
                onError(message)
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
            onSuccess()
        }
    }

    fun onDeleteSavedReceiver(
        walletNumber: String,
        onSuccess: () -> Unit
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
            onSuccess()
        }
    }

}