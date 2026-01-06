import type { AxiosResponse } from "axios";
import httpClient from "@/config/axiosConfig";
import API_ROUTES from "@/config/apiRoutes";

import type {
    FilterTransactions,
    TransactionPaginationResponse,
    CreateCashTransaction,
    CreateCashTransactionResponse,
    TransactionStatisticsResponse,
    TrendStatisticsRequest,
    TrendStatistics,
    DistributionStatisticsRequest,
    DistributionStatistics,
    TopUsersStatisticsRequest,
    TopUsersStatistics,
    
} from "@/types/transaction.type";

export const transactionService = {
    filterTransactions: async (data: FilterTransactions): Promise<TransactionPaginationResponse> => {
        const response: AxiosResponse<TransactionPaginationResponse> = await httpClient.post<TransactionPaginationResponse>(API_ROUTES.transaction.filterTransactions, data);
        return response.data;
    },
    createCashTransaction: async (data: CreateCashTransaction): Promise<CreateCashTransactionResponse> => {
        const response: AxiosResponse<CreateCashTransactionResponse> = await httpClient.post<CreateCashTransactionResponse>(API_ROUTES.transaction.createCash, data);
        return response.data;
    },
    getTransactionStatistics: async (date: string): Promise<TransactionStatisticsResponse> => {
        const response: AxiosResponse<TransactionStatisticsResponse> = await httpClient.get<TransactionStatisticsResponse>(API_ROUTES.transaction.statsTransactions({date} ));
        return response.data;
    },
    trendStatistics: async (data: TrendStatisticsRequest): Promise<TrendStatistics[]> => {
        const response: AxiosResponse<TrendStatistics[]> = await httpClient.post<TrendStatistics[]>(API_ROUTES.transaction.trendsForManage, data);
        return response.data;
    },
    getDistributionStatistics: async (data: DistributionStatisticsRequest): Promise<DistributionStatistics[]> => {
        const response: AxiosResponse<DistributionStatistics[]> = await httpClient.post<DistributionStatistics[]>(API_ROUTES.transaction.distributionForManage, data);
        return response.data;
    },
    getTopUsersStatistics: async (data: TopUsersStatisticsRequest): Promise<TopUsersStatistics[]> => {
        const response: AxiosResponse<TopUsersStatistics[]> = await httpClient.post<TopUsersStatistics[]>(API_ROUTES.transaction.topUsersForManage, data);
        return response.data;
    },  
}