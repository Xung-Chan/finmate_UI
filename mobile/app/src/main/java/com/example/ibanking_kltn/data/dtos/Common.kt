package com.example.ibanking_kltn.data.dtos

enum class  AccountType {
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
