import { useNavigate } from "@tanstack/react-router";
import toast from "react-hot-toast";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import type {
    NotificationResponse,
    NotificationPaginationResponse,
    FilterNotifications,
    CreateNotificationRequest,
    FilterLogMails,
    LogMailPaginationResponse,
} from "@/types/notification.type";
import type { ErrorResponse } from "@/types/error.type";
import { notificationService } from "@/services/notification.service";

export function useFilterNotifications(filterParams: FilterNotifications) {
    return useQuery<NotificationPaginationResponse>({
        queryKey: ['filterNotifications', filterParams],
        queryFn: () => notificationService.filterNotifications(filterParams),
        staleTime: 5 * 60 * 1000,     // 5 phút coi data là "fresh"
        refetchOnWindowFocus: false, // không refetch khi focus lại tab
        refetchOnReconnect: false,   // không refetch khi mạng reconnect
        retry: 1,
    });
}

export function useCreateNotification() {
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    return useMutation<NotificationResponse, ErrorResponse, CreateNotificationRequest>({
        mutationFn: (data: CreateNotificationRequest) => notificationService.createNotification(data),
        onSuccess: () => {
            toast.success( "Tạo thông báo thành công");
            queryClient.invalidateQueries({ queryKey: ['filterNotifications'] });
            navigate({ to: "/manage/notifications" });

        },
        onError: (error: ErrorResponse) => {
            return toast.error(error?.message || "Tạo thông báo thất bại");
        }
    });     
}

export function useFilterLogMails(filterParams: FilterLogMails) {
    return useQuery<LogMailPaginationResponse>({
        queryKey: ['filterLogMails', filterParams],
        queryFn: () => notificationService.filterLogMails(filterParams),
        staleTime: 5 * 60 * 1000,     // 5 phút coi data là "fresh"
        refetchOnWindowFocus: false, // không refetch khi focus lại tab
        refetchOnReconnect: false,   // không refetch khi mạng reconnect
        retry: 1,
    });
}