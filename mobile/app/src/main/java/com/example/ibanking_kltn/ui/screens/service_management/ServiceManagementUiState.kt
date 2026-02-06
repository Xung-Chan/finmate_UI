package com.example.ibanking_kltn.ui.screens.service_management

import com.example.ibanking_kltn.dtos.definitions.ServiceCategory
import com.example.ibanking_kltn.ui.uistates.StateType

data class ServiceManagementUiState(
    val screenState: StateType = StateType.NONE,
    val isModifyFavorite: Boolean = false,

    val favoriteServices: List<ServiceCategory> = emptyList()

)
