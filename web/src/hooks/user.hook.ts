import { useNavigate } from "@tanstack/react-router";
import toast from "react-hot-toast";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import type {
    FilterUsers,
    CreateSingleUserRequest,
    CreateBatchUserRequest,
    UpdateStatusUserRequest,
} from "@/types/user.type";
import type { AxiosError } from "axios";
import type { ErrorResponse } from "@/types/error.type";
import { userService } from "@/services/user.service";
export function useCreateBatchUsers() {
  const navigate = useNavigate();

  return useMutation({
    mutationKey: ['createBatchUsers'],
    mutationFn: (data: CreateBatchUserRequest[]) =>
      userService.createBatchUsers(data),

    onSuccess: () => {
      toast.success('Tạo người dùng hàng loạt thành công');
      navigate({ to: '/manage/users' });
    },

    onError: (error: AxiosError<ErrorResponse>) => {
      const message =
        error.response?.data?.message ||
        'Tạo người dùng hàng loạt thất bại. Vui lòng kiểm tra lại dữ liệu.';

      toast.error(message);
    },
  });
}


export function useCreateSingleUser() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: ["createSingleUser"],
    mutationFn: (data: CreateSingleUserRequest) =>
      userService.createSingleUser(data),

    onSuccess: () => {
      toast.success("Tạo người dùng thành công");
      queryClient.invalidateQueries({ queryKey: ['filterUsers'] });
      navigate({ to: "/manage/users/list" });
    },

    onError: (error: AxiosError<ErrorResponse>) => {
      const message =
        error.response?.data.message ||
        "Tạo người dùng thất bại. Vui lòng thử lại.";

      toast.error(message);
    },
  });
}
// export function useGetStatsUsers() {
//     return useQuery<StatsUsersResponse>({
//         queryKey: ['statsUsers'],
//         queryFn: async () => {
//             const res = await userService.getStatsUsers();
//             return res;
//         },
//     })
// }
// ================= User =================//
export function useFilterUsers(params: FilterUsers) {
    const { keyword, status } = params;

    return useQuery({
        queryKey: ['filterUsers', keyword, status],
        queryFn: () => userService.filterUsers(params),

        staleTime: 5 * 60 * 1000,     // 5 phút coi data là "fresh"
        refetchOnWindowFocus: false, // không refetch khi focus lại tab
        refetchOnReconnect: false,   // không refetch khi mạng reconnect
        retry: 1, // Thử lại 1 lần nếu thất bại
              
    });
}


export function useBanUser() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: ['banUser'],
    mutationFn: (data: UpdateStatusUserRequest) =>
      userService.banUser(data),

    onSuccess: (_res, variables) => {
      updateUserStatusInCache(
        queryClient,
        variables.usernames,
        'LOCKED'
      );

      toast.success('Khóa tài khoản thành công');
    },

    onError: () => {
      toast.error('Khóa tài khoản thất bại. Vui lòng thử lại.');
    },
  });
}


export function useUnbanUser() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: ['unbanUser'],
    mutationFn: (data: UpdateStatusUserRequest) =>
      userService.unbanUser(data),

    onSuccess: (_res, variables) => {
      updateUserStatusInCache(
        queryClient,
        variables.usernames,
        'ACTIVE'
      );

      toast.success('Mở khóa tài khoản thành công');
    },

    onError: () => {
      toast.error('Mở khóa tài khoản thất bại. Vui lòng thử lại.');
    },
  });
}


// helpers
function updateUserStatusInCache(
  queryClient: ReturnType<typeof useQueryClient>,
  usernames: string[],
  newStatus: string
) {
  queryClient.setQueriesData(
    { queryKey: ['filterUsers'], exact: false },
    (oldData: any) => {
      if (!oldData?.contents) return oldData;

      return {
        ...oldData,
        contents: oldData.contents.map((u: any) =>
          usernames.includes(u.username)
            ? { ...u, status: newStatus }
            : u
        ),
      };
    }
  );
}
