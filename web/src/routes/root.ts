import { createRootRoute, redirect } from "@tanstack/react-router";
import { isTokenValid } from '@/utils/jwt';

export const rootRoute = createRootRoute({
    beforeLoad: ({ location }) => {
        const token = localStorage.getItem("access_token") ?? undefined;
        const pathname = location.pathname;

        const isAuthenticated = isTokenValid(token);

        if (!isAuthenticated) {
            // ensure stale token removed
            localStorage.removeItem('access_token');
        }

        // /
        if (pathname === "/") {
            throw redirect({ to: isAuthenticated ? "/manage" : "/auth/login" });
        }

        // Chặn manage
        if (!isAuthenticated && pathname.startsWith("/manage")) {
            throw redirect({ to: "/auth/login" });
        }

        // Đã login mà vào login
        if (isAuthenticated && pathname.startsWith("/auth/login")) {
            throw redirect({ to: "/manage" });
        }
    },
});