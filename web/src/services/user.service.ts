import type { AxiosResponse } from "axios";
import httpClient from "@/config/axiosConfig";
import API_ROUTES from "@/config/apiRoutes";


import type { 
    FilterUsers,
    CreateSingleUserRequest,
    CreateBatchUserRequest,
    UpdateStatusUserRequest,
    UserPaginationResponse,
    ProfileResponse
} from "@/types/user.type";

export const userService = {
    createSingleUser: async (data: CreateSingleUserRequest): Promise<void> => { 
        await httpClient.post(API_ROUTES.user.createSingleUser, data);
    },
    createBatchUsers: async (data: CreateBatchUserRequest[]): Promise<void> => {
        await httpClient.post(API_ROUTES.user.createBatchUsers, data);
    },

    // getStatsUsers: async (): Promise<StatsUsersResponse> => {
    //     const response: AxiosResponse<StatsUsersResponse> = await httpClient.get<StatsUsersResponse>(API_ROUTES.auth.getStatsUsers);
    //     return response.data;
    // },

    filterUsers: async(data: FilterUsers): Promise<UserPaginationResponse> => {    
        const response: AxiosResponse<UserPaginationResponse> = await httpClient.post<UserPaginationResponse>(API_ROUTES.user.filterUsers, data);
        return response.data;
    },
    banUser: async(data: UpdateStatusUserRequest): Promise<void> => {    
        await httpClient.post<void>(API_ROUTES.user.banUser, data);
        },
    unbanUser: async(data: UpdateStatusUserRequest): Promise<void> => {    
        await httpClient.post<void>(API_ROUTES.user.unbanUser, data);
    },
    getProfile: async(): Promise<ProfileResponse> => {    
        const response: AxiosResponse<ProfileResponse> = await httpClient.get<ProfileResponse>(API_ROUTES.user.getProfile);
        return response.data;
    }
}
