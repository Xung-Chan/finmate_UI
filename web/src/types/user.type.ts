import type { PaginationResponse } from "./baseresponse";
import type { UserStatus, RoleStatus } from "@/enum/status";
export interface UserResource {
    id: string;
    username?: string;
    fullName?: string;
    mail?: string;
    phoneNumber?: string;
    avatarUrl?: string;
    dateOfBirth?: [number, number, number];
    gender?: "MALE" | "FEMALE";
    address?: string;
    cardId?: string;
    status: UserStatus;
}

export interface FilterUsers {
    keyword: string;
    status?: UserStatus;
}

export interface CreateSingleUserRequest {
    mail?: string;
    phoneNumber?: string;
    role?: RoleStatus;
    fullName: string;
    gender: "MALE" | "FEMALE"
    dateOfBirth?: string;
    address?: string;
    cardId?: string;
}
export interface UpdateStatusUserRequest {
    usernames: [string];
}

export interface CreateBatchUserRequest extends CreateSingleUserRequest { }

export type UserPaginationResponse = PaginationResponse<UserResource>;