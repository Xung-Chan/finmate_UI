import type { AxiosResponse } from 'axios';
import httpClient from '@/config/axiosConfig';
import API_ROUTES from '@/config/apiRoutes';

import type {
    LoginRequest,
    LoginResponse,
    RefreshTokenResponse,

} from '@/types/auth.type';

const authService = {
    logIn: async (data: LoginRequest): Promise<LoginResponse> => {
        const response: AxiosResponse<LoginResponse> =
            await httpClient.post<LoginResponse>(
                API_ROUTES.auth.login,
                data,
                {
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json',
                        'Authorization': 'Basic ' + btoa('banking_app:banking_app_secret'),
                        'X-App-Token': 'android_app_secret_key',
                    },
                    withCredentials: false,
                }
            );

        return response.data;
    },

    refreshToken: async (): Promise<RefreshTokenResponse> => {
        const response: AxiosResponse<RefreshTokenResponse> = await httpClient.post<RefreshTokenResponse>(API_ROUTES.auth.refresh);
        return response.data;
    },

};

export default authService;