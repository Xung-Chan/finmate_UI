package com.example.ibanking_kltn.dtos.requests

data class WalletVerificationRequest(
    val invoiceDisplayName: String,
    val businessName: String,
    val businessCode: String,
    val businessAddress: String,
    val representativeName: String,
    val representativeIdType: String,
    val representativeIdNumber: String,
    val contactEmail: String,
    val contactPhone: String
)