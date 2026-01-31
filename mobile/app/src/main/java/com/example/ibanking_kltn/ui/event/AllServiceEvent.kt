package com.example.ibanking_kltn.ui.event

import com.example.ibanking_kltn.dtos.definitions.ServiceCategory
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState


sealed class AllServiceEvent {
    data class SaveFavoriteServices(val services: List<ServiceCategory>): AllServiceEvent()
    data class NavigateToServiceScreen(val service: ServiceCategory): AllServiceEvent()
    object ChangeModifyFavorite: AllServiceEvent()
}

sealed class AllServiceEffect {
    data class ShowSnackBar(val snackBar: SnackBarUiState) : AllServiceEffect()
    data class NavigateToServiceScreen(val service: ServiceCategory) : AllServiceEffect()
}