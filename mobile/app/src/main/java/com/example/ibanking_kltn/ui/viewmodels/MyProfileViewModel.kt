package com.example.ibanking_kltn.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.VerificationStatus
import com.example.ibanking_kltn.data.repositories.AuthRepository
import com.example.ibanking_kltn.data.repositories.WalletRepository
import com.example.ibanking_kltn.data.session.UserSession
import com.example.ibanking_kltn.data.usecase.GetMyProfileUC
import com.example.ibanking_kltn.ui.event.MyProfileEffect
import com.example.ibanking_kltn.ui.event.MyProfileEvent
import com.example.ibanking_kltn.ui.uistates.MyProfileUiState
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
class MyProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val walletRepository: WalletRepository,
    private val userSession: UserSession,
    private val getMyProfileUC: GetMyProfileUC
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyProfileUiState())
    val uiState: StateFlow<MyProfileUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<MyProfileEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        retryProfileAndVerification()
        viewModelScope.launch {

            userSession.user.collect { user ->
                _uiState.update {
                    it.copy(
                        userInfo = user?.profile,
                        myWalletNumber = user?.wallet?.walletNumber
                    )
                }
            }
        }
    }

    fun onEvent(event: MyProfileEvent) {
        when (event) {
            MyProfileEvent.RetryProfileAndVerification -> retryProfileAndVerification()
            is MyProfileEvent.UploadAvatar -> onUpdateImageProfile(event.uri)
            MyProfileEvent.SavedQrSuccess -> savedContactSuccess()
            MyProfileEvent.NavigateVerifyRequest -> navigateToVerificationRequest()
        }
    }

    fun clearState() {
        _uiState.value = MyProfileUiState()
    }

    private fun retryProfileAndVerification() {
        _uiState.update {
            it.copy(
                initState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            val profile = getMyProfileUC()
            val verification = walletRepository.getMyVerificationStatus()
            if (profile is ApiResult.Error) {
                _uiState.update {
                    it.copy(
                        initState = StateType.FAILED(message = profile.message)
                    )
                }
                _uiEffect.emit(
                    MyProfileEffect.ShowSnackBar(
                        snackBar = SnackBarUiState(
                            message = profile.message,
                            type = SnackBarType.ERROR
                        )
                    )
                )
                return@launch
            }
            if (verification is ApiResult.Error) {
                _uiState.update {
                    it.copy(
                        initState = StateType.SUCCESS,
                        isVerified = false
                    )
                }
                return@launch
            }
            _uiState.update {
                it.copy(
                    initState = StateType.SUCCESS,
                    isVerified = true
                )
            }

        }

    }

    private fun onUpdateImageProfile(
        uri: Uri,
    ) {
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
                                avatarUrl = apiResult.data.avatarUrl
                            )
                        )
                    }

                    userSession.setUser(
                        user = userSession.user.value!!.copy(
                            profile = userSession.user.value?.profile?.copy(
                                avatarUrl = apiResult.data.avatarUrl
                            )
                        )
                    )
                    _uiEffect.emit(
                        MyProfileEffect.ShowSnackBar(
                            snackBar = SnackBarUiState(
                                message = "Cập nhật ảnh đại diện thành công",
                                type = SnackBarType.SUCCESS
                            )
                        )
                    )
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.FAILED(message = apiResult.message),
                        )
                    }
                    _uiEffect.emit(
                        MyProfileEffect.ShowSnackBar(
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

    private fun savedContactSuccess() {
        viewModelScope.launch {
            _uiEffect.emit(
                MyProfileEffect.ShowSnackBar(
                    snackBar = SnackBarUiState(
                        message = "Lưu thông tin liên hệ thành công",
                        type = SnackBarType.SUCCESS
                    )
                )
            )
        }
    }

    private fun navigateToVerificationRequest() {
        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }
        viewModelScope.launch {
            val verification = walletRepository.getMyVerification()
            when (verification) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS
                        )
                    }
                    if (verification.data.any { it.status == VerificationStatus.PENDING }) {
                        _uiEffect.emit(
                            MyProfileEffect.ShowSnackBar(
                                snackBar = SnackBarUiState(
                                    message = "Đã có yêu cầu xác thực",
                                    type = SnackBarType.INFO
                                )
                            )
                        )
                    }
                    else {
                        _uiEffect.emit(
                            MyProfileEffect.NavigateToVerificationRequest
                        )
                    }
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS
                        )
                    }
                    _uiEffect.emit(
                        MyProfileEffect.NavigateToVerificationRequest
                    )
                }
            }
        }
    }
}