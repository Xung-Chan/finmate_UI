package com.example.ibanking_kltn.data.dtos.responses

data class WalletResponse(
    val id: String,
    val mail: Any,
    val balance: Double?,
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
    val status: String,
    val verifiedDocuments: List<String>,
    val walletNumber: Any
)