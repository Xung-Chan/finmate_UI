// src/enum/status.ts
export const UserStatus = {
    ACTIVE: "ACTIVE",
    LOCKED: "LOCKED",
    UNACTIVE: "UNACTIVE",
} as const;

export type UserStatus =
    (typeof UserStatus)[keyof typeof UserStatus];

export const USER_STATUS_LABEL = {
    ACTIVE: "Hoạt động",
    LOCKED: "Bị khóa",
    UNACTIVE: "Chưa kích hoạt",
} as const;

export type UserStatusLabel = (typeof USER_STATUS_LABEL)[keyof typeof USER_STATUS_LABEL];
export const RoleStatus = {
    ADMIN: "ADMIN",
    STAFF: "STAFF",
    USER: "USER",
} as const;

export type RoleStatus =
    (typeof RoleStatus)[keyof typeof RoleStatus];

export const WalletStatus = {
    ACTIVE: "ACTIVE",
    SUSPENDED: "SUSPENDED",
    PENDING: "PENDING",
} as const;

export type WalletStatus =
    (typeof WalletStatus)[keyof typeof WalletStatus];

export const VerificationStatus = {
    PENDING: "PENDING",
    APPROVED: "APPROVED",
    REJECTED: "REJECTED",
    CANCELLED: "CANCELED"
} as const
export type VerificationStatus =
    (typeof VerificationStatus)[keyof typeof VerificationStatus]
export type PayLaterApplicationStatus = VerificationStatus
export const TransactionStatus = {
    PENDING: "PENDING",
    COMPLETED: "COMPLETED",
    PROCESSING: "PROCESSING",
    FAILED: "FAILED",
    CANCELED: "CANCELED"
} as const;

export type TransactionStatus =
    (typeof TransactionStatus)[keyof typeof TransactionStatus];

export const ActionType = {
    CASH_DEPOSIT: "CASH_DEPOSIT",
    CASH_WITHDRAW: "CASH_WITHDRAW",
    TRANSFER: "TRANSFER",
    BILL_PAYMENT: "BILL_PAYMENT",
    E_GATEWAY_DEPOSIT: "E_GATEWAY_DEPOSIT",
    PAY_LATER_REPAYMENT: "PAY_LATER_REPAYMENT",
    BILL_UTILITY_PAYMENT: "BILL_UTILITY_PAYMENT"
} as const;

export type ActionType =
    (typeof ActionType)[keyof typeof ActionType];

export const PayLaterApplicationType = {
    ACTIVATION: "ACTIVATION", // Kích hoạt thẻ PayLater
    LIMIT_ADJUSTMENT: "LIMIT_ADJUSTMENT", // Điều chỉnh hạn mức tín dụng
    SUSPEND_REQUEST: "SUSPEND_REQUEST" // Yêu cầu tạm ngưng thẻ PayLater
} as const;

export type PayLaterApplicationType =
    (typeof PayLaterApplicationType)[keyof typeof PayLaterApplicationType];