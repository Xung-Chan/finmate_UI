import type { PaginationResponse } from "./baseresponse";
import type { WalletStatus, VerificationStatus, PayLaterApplicationStatus, PayLaterApplicationType } from "@/enum/status";
export interface WalletResource {
    id: string;
    walletNumber?: string;
    merchantName?: string;
    username?: string;
    status: WalletStatus;
    balance?: number;
    mail?: string;
    verified?: boolean;
}
export interface FilterWallets {
    keyword?: string;
    status?: WalletStatus;
    sort_by?: string;
}

export interface WalletVerificationResource {
  id: string;

  walletNumber?: string;

  /* ===== Business Info ===== */
  invoiceDisplayName?: string;
  businessName?: string;
  businessCode?: string;
  businessAddress?: string;

  /* ===== Representative ===== */
  representativeName?: string;
  representativeIdType?: string;
  representativeIdNumber?: string;

  /* ===== Contact ===== */
  contactEmail?: string;
  contactPhone?: string;

  /* ===== Verification ===== */
  status: VerificationStatus;

  verifiedDocuments?: string[];

  createdAt?: string;
  processedAt?: string;
  processedBy?: string;
}

export interface FilterWalletVertifications {
    keyword?: string;
    status?: VerificationStatus;
    sort_by?: string;
}

export interface ProcessWalletVerificationRequest {
    requestId: string;
    status: VerificationStatus;
}

export interface UpdateWalletStatusRequest {
    walletNumber: string;
    status: WalletStatus;
}

export type WalletPaginationResponse = PaginationResponse<WalletResource>;
export type WalletResponse = WalletResource;
export type WalletsVerificationPaginationResponse = PaginationResponse<WalletVerificationResource>; 
export type WalletVerificationResponse = WalletVerificationResource;
export type ProcessWalletVerificationResponse = WalletVerificationResource;
export type UpdateWalletStatusResponse = {
    message: string;
};
export interface PayLaterResource {
    id: string;
    approvedAt?: string;
    approvedBy?: string;
    username?: string;
    walletNumber?: string;
    payLaterAccountNumber?: string;
    usedCredit?: number;
    creditLimit?: number;
    nextBillingDate?: string;
    nextDueDate?: string;
    status?: WalletStatus;
    interestRate?: number;

}

export interface PayLaterApplicationResource {
    id: string;
    username?: string;
    type?: PayLaterApplicationType;
    requestedCreditLimit?: number;
    approvedLimit?: number;
    reason?: string;
    rejectionReason?: string;
    status: PayLaterApplicationStatus;
    approvedBy?: string;
    appliedAt?: string;
    processedAt?: string;
}

export interface FilterPayLatersApplications  {
    fromDate?: string;
    toDate?: string;
    status?: PayLaterApplicationStatus;
    type?: PayLaterApplicationType;
}
export interface FilterPayLaters extends FilterWallets {}

export interface ProcessPaylaterApplicationRequest {
    id: string;
    action: "APPROVE" | "REJECT";
}
export type PayLaterPaginationResponse = PaginationResponse<PayLaterResource>;
export type PayLaterApplicationPaginationResponse = PaginationResponse<PayLaterApplicationResource>;
export type PayLaterResponse = PayLaterResource;
export type ProcessPaylaterApplicationResponse = PayLaterApplicationResource;

