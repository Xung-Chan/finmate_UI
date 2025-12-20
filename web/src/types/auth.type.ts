export type LoginRequest = {
    username: string;
    password: string;
}

export type LoginResponse = {
    access_token: string;
    refresh_token: string;
    id_token: string;
    expires_in: number; // in seconds
}

export type RefreshTokenRequest = {
    refresh_token: string;
}

export type RefreshTokenResponse = LoginResponse;

export type CreateBatchUserRequest = {
    phoneNumber: string;
    mail: string;
    role: string;
    fullName: string;
    address: string;
    dateOfBirth: string; // ISO date string
    cardId: string;
}

export type CreateBatchUsersResponse = {}

export type StatsUsersResponse = {}