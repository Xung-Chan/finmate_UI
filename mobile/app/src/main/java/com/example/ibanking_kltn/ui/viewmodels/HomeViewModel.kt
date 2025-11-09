package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.repositories.WalletRepository
import com.example.ibanking_kltn.ui.uistates.HomeUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    @ApplicationContext private val context: Context
) : ViewModel(), IViewModel {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    override fun error(message: String) {
        _uiState.update {
            it.copy(state = StateType.FAILED(message = message))
        }
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

     fun init() {
        clearState()
        loadWalletInfo()
    }

    override fun clearState() {
        _uiState.value = HomeUiState()
    }

    fun loadWalletInfo() {
        _uiState.update {
            it.copy(state = StateType.LOADING)
        }
        viewModelScope.launch {
            val apiResult = walletRepository.getMyWalletInfor()
            when (apiResult) {
                is ApiResult.Success -> {
                    val walletResponse = apiResult.data
                    _uiState.update {
                        it.copy(
                            state = StateType.SUCCESS,
                            myWallet = walletResponse
                        )
                    }
                }

                is ApiResult.Error -> {
                    error(apiResult.message)
                }
            }
        }
    }

    fun onChangeVisibleBalance() {
        if (!uiState.value.isBalanceShow) loadWalletInfo()

        _uiState.update {
            it.copy(isBalanceShow = !it.isBalanceShow)
        }
    }
}