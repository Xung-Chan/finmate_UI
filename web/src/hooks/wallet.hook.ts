import { useNavigate } from "@tanstack/react-router";
import toast from "react-hot-toast";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import type {
    FilterWallets,
    WalletResponse,
    WalletPaginationResponse,
    FilterWalletVertifications,
    WalletsVerificationPaginationResponse,
    FilterPayLaters,
    FilterPayLatersApplications,
    PayLaterPaginationResponse,
    PayLaterApplicationPaginationResponse,
    PayLaterResponse,
    UpdateWalletStatusRequest,
    UpdateWalletStatusResponse,
    ProcessWalletVerificationRequest,
    ProcessWalletVerificationResponse,
    ProcessPaylaterApplicationRequest,
    ProcessPaylaterApplicationResponse,
} from "@/types/walltet.type";
import type { ErrorResponse } from "@/types/error.type";
import { walletService } from "@/services/wallet.service";

export function useFilterWallets(filterParams: FilterWallets) {
    return useQuery<WalletPaginationResponse>({
        queryKey: ['filterWallets', filterParams],
        queryFn: () => walletService.filterWallets(filterParams),
        staleTime: 5 * 60 * 1000,     // 5 phút coi data là "fresh"
        refetchOnWindowFocus: false, // không refetch khi focus lại tab
        refetchOnReconnect: false,   // không refetch khi mạng reconnect
        retry: 1,
    });
}

export function useGetWalletInfo(walletNumber?: string) {
    return useQuery<WalletResponse>({
        queryKey: ['getWalletInfo', walletNumber],
        enabled: !!walletNumber,
        queryFn: () => walletService.getWalletInfo(walletNumber as string),
        staleTime: 5 * 60 * 1000,     // 5 phút coi data là "fresh"
        refetchOnWindowFocus: false, // không refetch khi focus lại tab
        refetchOnReconnect: false,   // không refetch khi mạng reconnect
        retry: 1,
    });
}

export function useUpdateWalletStatus() {
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    return useMutation<UpdateWalletStatusResponse, ErrorResponse, UpdateWalletStatusRequest>({
        mutationFn: (data: UpdateWalletStatusRequest) => walletService.updateWalletStatus(data),
        onSuccess: (data) => {
            toast.success(data.message || "Cập nhật trạng thái ví thành công");
            queryClient.invalidateQueries({ queryKey: ['filterWallets'] });
            navigate({ to: "/manage/wallets-management/wallets" });
        },
        onError: (error: ErrorResponse) => {
            return toast.error(error?.message || "Cập nhật trạng thái ví thất bại");
        },
    }); 
}

export function useProcessWalletVerification() {
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    return useMutation<ProcessWalletVerificationResponse, ErrorResponse, ProcessWalletVerificationRequest>({
        mutationFn: (data: ProcessWalletVerificationRequest) => walletService.processWalletVerification(data),
        onSuccess: () => {
            toast.success("Xử lý xác minh ví thành công");
            queryClient.invalidateQueries({ queryKey: ['filterWalletVerifications'] });
            navigate({ to: "/manage/wallet-verifications" });
        },
        onError: (error: ErrorResponse) => {
            toast.error(error?.message || "Xử lý xác minh ví thất bại");
        },
    });
}
export function useFilterWalletVerifications(filterParams: FilterWalletVertifications) {
    return useQuery<WalletsVerificationPaginationResponse>({
        queryKey: ['filterWalletVerifications', filterParams],
        queryFn: () => walletService.filterWalletVerifications(filterParams),
        staleTime: 5 * 60 * 1000,     // 5 phút coi data là "fresh"
        refetchOnWindowFocus: false, // không refetch khi focus lại tab
        refetchOnReconnect: false,   // không refetch khi mạng reconnect
        retry: 1,
    });
}

export function useFilterPayLaters(filterParams: FilterPayLaters) {
    return useQuery<PayLaterPaginationResponse>({
        queryKey: ['filterPayLaters', filterParams],
        queryFn: () => walletService.filterAccountPaylaters(filterParams),
        staleTime: 5 * 60 * 1000,     // 5 phút coi data là "fresh"
        refetchOnWindowFocus: false, // không refetch khi focus lại tab
        refetchOnReconnect: false,   // không refetch khi mạng reconnect
        retry: 1,
    });
}

export function useFilterPayLaterApplications(filterParams: FilterPayLatersApplications) {
    return useQuery<PayLaterApplicationPaginationResponse>({
        queryKey: ['filterPayLaterApplications', filterParams],
        queryFn: () => walletService.filterPaylaterApplications(filterParams),
        staleTime: 5 * 60 * 1000,     // 5 phút coi data là "fresh"
        refetchOnWindowFocus: false, // không refetch khi focus lại tab
        refetchOnReconnect: false,   // không refetch khi mạng reconnect
        retry: 1,
    });
}
export function useGetPayLaterInfo(payLaterAccountNumber?: string) {
    return useQuery<PayLaterResponse>({
        queryKey: ['getPayLaterInfo', payLaterAccountNumber],
        enabled: !!payLaterAccountNumber,
        queryFn: () => walletService.getPaylaterInfo(payLaterAccountNumber as string),
        staleTime: 5 * 60 * 1000,     // 5 phút coi data là "fresh"
        refetchOnWindowFocus: false, // không refetch khi focus lại tab
        refetchOnReconnect: false,   // không refetch khi mạng reconnect
        retry: 1,
    });
}

export function useProcessPaylaterApplication() {
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    return useMutation<ProcessPaylaterApplicationResponse, ErrorResponse, ProcessPaylaterApplicationRequest>({
        mutationFn: (data: ProcessPaylaterApplicationRequest) => walletService.processPaylaterApplication(data),
        onSuccess: () => {
            toast.success("Xử lý đơn Paylater thành công");
            queryClient.invalidateQueries({ queryKey: ['filterPayLaterApplications'] });
            navigate({ to: "/manage/wallets-management/paylater-applications" });
        },
        onError: (error: ErrorResponse) => {
            toast.error(error?.message || "Xử lý đơn Paylater thất bại");
        },
    });
}