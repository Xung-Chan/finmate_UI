package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.di.ServiceManager
import com.example.ibanking_kltn.data.di.TokenManager
import com.example.ibanking_kltn.data.dtos.LastLoginUser
import com.example.ibanking_kltn.data.dtos.ServiceCategory
import com.example.ibanking_kltn.data.session.UserSession
import com.example.ibanking_kltn.data.usecase.GetMyProfileUC
import com.example.ibanking_kltn.data.usecase.GetMyWalletUC
import com.example.ibanking_kltn.ui.event.HomeEffect
import com.example.ibanking_kltn.ui.event.HomeEvent
import com.example.ibanking_kltn.ui.uistates.HomeUiState
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
class HomeViewModel @Inject constructor(
    private val serviceManager: ServiceManager,
    private val tokenManager: TokenManager,
    private val getMyProfileUC: GetMyProfileUC,
    private val getMyWalletUC: GetMyWalletUC,
    private val userSession: UserSession

) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<HomeEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        loadFavoriteAndRecentServices()
        retryLoadUserData()
        viewModelScope.launch {

            userSession.user.collect { user ->
                _uiState.update {
                    it.copy(
                        myProfile = user?.profile,
                        myWallet = user?.wallet,
                    )
                }
                if (user?.wallet != null && user.profile != null) {
                    tokenManager.setLastLoginUser(
                        lastLoginUser = LastLoginUser(
                            username = user.wallet.username,
                            fullName = user.wallet.merchantName,
                        )
                    )
                }
            }
        }

    }


    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.ChangeVisibilityBalance -> onChangeVisibleBalance()
            HomeEvent.RetryLoadUserInfo -> retryLoadUserData()
            is HomeEvent.ClickService -> onSelectService(event.service)
        }
    }

    fun clearState() {
        _uiState.value = HomeUiState()
    }

    private fun onSelectService(service: ServiceCategory) {
        viewModelScope.launch {
            _uiEffect.emit(
                HomeEffect.NavigateToServiceScreen(
                    service = service
                )
            )
        }
    }

    private fun retryLoadUserData() {
        _uiState.update {
            it.copy(
                initState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            val profile = getMyProfileUC()
            val wallet = getMyWalletUC()
            if (profile is ApiResult.Error) {
                _uiState.update {
                    it.copy(
                        initState = StateType.FAILED(profile.message)
                    )
                }
                _uiEffect.emit(
                    HomeEffect.ShowSnackBar(
                        snackBar = SnackBarUiState(
                            message = profile.message,
                            type = SnackBarType.ERROR
                        )
                    )
                )
                return@launch
            }
            if (wallet is ApiResult.Error) {
                _uiState.update {
                    it.copy(
                        initState = StateType.FAILED(wallet.message)
                    )
                }
                _uiEffect.emit(
                    HomeEffect.ShowSnackBar(
                        snackBar = SnackBarUiState(
                            message = wallet.message,
                            type = SnackBarType.ERROR
                        )
                    )
                )
                return@launch
            }
            _uiState.update {
                it.copy(
                    initState = StateType.SUCCESS
                )
            }
        }
    }

    private fun loadFavoriteAndRecentServices() {
        viewModelScope.launch {

            serviceManager.favoriteServices.collect { favoriteServices ->
                _uiState.update {
                    it.copy(
                        favoriteServices = favoriteServices
                    )
                }
            }
        }
        viewModelScope.launch {

            serviceManager.recentServices.collect { recentServices ->
                _uiState.update {
                    it.copy(
                        recentServices = recentServices
                    )
                }
            }
        }
    }

    private fun onChangeVisibleBalance() {
        if (!uiState.value.isBalanceShow) {
            _uiState.update {
                it.copy(
                    state = StateType.LOADING
                )
            }
            viewModelScope.launch {
                val wallet = getMyWalletUC()
                when (wallet) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                state = StateType.SUCCESS
                            )
                        }

                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                state = StateType.FAILED(wallet.message)
                            )
                        }
                        _uiEffect.emit(
                            HomeEffect.ShowSnackBar(
                                snackBar = SnackBarUiState(
                                    message = wallet.message,
                                    type = SnackBarType.ERROR
                                )
                            )
                        )
                    }
                }
            }
        }

        _uiState.update {
            it.copy(isBalanceShow = !it.isBalanceShow)
        }
    }
}