// types/transaction.type.ts
import { TransactionStatus, ActionType } from "@/enum/status";
import type {PaginationResponse} from "./baseresponse";
export interface TransactionResource {
  id: string;
  sourceAccountNumber?: string;
  toWalletNumber?: string;
  amount?: number;
  sourceBalanceUpdated?: number;
  destinationBalanceUpdated?: string;
  description?: string;
  status?: TransactionStatus;
  metaData?: string;
  processedAt?: string;
}

export interface FilterTransactions {
  accountNumber?: string;
  accountType?: "WALLET" | "PAY_LATER";
  type?: ActionType;
  fromDate?: string;
  toDate?: string;
  sortBy?: string;
}


export interface CreateCashTransaction {
  actionType: ActionType;
  amount: number;
  sourceWalletNumber: string;
}

export type TransactionStatisticsResponse = {
  previousPeriodTransactions?: number;
  totalTransactions?: number;
  previousPeriodValue?: number;
  totalValue?: number;
  previousPeriodFailedRate?: number;
  failedRate?: number;
};
export interface TrendStatisticsRequest {
  fromDate?: string;
  toDate?: string;
}
export type TrendStatistics = {
  date?: string;
  totalTransactions?: number;
  totalValue?: number;
};

export interface DistributionStatisticsRequest {
  fromDate?: string;
  toDate?: string;
}
export type DistributionStatistics = {
  label?: string;
  expenseTag?: number;
  expenseName?: string;
  totalTransactions?: number;
  totalValue?: number;
};

export interface TopUsersStatisticsRequest {
  fromDate?: string;
  toDate?: string;
  accountType?: "WALLET" | "PAY_LATER";
}
export type TopUsersStatistics = {
  username?: string;
  accountNumber?: string;
  fullName?: string;
  avatarUrl?: string;
  totalValue?: number;
  transactionCount?: number;
};
export type TransactionPaginationResponse = PaginationResponse<TransactionResource>;
export type CreateCashTransactionResponse = {
  transactionId: string;
};