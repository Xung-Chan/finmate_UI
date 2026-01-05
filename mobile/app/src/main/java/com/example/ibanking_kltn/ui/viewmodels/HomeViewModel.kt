package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.di.ServiceManager
import com.example.ibanking_kltn.data.di.TokenManager
import com.example.ibanking_kltn.data.dtos.LastLoginUser
import com.example.ibanking_kltn.data.repositories.AuthRepository
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
    private val authRepository: AuthRepository,
    private val serviceManager: ServiceManager,
    private val tokenManager: TokenManager,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun init(
        onError: (String) -> Unit
    ) {
        clearState()
        loadWalletInfo(
            onError = onError
        )
        loadUserInfo(
            onError = onError
        )
        loadFavoriteAndRecentServices()
    }

     fun clearState() {
        _uiState.value = HomeUiState()
    }

    fun loadFavoriteAndRecentServices() {
        val favoriteServices = serviceManager.getFavoriteServices()
        val recentServices = serviceManager.getRecentServices()

        _uiState.update {
            it.copy(
                favoriteServices = favoriteServices,
                recentServices = recentServices
            )
        }
    }

    fun loadWalletInfo(
        onError: (String) -> Unit
    ) {
        var message = ""
        viewModelScope.launch {

            repeat(3) {
                _uiState.update {
                    it.copy(
                        state = StateType.LOADING
                    )
                }

                val apiResult = walletRepository.getMyWalletInfor()
                when (apiResult) {
                    is ApiResult.Success -> {
                        val walletResponse = apiResult.data
                        _uiState.update {
                            it.copy(
                                state = StateType.SUCCESS,
                                myWallet = walletResponse,
                                initialedUserWallet = true
                            )
                        }
                        tokenManager.setLastLoginUser(
                            lastLoginUser = LastLoginUser(
                                username = walletResponse.username,
                                fullName = walletResponse.merchantName,
                            )
                        )
                        return@launch
                    }

                    is ApiResult.Error -> {
                        message = apiResult.message
                        _uiState.update {
                            it.copy(
                                state = StateType.FAILED(message),
                            )
                        }

                    }
                }
            }
            _uiState.update {
                it.copy(
                    initialedUserWallet = true
                )
            }

            onError(message)
        }
    }
    fun loadUserInfo(
        onError: (String) -> Unit
    ){
        var message = ""
        viewModelScope.launch {


            repeat(3) {
                _uiState.update {
                    it.copy(
                        state = StateType.LOADING
                    )
                }
                val apiResult = authRepository.getMyProfile()
                when (apiResult) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                state = StateType.SUCCESS,
                                myProfile = apiResult.data,
                                initialedUserInfo = true

                            )
                        }
                        return@launch
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                state = StateType.FAILED(apiResult.message),
                            )
                        }
                        message = apiResult.message
                    }
                }

            }
            _uiState.update {
                it.copy(
                    state = StateType.FAILED(message),
                    initialedUserInfo = true
                )
            }
            onError(message)
        }

    }

    fun onChangeVisibleBalance(
        onError: (String) -> Unit
    ) {
        if (!uiState.value.isBalanceShow) loadWalletInfo(
            onError = onError
        )

        _uiState.update {
            it.copy(isBalanceShow = !it.isBalanceShow)
        }
    }
}