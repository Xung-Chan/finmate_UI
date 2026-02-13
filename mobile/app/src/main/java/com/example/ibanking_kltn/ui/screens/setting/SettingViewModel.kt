package com.example.ibanking_kltn.ui.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.di.BiometricManager
import com.example.ibanking_kltn.data.di.TokenManager
import com.example.ibanking_kltn.data.repositories.AuthRepository
import com.example.ibanking_kltn.data.session.UserSession
import com.example.ibanking_kltn.dtos.requests.RegisterBiometricRequest
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class SettingViewModel @Inject constructor(
    private val biometricManager: BiometricManager,
    private val authRepository: AuthRepository,
    private val userSession: UserSession,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<SettingEffect>()
    val uiEffect: MutableSharedFlow<SettingEffect> = _uiEffect

    init {
        loadBiometricStatus()
        viewModelScope.launch {
            userSession.user.collect { user ->

                if (user?.profile?.fullName != null) {
                    _uiState.update {
                        it.copy(
                            fullName = user.profile.fullName,
                        )
                    }

                }
                if (user?.profile?.avatarUrl != null) {
                    _uiState.update {
                        it.copy(
                            avatarUrl = user.profile.avatarUrl
                        )
                    }
                }

            }
        }
    }

    fun onEvent(event: SettingEvent) {
        when (event) {
            SettingEvent.SwitchBiometric -> onSwitchBiometric()
            SettingEvent.NavigateToChangePasswordScreen -> navigateToChangePasswordScreen()
            SettingEvent.NavigateToMyProfile -> navigateToMyProfile()
            SettingEvent.Logout -> onLogout()
            is SettingEvent.ChangeConfirmPassword -> onChangePasswordConfirm(event.confirmPassword)
        }
    }


    private fun clearState() {
        _uiState.value = SettingUiState()
    }


    private fun onLogout() {
        tokenManager.clearToken()
        userSession.clear()
        viewModelScope.launch {
            _uiEffect.emit(
                SettingEffect.Logout
            )

        }
    }

    private fun navigateToChangePasswordScreen() {
        viewModelScope.launch {
            _uiEffect.emit(
                SettingEffect.NavigateToChangePasswordScreen
            )
        }
    }

    private fun navigateToMyProfile() {
        viewModelScope.launch {
            _uiEffect.emit(
                SettingEffect.NavigateToMyProfile
            )
        }
    }

    private fun loadBiometricStatus() {
        _uiState.update {
            it.copy(
                isEnableBiometric = biometricManager.getBiometricKey() != null
            )
        }
    }

    private fun onSwitchBiometric(
    ) {
        if (uiState.value.confirmPassword.isEmpty()) {
            viewModelScope.launch {
                _uiEffect.emit(
                    SettingEffect.ShowSnackBar(
                        snackBar = SnackBarUiState(
                            message = "Vui lòng nhập mật khẩu để xác nhận",
                            type = SnackBarType.INFO
                        )
                    )
                )
            }
            return
        }


        if (uiState.value.isEnableBiometric) {

            _uiState.update {
                it.copy(
                    screenState = StateType.LOADING
                )
            }

            viewModelScope.launch {
                var deviceId = biometricManager.getDeviceToken()
                if (deviceId == null) {
                    deviceId = biometricManager.generateDeviceToken()
                }
                val request = RegisterBiometricRequest(
                    deviceId = deviceId,
                    password = uiState.value.confirmPassword,
                )
                val apiResult = authRepository.cancelBiometric(
                    request = request
                )
                when (apiResult) {
                    is ApiResult.Success -> {
                        biometricManager.clear()
                        _uiState.update {
                            it.copy(
                                isEnableBiometric = false,
                                screenState = StateType.SUCCESS,
                                confirmPassword = ""

                            )
                        }
                        _uiEffect.emit(
                            SettingEffect.ShowSnackBar(
                                snackBar = SnackBarUiState(
                                    message = "Huỷ đăng ký vân sinh trắc học thành công",
                                    type = SnackBarType.SUCCESS
                                )
                            )
                        )
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                screenState = StateType.FAILED(apiResult.message)
                            )
                        }
                        _uiEffect.emit(
                            SettingEffect.ShowSnackBar(
                                snackBar = SnackBarUiState(
                                    message = apiResult.message,
                                    type = SnackBarType.ERROR
                                )
                            )
                        )
                    }
                }
            }

        } else {
            _uiState.update {
                it.copy(
                    screenState = StateType.LOADING
                )
            }

            viewModelScope.launch {
                var deviceId = biometricManager.getDeviceToken()
                if (deviceId == null) {
                    deviceId = biometricManager.generateDeviceToken()
                }
                val request = RegisterBiometricRequest(
                    deviceId = deviceId,
                    password = uiState.value.confirmPassword,
                )
                val apiResult = authRepository.registerBiometric(
                    request = request
                )
                when (apiResult) {
                    is ApiResult.Success -> {
                        biometricManager.setBioBiometricKey(
                            biometricKey = apiResult.data.biometricKey
                        )
                        _uiState.update {
                            it.copy(
                                screenState = StateType.SUCCESS,
                                isEnableBiometric = true,
                                confirmPassword = ""
                            )
                        }
                        _uiEffect.emit(
                            SettingEffect.ShowSnackBar(
                                snackBar = SnackBarUiState(
                                    message = "Đăng ký vân sinh trắc học thành công",
                                    type = SnackBarType.SUCCESS
                                )
                            )
                        )
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                screenState = StateType.FAILED(apiResult.message)
                            )
                        }
                        _uiEffect.emit(
                            SettingEffect.ShowSnackBar(
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

    fun onChangePasswordConfirm(newPassword: String) {
        _uiState.update {
            it.copy(confirmPassword = newPassword)
        }
    }

}