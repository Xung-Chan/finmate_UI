package com.example.ibanking_kltn.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.di.ServiceManager
import com.example.ibanking_kltn.data.dtos.ServiceCategory
import com.example.ibanking_kltn.data.dtos.ServiceItem
import com.example.ibanking_kltn.ui.event.AllServiceEffect
import com.example.ibanking_kltn.ui.event.AllServiceEvent
import com.example.ibanking_kltn.ui.uistates.AllServiceUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AllServiceViewModel @Inject constructor(
    private val serviceManager: ServiceManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AllServiceUiState())
    val uiState: StateFlow<AllServiceUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<AllServiceEffect>()
    val uiEffect = _uiEffect.asSharedFlow()


    init {
        loadFavoriteServices()
    }

    fun onEvent(event: AllServiceEvent) {
        when (event) {
            is AllServiceEvent.SaveFavoriteServices -> onSaveFavoriteServices(event.services)
            AllServiceEvent.ChangeModifyFavorite -> onChangeModifyFavorite()
            is AllServiceEvent.NavigateToServiceScreen -> {
                viewModelScope.launch {
                    _uiEffect.emit(
                        AllServiceEffect.NavigateToServiceScreen(event.service)
                    )
                }
            }
        }
    }


    private fun loadFavoriteServices() {
        viewModelScope.launch {
            serviceManager.favoriteServices.collect { savedServices ->
                val favoriteServices = savedServices.map { service ->
                    ServiceCategory.valueOf(service.service)
                }
                _uiState.update {
                    it.copy(
                        favoriteServices = favoriteServices
                    )
                }
            }
        }

    }

    private fun onChangeModifyFavorite() {
        _uiState.update {
            it.copy(
                isModifyFavorite = true
            )
        }
    }

    private fun onSaveFavoriteServices(services: List<ServiceCategory>) {
        val serviceItems = services.map { service ->
            ServiceItem(
                service = service.name,
                lastUsed = System.currentTimeMillis()
            )
        }
        serviceManager.updateFavorite(serviceItems)
        _uiState.update {
            it.copy(
                isModifyFavorite = false,
                favoriteServices = services
            )
        }
    }

}