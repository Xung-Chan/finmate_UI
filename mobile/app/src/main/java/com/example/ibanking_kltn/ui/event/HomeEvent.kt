package com.example.ibanking_kltn.ui.event

import com.example.ibanking_kltn.data.dtos.ServiceCategory
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class HomeEvent {
    object ChangeVisibilityBalance : HomeEvent()
    object RetryLoadUserInfo : HomeEvent()
    data class ClickService(val service: ServiceCategory) : HomeEvent()
    object NavigateToAllServiceScreen : HomeEvent()
}

sealed class HomeEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : HomeEffect()
    data class NavigateToServiceScreen(val service: ServiceCategory) : HomeEffect()
    object NavigateToAllServiceScreen : HomeEffect()
}