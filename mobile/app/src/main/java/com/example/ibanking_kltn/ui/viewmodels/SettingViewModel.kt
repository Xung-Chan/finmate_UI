package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.di.BiometricManager
import com.example.ibanking_kltn.data.dtos.requests.RegisterBiometricRequest
import com.example.ibanking_kltn.data.repositories.AuthRepository
import com.example.ibanking_kltn.ui.uistates.SettingUiState
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
class SettingViewModel @Inject constructor(
    private val biometricManager: BiometricManager,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()


    fun init(
    ) {
        clearState()
        loadBiometricStatus()
    }

    fun clearState() {
        _uiState.value = SettingUiState()
    }


    fun loadBiometricStatus() {
        _uiState.update {
            it.copy(
                isEnableBiometric = biometricManager.getBiometricKey() != null
            )
        }
    }

    fun onSwitchBiometric(
        onSuccess: (Boolean) -> Unit,
        onError: (String) -> Unit,
    ) {
        if (uiState.value.isEnableBiometric) {

            //TODO turn off biometric
            biometricManager.clear()
            _uiState.update {
                it.copy(
                    isEnableBiometric = false
                )
            }
            onSuccess(false)
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
                                isEnableBiometric = true
                            )
                        }
                        onSuccess(true)
                    }

                    is ApiResult.Error -> {
                        onError(apiResult.message)
                    }
                }
            }
        }
    }


}