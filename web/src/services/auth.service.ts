import type { AxiosResponse } from 'axios';
import httpClient from '@/config/axiosConfig';
import API_ROUTES from '@/config/apiRoutes';
import type {
    LoginRequest,
    LoginResponse,
    RefreshTokenResponse,
    CreateBatchUserRequest,
    CreateBatchUsersResponse,
    StatsUsersResponse
} from '@/types/auth.type';

const authService = {
    logIn: async (data: LoginRequest): Promise<LoginResponse> => {
        const response: AxiosResponse<LoginResponse> = await httpClient.post<LoginResponse>(API_ROUTES.auth.login, data);
        return response.data;
    },

    refreshToken: async (): Promise<RefreshTokenResponse> => {
        const response: AxiosResponse<RefreshTokenResponse> = await httpClient.post<RefreshTokenResponse>(API_ROUTES.auth.refresh);
        return response.data;
    },

    createBatchUsers: async (data: CreateBatchUserRequest[]): Promise<CreateBatchUsersResponse> => {
        const response: AxiosResponse<CreateBatchUsersResponse> = await httpClient.post<CreateBatchUsersResponse>(API_ROUTES.auth.createBatchUsers, data);
        return response.data;
    },

    getStatsUsers: async (): Promise<StatsUsersResponse> => {
        const response: AxiosResponse<StatsUsersResponse> = await httpClient.get<StatsUsersResponse>(API_ROUTES.auth.getStatsUsers);
        return response.data;
    }
};

export default authService;