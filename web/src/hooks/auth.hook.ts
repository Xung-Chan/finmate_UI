import { useNavigate } from "@tanstack/react-router";
import toast from "react-hot-toast";
import type {
    LoginRequest,
    LoginResponse,
    CreateBatchUserRequest,
    CreateBatchUsersResponse,
    StatsUsersResponse,
} from "@/types/auth.type";
import authService from "@/services/auth.service";
import { useMutation, useQuery } from '@tanstack/react-query';

export function useAuth() {
    const navigate = useNavigate();

    return useMutation({
        mutationKey: ['login'],
        mutationFn: (_user: LoginRequest) => {
            return authService.logIn(_user)
        },
        onSuccess: (_res: LoginResponse) => {
            toast.success('Đăng nhập thành công');
            navigate({ to: '/manage/tong-quan' });
        },
        onError: () => {
            toast.error('Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.');
        }
    })

}   
export function useCreateBatchUsers() {
    return useMutation({
        mutationKey: ['createBatchUsers'],
        mutationFn: (data: CreateBatchUserRequest[]) => {
            return authService.createBatchUsers(data)
        },
        onSuccess: (_res: CreateBatchUsersResponse) => {
            toast.success('Tạo người dùng hàng loạt thành công');
        },
        onError: () => {
            toast.error('Tạo người dùng hàng loạt thất bại. Vui lòng kiểm tra lại thông tin.');
        }
    })
}

export function useGetStatsUsers() {
    return useQuery<StatsUsersResponse>({
        queryKey: ['statsUsers'],
        queryFn: async () => {
            const res = await authService.getStatsUsers();
            return res;
        },
    })
}
