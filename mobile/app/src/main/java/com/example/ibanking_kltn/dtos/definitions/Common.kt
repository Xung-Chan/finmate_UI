package com.example.ibanking_kltn.dtos.definitions

import androidx.compose.ui.graphics.Color
import com.example.ibanking_kltn.R
import com.example.ibanking_kltn.ui.theme.Blue5
import com.example.ibanking_kltn.ui.theme.Green1
import com.example.ibanking_kltn.ui.theme.Green2
import com.example.ibanking_kltn.ui.theme.Orange1
import com.example.ibanking_kltn.ui.theme.Red1
import com.example.ibanking_kltn.ui.theme.Red2
import kotlinx.serialization.Serializable

enum class AccountType(val type: String, val icon: Int, val color: Color) {
    WALLET(type = "Tài khoản mặc định", icon = R.drawable.wallet_regular, color = Orange1),
    PAY_LATER(type = "Ví trả sau", icon = R.drawable.paylater, color = Green2)
}

data class PaymentAccount(
    val accountType: AccountType,
    val accountNumber: String?,
    val merchantName: String,
    val balance: Long
)


enum class ServiceType(val serviceName: String) {
    TRANSFER("Chuyển tiền"),
    BILL_PAYMENT("Thanh toán hóa đơn"),
    CASH_DEPOSIT("Nạp tiền"),
    CASH_WITHDRAW("Rút tiền"),
    E_GATEWAY_DEPOSIT("Nạp tiền qua ứng dụng"),
    PAY_LATER_REPAYMENT("Thanh toán nợ ví trả sau"),
    BILL_UTILITY_PAYMENT("Thanh toán tiện ích")
}

enum class TabNavigation {
    HOME,
    HISTORY,
    ANALYTICS,
    PROFILE
}


enum class QRType {
    BILL,
    TRANSFER
}

@Serializable
sealed class QRPayload {
    abstract val qrType: String
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
    val amount: Long = 0L,
    val description: String? = null,
    val expenseType: String? = null
) : QRPayload()


enum class BillStatus(val status: String) {
    ACTIVE("Chưa thanh toán"),
    PAID("Đã thanh toán"),
    OVERDUE("Quá hạn"),
    PENDING("Đang xử lý"),
    CANCELED("Đã hủy")
}

enum class TransactionStatus(val status: String) {
    PENDING("Đang xử lý"),
    COMPLETED("Thành công"),
    FAILED("Thất bại"),
    CANCELED("Đã hủy")
}


enum class SortOption(val sortBy: String) {
    NEWEST("Mới nhất"),
    OLDEST("Cũ nhất")
}

data class Pagination<T>(
    val contents: List<T>,
    val currentPage: Int,
    val pageSize: Int,
    val totalElements: Int,
    val totalPages: Int
)

@Serializable
data class ServiceItem(
    val service: String,
    val lastUsed: Long = System.currentTimeMillis(),
)

@Serializable
data class SavedReceiverInfo(
    val memorableName: String,
    val toWalletNumber: String,
    val toMerchantName: String,
)

enum class ServiceCategory(val serviceName: String, val icon: Int, val color: ULong) {
    MONEY_TRANSFER(
        serviceName = "Chuyển tiền",
        icon = R.drawable.money_transfer_service,
        color = Red1.value
    ),
    BILL_PAYMENT(
        serviceName = "Thanh toán hóa đơn",
        icon = R.drawable.pay_bill_service,
        color = Blue5.value
    ),
    DEPOSIT(
        serviceName = "Nạp tiền trực tuyến",
        icon = R.drawable.deposit_service,
        color = Orange1.value
    ),
    PAY_LATER(serviceName = "Ví trả sau", icon = R.drawable.paylater, color = Green2.value),
    BILL_CREATE(
        serviceName = "Tạo hóa đơn",
        icon = R.drawable.bill_create_service,
        color = Green1.value
    ),
    BILL_HISTORY(
        serviceName = "Lịch sử hóa đơn",
        icon = R.drawable.bill_history_service,
        color = Red2.value
    ),
}


@Serializable
data class BiometricStorage(
    val deviceId: String,
    val biometricKey: String,
    val username: String
)

@Serializable
data class LastLoginUser(
    val username: String,
    val fullName: String,
)


enum class PayLaterAccountStatus {
    PENDING,
    ACTIVE,
    SUSPENDED
}


enum class PayLaterApplicationType(val typeName: String) {
    ACTIVATION("Yêu cầu kích hoạt"), // Kích hoạt thẻ PayLater
    LIMIT_ADJUSTMENT("Yêu cầu điều chỉnh hạn mức"), // Điều chỉnh hạn mức tín dụng
    SUSPEND_REQUEST("Tạm khóa") // Yêu cầu tạm ngưng thẻ PayLater
}

enum class PayLaterApplicationStatus(val statusName: String) {
    PENDING("Đang xử lý"),
    APPROVED("Đã duyệt"),
    REJECTED("Bị từ chối"),
    CANCELED("Đã hủy")
}

enum class BillingCycleStatus {
    OPEN, //Chu kỳ đang mở, chưa đến due_date
    OVERDUE, //Đã quá due_date nhưng chưa trả đủ
    PAID, //Đã trả đủ total_amount_due
    PARTIALLY_PAID, //Đã trả >= minimum_payment nhưng < total_amount_due
    CLOSED,  //Chu kỳ đã đóng (admin can thiệp hoặc đặc biệt)
}


enum class MoneyFlowType {
    OUTGOING,
    INCOMING
}

enum class RequestOtpPurpose {
    PASSWORD_RESET,
    EMAIL_VERIFICATION
}

enum class NotificationType {
    SYSTEM,
    PERSONAL,
}

enum class VerificationStatus {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED
}