package com.example.ibanking_kltn.ui.screens.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.ibanking_kltn.data.repositories.NotificationRepository
import com.example.ibanking_kltn.dtos.definitions.NotificationType
import com.example.ibanking_kltn.ui.paging_sources.NotificationPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

sealed class NotificationEvent {
    object ChangeType : NotificationEvent()
}

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val notificationPager = uiState
        .map {
            it.selectedType
        }
        .distinctUntilChanged()
        .flatMapLatest { type ->
            Pager(
                PagingConfig(
                    pageSize = 10,
                    prefetchDistance = 1
                )
            ) {
                NotificationPagingSource(
                    api = notificationRepository,
                    notificationType = type
                )
            }.flow.cachedIn(viewModelScope)
        }


    fun clearState() {
        _uiState.value = NotificationUiState()
    }

    fun onEvent(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.ChangeType -> {
                onChangeType()
            }
        }
    }

    private fun onChangeType() {
        _uiState.update {
            it.copy(
                selectedType = when (it.selectedType) {
                    NotificationType.SYSTEM -> NotificationType.PERSONAL
                    NotificationType.PERSONAL -> NotificationType.SYSTEM
                }
            )
        }
    }


}