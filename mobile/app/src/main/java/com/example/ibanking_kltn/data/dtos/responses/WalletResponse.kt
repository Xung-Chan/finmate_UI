package com.example.ibanking_kltn.data.dtos.responses

import com.example.ibanking_kltn.data.dtos.VerificationStatus

data class WalletResponse(
    val id: String,
    val mail: Any,
    val balance: Double,
    val merchantName: String,
    val status: String,
    val username: String,
    val walletNumber: String,
    val verified: Boolean
)

data class WalletVerificationResponse(
    val businessAddress: String,
    val businessCode: String,
    val businessName: String,
    val contactEmail: String,
    val contactPhone: String,
    val createdAt: String,
    val id: String,
    val invoiceDisplayName: String,
    val processedAt: Any,
    val processedBy: Any,
    val representativeIdNumber: String,
    val representativeIdType: String,
    val representativeName: String,
    val status: VerificationStatus,
    val verifiedDocuments: String,
    val walletNumber: Any
)