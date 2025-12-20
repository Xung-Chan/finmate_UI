// types/transaction.type.ts
export type TransactionStatus = "success" | "failed" | "pending";

export type TransactionType = "DEPOSIT" | "PAYMENT" | "REFUND";

export interface Transaction {
  id: string;
  userName: string;
  amount?: number;
  type?: TransactionType;
  status?: TransactionStatus;
  createdAt?: string;
}
