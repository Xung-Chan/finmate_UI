import { useNavigate } from "@tanstack/react-router";
import toast from "react-hot-toast";
import type {
    LoginRequest,
    LoginResponse,
} from "@/types/auth.type";

import authService from "@/services/auth.service";
import { useMutation } from '@tanstack/react-query';
import type { ErrorResponse } from "@/types/error.type";
export function useLogin() {
    const navigate = useNavigate();

    return useMutation({
        mutationKey: ['login'],
        mutationFn: async (_user: LoginRequest) => {
            const res = await authService.logIn(_user); 
            return res;
        },
        onSuccess: (_res: LoginResponse) => {
            const token = _res.access_token;
            localStorage.setItem('access_token', token);
            toast.success('Đăng nhập thành công');
            navigate({ to: '/manage/analytics' });
        },
        onError: (error: ErrorResponse) => {
            toast.error(`${error.message || 'Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.'}`);
        }
    })

}

export function useLogout() {
    const navigate = useNavigate();
    return () => {
        localStorage.removeItem('access_token');
        toast.success('Đăng xuất thành công');
        navigate({ to: '/auth/login' });
    }
}