package com.example.ibanking_kltn.ui.screens.notification

import com.example.ibanking_kltn.dtos.definitions.NotificationType
import com.example.ibanking_kltn.ui.uistates.StateType


data class NotificationUiState(
    val screenState: StateType = StateType.NONE,

    val selectedType: NotificationType = NotificationType.SYSTEM


)
