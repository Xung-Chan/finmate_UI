package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.NotificationType


data class NotificationUiState(
    val screenState: StateType = StateType.NONE,

    val selectedType: NotificationType = NotificationType.SYSTEM


)
