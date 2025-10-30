package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.ibanking_kltn.data.dtos.requests.LoginRequest
import com.example.ibanking_kltn.data.repositories.AuthRepository
import com.example.ibanking_kltn.ui.Screens
import com.example.ibanking_kltn.ui.uistates.AuthUiState
import com.example.ibanking_kltn.ui.uistates.LoginState
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(username = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update {
            it.copy(password = newPassword)
        }
    }
    fun checkEnableLogin(): Boolean{
        return uiState.value.username!="" && uiState.value.password!=""
    }

    fun onChangeVisiblePassword() {
        _uiState.update {
            it.copy(isPasswordShow = !it.isPasswordShow)
        }
    }

    fun onLoginClick(context: Context, navControler: NavController) {
        _uiState.update {
            it.copy(loginState = LoginState.LOGGING)
        }
        viewModelScope.launch {
            val loginRequest = LoginRequest(uiState.value.username, uiState.value.password)
            val apiResult = authRepository.login(loginRequest)
            when (apiResult) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(loginState = LoginState.SUCCESS(apiResult.data))
                    }
                    Toast.makeText(context,"Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                    navControler.navigate(Screens.Home.name)
                }
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(loginState = LoginState.FAILED(message = apiResult.message))
                    }

                    Toast.makeText(context,(uiState.value.loginState as LoginState.FAILED).message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}