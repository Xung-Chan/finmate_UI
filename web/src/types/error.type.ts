export interface ErrorResponse {
    error: string;
    message: string;
    timestamp: string;
    statusCode: number;
    settingCookies?: boolean;
}