package com.example.ibanking_kltn.ui.viewmodels


import androidx.lifecycle.ViewModel
import com.example.ibanking_kltn.data.dtos.BillPayload
import com.example.ibanking_kltn.data.dtos.TransferPayload
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class QRScannerViewModel @Inject constructor() : ViewModel() {

    fun onBillDetecting(
        payload: BillPayload,
        naigateToHome: () -> Unit,
    ) {
            naigateToHome()
    }

    fun onTransferDetecting(payload: TransferPayload) {

    }

}