export type User = {
    id: number;
    email: string;
    phone: string;
    fullName: string;
    birthYear: number;
    address: string;
    status: "active" | "locked" | "pending";
}