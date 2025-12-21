package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.data.dtos.ServiceCategory

data class AllServiceUiState(
    val screenState: StateType = StateType.NONE,
    val isModifyFavorite: Boolean = false,

    val favoriteServices: List<ServiceCategory> = emptyList()

)
