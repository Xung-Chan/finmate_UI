export interface PaginationResponse<T> {
    contents: T[];
    totalPages: number;
    currentPage: number;
    pageSize: number;
    totalElements: number;
}