import toast from "react-hot-toast";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import type {
    FilterTransactions,
    TransactionPaginationResponse,
    CreateCashTransaction,
    CreateCashTransactionResponse,
} from "@/types/transaction.type";
import type { AxiosError } from "axios";
import type { ErrorResponse } from "@/types/error.type";
import { transactionService } from "@/services/transaction.service";
import type { WalletResponse } from "@/types/walltet.type";

export function useFilterTransactions(filterParams: FilterTransactions) {
    return useQuery<TransactionPaginationResponse>({
        queryKey: ['filterTransactions', filterParams],
        queryFn: () => transactionService.filterTransactions(filterParams),
        staleTime: 5 * 60 * 1000,     // 5 phút coi data là "fresh"
        refetchOnWindowFocus: false, // không refetch khi focus lại tab
        refetchOnReconnect: false,   // không refetch khi mạng reconnect
        retry: 1,
    });
}

export function useCreateCashTransaction() {
    const queryClient = useQueryClient();

    return useMutation<
        CreateCashTransactionResponse,
        AxiosError<ErrorResponse>,
        CreateCashTransaction
    >({
        mutationFn: (data) =>
            transactionService.createCashTransaction(data),

        onMutate: async (variables) => {
            const { actionType, amount, sourceWalletNumber } = variables;

            const wallet = queryClient.getQueryData<WalletResponse>([
                'getWalletInfo',
                sourceWalletNumber,
            ]);

            if (!wallet) return;

            if (
                actionType === 'CASH_WITHDRAW' &&
                amount > (wallet.balance ?? 0)
            ) {
                toast.error("Số dư không đủ để rút");
                throw new Error("INSUFFICIENT_BALANCE"); // ❌ chặn mutation
            }
        },

        onSuccess: (_, variables) => {
            const { actionType, amount, sourceWalletNumber } = variables;

            queryClient.setQueryData<WalletResponse>(
                ['getWalletInfo', sourceWalletNumber],
                (old) => {
                    if (!old) return old;

                    let newBalance = old.balance ?? 0;

                    if (actionType === 'CASH_DEPOSIT') {
                        newBalance += amount;
                    }

                    if (actionType === 'CASH_WITHDRAW') {
                        newBalance -= amount;
                    }

                    return {
                        ...old,
                        balance: newBalance,
                    };
                }
            );

            toast.success("Cash transaction created successfully");

            queryClient.invalidateQueries({
                queryKey: ['filterTransactions'],
            });
        },

        onError: (error) => {
            if (error.message === "INSUFFICIENT_BALANCE") return;

            toast.error(
                error.response?.data.message ||
                "Failed to create cash transaction"
            );
        },
    });
}
