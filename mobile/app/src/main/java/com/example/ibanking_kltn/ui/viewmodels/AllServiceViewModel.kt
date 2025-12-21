package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ibanking_kltn.data.di.ServiceManager
import com.example.ibanking_kltn.data.dtos.ServiceCategory
import com.example.ibanking_kltn.ui.uistates.AllServiceUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class AllServiceViewModel @Inject constructor(
    private val serviceManager: ServiceManager,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(AllServiceUiState())
    val uiState: StateFlow<AllServiceUiState> = _uiState.asStateFlow()

    fun init() {
        loadFavoriteServices()
    }
    private fun loadFavoriteServices() {
        val savedServices = serviceManager.getFavoriteServices()
        val favoriteServices = savedServices.map { service ->
            ServiceCategory.valueOf(service.service)
        }
        _uiState.update {
            it.copy(
                favoriteServices =favoriteServices
            )
        }
    }

}