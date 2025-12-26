package com.example.ibanking_kltn.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.data.dtos.requests.WalletVerificationRequest
import com.example.ibanking_kltn.data.repositories.WalletRepository
import com.example.ibanking_kltn.ui.uistates.CreateVerificationRequestUiState
import com.example.ibanking_kltn.ui.uistates.FileInfo
import com.example.ibanking_kltn.ui.uistates.IdType
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.getFileInfo
import com.example.ibanking_soa.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class CreateVerificationRequestViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateVerificationRequestUiState())
    val uiState: StateFlow<CreateVerificationRequestUiState> = _uiState.asStateFlow()


    fun init(
    ) {
        clearState()
    }

    fun clearState() {
        _uiState.value = CreateVerificationRequestUiState()
    }

    fun onAddFile(uris: List<Uri>) {
        val mutableList = emptyList<FileInfo>().toMutableList()
        uris.forEach { uri ->
            val info = context.getFileInfo(uri)
            mutableList.add(
                FileInfo(
                    uri = uri,
                    fileName = info.first ?: "Document",
                    extension = info.second ?: "",
                    size = info.third ?: 0L
                )
            )
        }
        _uiState.update {
            it.copy(
                documents = (it.documents + mutableList).toSet().toList()
            )
        }
    }

    fun onDeleteFile(file: FileInfo) {
        _uiState.update {
            it.copy(
                documents = it.documents.filter { f -> f.uri != file.uri }
            )
        }
    }

    fun onConfirmClick(
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    )  {

        _uiState.update {
            it.copy(
                screenState = StateType.LOADING
            )
        }

        viewModelScope.launch {

            val request = WalletVerificationRequest(
                invoiceDisplayName = _uiState.value.invoiceDisplayName,
                businessName = _uiState.value.businessName,
                businessCode = _uiState.value.businessCode,
                businessAddress = _uiState.value.businessAddress,
                representativeName = _uiState.value.representativeName,
                representativeIdType = _uiState.value.representativeIdType,
                representativeIdNumber = _uiState.value.representativeIdNumber,
                contactEmail = _uiState.value.contactEmail,
                contactPhone = _uiState.value.contactPhone
            )
            val apiResult = walletRepository.createWalletVerification(
                request,
                _uiState.value.documents.map { it.uri }
            )
            when (apiResult) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.SUCCESS
                        )
                    }
                    onSuccess()
                }

                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            screenState = StateType.FAILED(apiResult.message)
                        )
                    }
                    onError(apiResult.message)
                }
            }
        }

    }

    fun onSelectType(idType: IdType) {
        _uiState.update {
            it.copy(
                representativeIdType = idType.name
            )
        }
    }
    fun onChangeInvoiceDisplayName(value: String) {
        _uiState.update {
            it.copy(invoiceDisplayName = value)
        }
    }

    fun onChangeBusinessName(value: String) {
        _uiState.update {
            it.copy(businessName = value)
        }
    }

    fun onChangeBusinessCode(value: String) {
        _uiState.update {
            it.copy(businessCode = value)
        }
    }

    fun onChangeBusinessAddress(value: String) {
        _uiState.update {
            it.copy(businessAddress = value)
        }
    }

    fun onChangeContactEmail(value: String) {
        _uiState.update {
            it.copy(contactEmail = value)
        }
    }

    fun onChangeContactPhone(value: String) {
        _uiState.update {
            it.copy(contactPhone = value)
        }
    }

    fun onChangeRepresentativeName(value: String) {
        _uiState.update {
            it.copy(representativeName = value)
        }
    }

    fun onChangeRepresentativeIdNumber(value: String) {
        _uiState.update {
            it.copy(representativeIdNumber = value)
        }
    }



    fun isEnableConfirm():Boolean{
        val state = _uiState.value
        return state.documents.isNotEmpty()
                && state.invoiceDisplayName.isNotBlank()
                && state.businessName.isNotBlank()
                && state.businessCode.isNotBlank()
                && state.businessAddress.isNotBlank()
                && state.representativeName.isNotBlank()
                && state.representativeIdType.isNotBlank()
                && state.representativeIdNumber.isNotBlank()
                && state.contactEmail.isNotBlank()
                && state.contactPhone.isNotBlank()
    }



}