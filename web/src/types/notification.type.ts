import type { PaginationResponse } from "./baseresponse";

export interface NotificationResource {
    id: string;
    title?: string;
    content?: string;
    createdAt?: string;
    read?: boolean;
}

export interface FilterNotifications {
    fromDate?: string;
    toDate?: string;
    type?: string;
}

export interface CreateNotificationRequest {
    title: string;
    description: string;
}

export interface LogMailResource {
    id: string;
    mail?: string;
    subject?: string;
    template?: string;
    sendAt?: string;
}
export interface FilterLogMails {
    mailKeyword?: string;
    size?: number;
    page?: number;
}
export type LogMailPaginationResponse = PaginationResponse<LogMailResource>;
export type NotificationPaginationResponse = PaginationResponse<NotificationResource>;
export type NotificationResponse = NotificationResource;