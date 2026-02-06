package com.example.ibanking_kltn.ui.screens.wallet.verification

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibanking_kltn.dtos.requests.WalletVerificationRequest
import com.example.ibanking_kltn.data.repositories.WalletRepository
import com.example.ibanking_kltn.ui.uistates.StateType
import com.example.ibanking_kltn.utils.getFileInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val _uiEffect = MutableSharedFlow<CreateVerificationEffect>()
    val uiEffect = _uiEffect.asSharedFlow()


    fun onEvent(event: CreateVerificationEvent) {
        when (event) {
            is CreateVerificationEvent.AddFile -> onAddFile(event.uris)
            is CreateVerificationEvent.ChangeBusinessAddress -> onChangeBusinessAddress(event.address)
            is CreateVerificationEvent.ChangeBusinessCode -> onChangeBusinessCode(event.code)
            is CreateVerificationEvent.ChangeBusinessName -> onChangeBusinessName(event.name)
            is CreateVerificationEvent.ChangeContactEmail -> onChangeContactEmail(event.email)
            is CreateVerificationEvent.ChangeContactPhone -> onChangeContactPhone(event.phone)
            is CreateVerificationEvent.ChangeInvoiceDisplayName -> onChangeInvoiceDisplayName(event.name)
            is CreateVerificationEvent.ChangeRepresentativeIdNumber -> onChangeRepresentativeIdNumber(
                event.id
            )

            is CreateVerificationEvent.ChangeRepresentativeName -> onChangeRepresentativeName(event.name)
            is CreateVerificationEvent.DeleteFile -> onDeleteFile(event.file)
            is CreateVerificationEvent.SelectType -> onSelectType(event.idType)
            CreateVerificationEvent.SubmitVerificationRequest -> onConfirmClick()
        }
    }

    private fun onAddFile(uris: List<Uri>) {
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

    private fun onDeleteFile(file: FileInfo) {
        _uiState.update {
            it.copy(
                documents = it.documents.filter { f -> f.uri != file.uri }
            )
        }
    }

    private fun onConfirmClick(
    ) {

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
            //todo
//            when (apiResult) {
//                is ApiResult.Success -> {
//                    _uiState.update {
//                        it.copy(
//                            screenState = StateType.SUCCESS
//                        )
//                    }
//                    _uiEffect.emit(
//                        CreateVerificationEffect.SubmitSuccess
//
//                    )
//
//                }
//
//                is ApiResult.Error -> {
//                    _uiState.update {
//                        it.copy(
//                            screenState = StateType.SUCCESS
//                        )
//                    }
//                    _uiEffect.emit(
//                        CreateVerificationEffect.ShowSnackBar(
//                            snackBar = SnackBarUiState(
//                                message = apiResult.message,
//                                type = SnackBarType.ERROR
//                            )
//                        )
//                    )
//                }
//            }
            _uiState.update {
                it.copy(
                    screenState = StateType.SUCCESS
                )
            }
            _uiEffect.emit(
                CreateVerificationEffect.SubmitSuccess

            )
        }

    }

    private fun onSelectType(idType: IdType) {
        _uiState.update {
            it.copy(
                representativeIdType = idType.name
            )
        }
    }

    private fun onChangeInvoiceDisplayName(value: String) {
        _uiState.update {
            it.copy(invoiceDisplayName = value)
        }
    }

    private fun onChangeBusinessName(value: String) {
        _uiState.update {
            it.copy(businessName = value)
        }
    }

    private fun onChangeBusinessCode(value: String) {
        _uiState.update {
            it.copy(businessCode = value)
        }
    }

    private fun onChangeBusinessAddress(value: String) {
        _uiState.update {
            it.copy(businessAddress = value)
        }
    }

    private fun onChangeContactEmail(value: String) {
        _uiState.update {
            it.copy(contactEmail = value)
        }
    }

    private fun onChangeContactPhone(value: String) {
        _uiState.update {
            it.copy(contactPhone = value)
        }
    }

    private fun onChangeRepresentativeName(value: String) {
        _uiState.update {
            it.copy(representativeName = value)
        }
    }

    private fun onChangeRepresentativeIdNumber(value: String) {
        _uiState.update {
            it.copy(representativeIdNumber = value)
        }
    }


}