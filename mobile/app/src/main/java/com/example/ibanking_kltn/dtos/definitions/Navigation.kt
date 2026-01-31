package com.example.ibanking_kltn.dtos.definitions

enum class Screens {
    SignIn, ChangePassword, ChangePasswordSuccess, ForgotPassword,
    Notification,
    Home, Settings, Analytic,
    TransactionResult, Transfer, ConfirmPayment,
    MyProfile, TermAndConditions,
    PayBill, CreateBill, BillHistory, BillDetail,
    TransactionHistory, TransactionHistoryDetail,
    Deposit, HandleDepositResult,
    QRScanner,
    AllService,
    VerificationRequest,
    SavedReceiver,
    PayLater, PayLaterApplication, PayLaterApplicationHistory, BillingCycle
}

enum class AppGraph {
    SignInGraph,
    ChangePasswordGraph,
    MyProfileGraph,
    DepositGraph,
    HomeGraph,
}

enum class NavKey{
    CONFIRM_CONTENT,
    TRANSACTION_ID
}