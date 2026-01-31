import { createRootRoute, redirect } from "@tanstack/react-router";
import { isTokenValid } from '@/utils/jwt';
import { hasAnyRole } from '@/hooks/permission.hook';
import { RoleStatus } from '@/enum/status';

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

        // Chặn manage: nếu chưa auth thì chuyển đến login
        if (!isAuthenticated && pathname.startsWith("/manage")) {
            throw redirect({ to: "/auth/login" });
        }

        // Nếu đã login nhưng không phải ADMIN thì không cho vào /manage
        if (isAuthenticated && pathname.startsWith("/manage")) {
            const isAdmin = hasAnyRole(RoleStatus.ADMIN);
            if (!isAdmin) {
                // remove token and redirect to login
                localStorage.removeItem('access_token');
                throw redirect({ to: "/auth/login" });
            }
        }

        // Đã login mà vào login
        if (isAuthenticated && pathname.startsWith("/auth/login")) {
            throw redirect({ to: "/manage" });
        }
    },
});