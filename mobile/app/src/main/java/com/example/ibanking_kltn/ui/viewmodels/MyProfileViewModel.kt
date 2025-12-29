package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.repositories.AuthRepository
import com.example.ibanking_kltn.ui.uistates.MyProfileUiState
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
class MyProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyProfileUiState())
    val uiState: StateFlow<MyProfileUiState> = _uiState.asStateFlow()


    fun init(
        onError: (message: String) -> Unit
    ) {
        clearState()
        loadUserInfo(
            onError = onError
        )
    }

    fun clearState() {
        _uiState.value = MyProfileUiState()
    }

    fun loadUserInfo(
        onError: (message: String) -> Unit
    ) {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    screenState = StateType.LOADING
                )
            }

            repeat(3) {
                val apiResult = authRepository.getMyProfile()
                when (apiResult) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                screenState = StateType.SUCCESS,
                                userInfo = apiResult.data

                            )
                        }
                        return@launch
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                screenState = StateType.FAILED(apiResult.message)
                            )
                        }
                        onError(apiResult.message)
                    }
                }

            }
        }

    }

    fun onUpdateImageProfile(uri: Uri, onSuccess: () -> Unit, onError: (message: String) -> Unit) {
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
                    onSuccess()
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.FAILED(apiResult.message)
                        )
                    }
                    onError(apiResult.message)
                }
            }
        }
    }
}