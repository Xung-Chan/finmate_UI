//package com.example.ibanking_kltn.ui.exception
//
//interface ErrorDetail {
//    val code: Int
//    val message: String
//}
//enum class LoginErrorEnum(
//    override val code: Int,
//    override val message: String
//) : ErrorDetail {
//    Unauthorized(401, "Tài khoản hoặc mật khẩu không đúng"),
//    Forbidden(403, "Tài khoản bị khóa"),
//    ServiceError(500, "Dịch vụ xác thực gặp sự cố")
//}
//
//enum class PrepareTransferErrorEnum(
//    override val code: Int,
//    override val message: String
//) : ErrorDetail {
//    NotFound(404, "Số tài khoản không tồn tại"),
//    ServiceError(500, "Dịch vụ ví gặp sự cố")
//
//}
//
//enum class ConfirmTransferErrorEnum(
//    override val code: Int,
//    override val message: String
//) : ErrorDetail {
//
//    NotFound(404, "Giao dịch không tồn tại"),
//    BadRequest(400, "OTP không đúng"),
//    ServiceError(500, "Dịch vụ ví gặp sự cố")
//
//}
//
//enum class GetMyWalletErrorEnum(
//    override val code: Int,
//    override val message: String
//) : ErrorDetail {
//
//    BadRequest(401, "Phiên đăng nhập hết hạn"),
//    ServiceError(500, "Dịch vụ ví gặp sự cố")
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//enum class ChangePasswordErrorEnum(
//    override val code: Int,
//    override val message: String
//) : ErrorDetail {
//    Badrequest(400, "Mật khẩu hiện tại không đúng"),
//    ServiceError(500, "Dịch vụ ví gặp sự cố")
//
//}
