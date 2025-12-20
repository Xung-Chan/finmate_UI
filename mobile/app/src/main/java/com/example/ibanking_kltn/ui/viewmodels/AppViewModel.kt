package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.di.ServiceManager
import com.example.ibanking_kltn.data.dtos.ServiceCategory
import com.example.ibanking_kltn.data.dtos.ServiceItem
import com.example.ibanking_kltn.ui.uistates.SnackBarState
import com.example.ibanking_kltn.utils.SnackBarType
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class AppViewModel @Inject constructor(
    private val serviceManager: ServiceManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(SnackBarState())
    val uiState: StateFlow<SnackBarState> = _uiState.asStateFlow()

    private var snackBarJob: Job? = null
    fun closeSnackBarMessage() {
        snackBarJob?.cancel()
        _uiState.value = SnackBarState()
    }

    fun showSnackBarMessage(
        message: String,
        type: SnackBarType,
        actionLabel: String? = "Đóng",
        onAction: () -> Unit = {
            closeSnackBarMessage()
        }
    ) {
        snackBarJob?.cancel()
        _uiState.update {
            it.copy(

                isVisible = true,
                message = message,
                type = type,
                actionLabel = actionLabel,
                onAction = onAction

            )
        }
        snackBarJob = viewModelScope.launch {
            delay(3000L)
            closeSnackBarMessage()
        }
    }

    fun addRecentService(service: ServiceCategory) {
        serviceManager.addRecentService(ServiceItem(
            service = service.name,
            lastUsed = System.currentTimeMillis()
        ))
    }

}