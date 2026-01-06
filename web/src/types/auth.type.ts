export interface LoginRequest {
    username: string;
    password: string;
}

export interface LoginResponse {
    access_token: string;
    refresh_token: string;
    id_token: string;
    expires_in: number; // in seconds
}

export interface RefreshTokenRequest {
    refresh_token: string;
}   

export interface RefreshTokenResponse extends LoginResponse {}



