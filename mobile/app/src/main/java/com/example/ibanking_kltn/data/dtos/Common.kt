package com.example.ibanking_kltn.data.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class AccountType {
    WALLET,
    PAY_LATER
}

enum class Service(serviceName: String) {
    NONE("Không có"),
    TRANSFER("Chuyển tiền"),
    PAY_BILL("Thanh toán hóa đơn"),
}

enum class TabNavigation {
    HOME,
    WALLET,
    ANALYTICS,
    PROFILE
}

enum class TransactionStatus {
    PENDING,
    COMPLETED,
    PROCESSING,
    FAILED,
    CANCELED
}


enum class QRType {
    BILL,
    TRANSFER
}

@Serializable
sealed class QRPayload{
    abstract  val  qrType: String
}


@Serializable
data class BillPayload(
    override val qrType: String = QRType.BILL.name,
    val billCode: String
) : QRPayload()
@Serializable
data class TransferPayload(
    override val qrType: String = QRType.TRANSFER.name,
    val toWalletNumber: String,
    val amount: Long?,
    val description: String?,
    val expenseType: String?
) : QRPayload()
