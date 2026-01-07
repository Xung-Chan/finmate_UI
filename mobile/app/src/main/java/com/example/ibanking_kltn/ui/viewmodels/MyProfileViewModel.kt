package com.example.ibanking_kltn.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.repositories.AuthRepository
import com.example.ibanking_kltn.data.repositories.WalletRepository
import com.example.ibanking_kltn.ui.uistates.MyProfileUiState
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
class MyProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val walletRepository: WalletRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyProfileUiState())
    val uiState: StateFlow<MyProfileUiState> = _uiState.asStateFlow()


    fun init(
        onSuccess:(Any?)->Unit,
        onError: (message: String) -> Unit
    ) {
        clearState()
        loadUserInfo(
            onSuccess = onSuccess,
            onError = onError
        )
        loadWalletVerification(

            onError = onError
        )
    }

    fun clearState() {
        _uiState.value = MyProfileUiState()
    }

    fun loadUserInfo(
        onSuccess: (Any?) -> Unit,
        onError: (message: String) -> Unit
    ) {
        var message = ""
        viewModelScope.launch {


            repeat(3) {
                _uiState.update {
                    it.copy(
                        screenState = StateType.LOADING
                    )
                }
                val apiResult = authRepository.getMyProfile()
                when (apiResult) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                screenState = StateType.SUCCESS,
                                userInfo = apiResult.data,
                                initialedUserInfo = true

                            )
                        }
                        onSuccess(apiResult.data.avatarUrl)
                        return@launch
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                screenState = StateType.FAILED(apiResult.message),
                            )
                        }
                        message = apiResult.message
                    }
                }

            }
            _uiState.update {
                it.copy(
                    screenState = StateType.FAILED(message),
                    initialedUserInfo = true
                )
            }
            onError(message)
        }

    }

    fun loadWalletVerification(
        onError: (String) -> Unit
    ) {
        var message=""

        viewModelScope.launch {



            repeat(3) {
                _uiState.update {
                    it.copy(
                        screenState = StateType.LOADING
                    )
                }
                val apiResult = walletRepository.getMyVerification()
                when (apiResult) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                screenState = StateType.SUCCESS,
                                isVerified = true,
                                initialVerification = true
                            )
                        }
                        return@launch
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                screenState = StateType.FAILED(apiResult.message),
                                isVerified = false,
                                )
                        }
                        message=apiResult.message

                    }
                }

            }
            _uiState.update {
                it.copy(
                    screenState = StateType.FAILED(message),
                    initialVerification =true,
                )
            }
        }

    }

    fun onUpdateImageProfile(uri: Uri, onSuccess: (uri: Any) -> Unit, onError: (message: String) -> Unit) {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }

        viewModelScope.launch {
            val apiResult = authRepository.updateAvatar(imageUrl = uri)
            when (apiResult) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS,
                            userInfo = uiState.value.userInfo?.copy(
                                avatarUrl = apiResult.data.imageUrl
                            )
                        )
                    }
                    onSuccess(apiResult.data.imageUrl)
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS,
                            userInfo = uiState.value.userInfo?.copy(
                                avatarUrl = uri
                            )
                        )
                    }
                    onSuccess(uri)
                }
            }
        }
    }
}