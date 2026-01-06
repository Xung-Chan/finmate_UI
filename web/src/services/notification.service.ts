import type { AxiosResponse } from "axios";
import httpClient from "@/config/axiosConfig";
import API_ROUTES from "@/config/apiRoutes";

import type {
    FilterNotifications,
    NotificationPaginationResponse,
    CreateNotificationRequest,
    NotificationResponse,
    FilterLogMails,
    LogMailPaginationResponse,
} from "@/types/notification.type";

export const notificationService = {
    filterNotifications: async (data: FilterNotifications): Promise<NotificationPaginationResponse> => {
        const response: AxiosResponse<NotificationPaginationResponse> = await httpClient.post<NotificationPaginationResponse>(API_ROUTES.notify.filterNotifications, data);
        return response.data;
    },
    createNotification: async (data: CreateNotificationRequest): Promise<NotificationResponse> => {
        const response: AxiosResponse<NotificationResponse> = await httpClient.post<NotificationResponse>(API_ROUTES.notify.createNotification, data);
        return response.data;
    },
    filterLogMails: async (data: FilterLogMails): Promise<LogMailPaginationResponse> => {
        const response: AxiosResponse<LogMailPaginationResponse> = await httpClient.post<LogMailPaginationResponse>(API_ROUTES.notify.filterLogMails, data);
        return response.data;
    },

};
