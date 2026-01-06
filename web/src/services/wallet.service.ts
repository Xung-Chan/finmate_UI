import type { AxiosResponse } from "axios";
import httpClient from "@/config/axiosConfig";
import API_ROUTES from "@/config/apiRoutes";

import type {
    FilterWallets,
    WalletPaginationResponse,
    WalletResponse,
    FilterWalletVertifications,
    WalletsVerificationPaginationResponse,
    UpdateWalletStatusRequest,
    UpdateWalletStatusResponse,
    FilterPayLaters,
    FilterPayLatersApplications,
    PayLaterPaginationResponse,
    PayLaterResponse,
    ProcessPaylaterApplicationRequest,
    ProcessWalletVerificationRequest,
    ProcessWalletVerificationResponse,
    ProcessPaylaterApplicationResponse,
    PayLaterApplicationPaginationResponse,
} from "@/types/walltet.type";

export const walletService = {
    filterWallets: async (data: FilterWallets): Promise<WalletPaginationResponse> => {
        const response: AxiosResponse<WalletPaginationResponse> = await httpClient.post<WalletPaginationResponse>(API_ROUTES.wallet.filterAccounts, data);
        return response.data;
    },
    getWalletInfo: async (walletNumber: string): Promise<WalletResponse> => {
        const response: AxiosResponse<WalletResponse> = await httpClient.get<WalletResponse>(API_ROUTES.wallet.getWalletInfo(walletNumber));
        return response.data;
    },
    updateWalletStatus: async (data: UpdateWalletStatusRequest): Promise<UpdateWalletStatusResponse> => {
        const response: AxiosResponse<UpdateWalletStatusResponse> = await httpClient.put<UpdateWalletStatusResponse>(API_ROUTES.wallet.updateStatus, data);
        return response.data;
    },

    filterWalletVerifications: async (data: FilterWalletVertifications): Promise<WalletsVerificationPaginationResponse> => {
        const response: AxiosResponse<WalletsVerificationPaginationResponse> = await httpClient.post<WalletsVerificationPaginationResponse>(API_ROUTES.wallet.filterWalletVertifications, data);
        return response.data;
    },
    processWalletVerification: async (data: ProcessWalletVerificationRequest): Promise<ProcessWalletVerificationResponse> => {
        const response: AxiosResponse<ProcessWalletVerificationResponse> = await httpClient.put<ProcessWalletVerificationResponse>(API_ROUTES.wallet.processWalletVerification, data);
        return response.data;
    },

    filterAccountPaylaters: async (data: FilterPayLaters): Promise<PayLaterPaginationResponse> => {
        const response: AxiosResponse<PayLaterPaginationResponse> = await httpClient.post<PayLaterPaginationResponse>(API_ROUTES.wallet.filterAccountsPaylater, data);
        return response.data;
    },
    getPaylaterInfo: async (payLaterAccountNumber: string): Promise<PayLaterResponse> => {
        const response: AxiosResponse<PayLaterResponse> = await httpClient.get<PayLaterResponse>(API_ROUTES.wallet.getPayLaterInfo(payLaterAccountNumber));
        return response.data;
    },
    filterPaylaterApplications: async (data: FilterPayLatersApplications): Promise<PayLaterApplicationPaginationResponse> => {
        const response: AxiosResponse<PayLaterApplicationPaginationResponse> = await httpClient.post<PayLaterApplicationPaginationResponse>(API_ROUTES.wallet.filterPaylaterApplications, data);
        return response.data;
    },
    processPaylaterApplication: async (data: ProcessPaylaterApplicationRequest): Promise<ProcessPaylaterApplicationResponse> => {
        const response: AxiosResponse<ProcessPaylaterApplicationResponse> = await httpClient.put<ProcessPaylaterApplicationResponse>(API_ROUTES.wallet.processPaylaterApplication, data);
        return response.data;
    }
}