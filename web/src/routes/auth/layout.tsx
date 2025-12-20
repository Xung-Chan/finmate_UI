import { createRoute } from "@tanstack/react-router";
import AuthLayout from "@/layouts/AuthLayout";
import { rootRoute } from "@/routes/root";

export const authLayoutRoute = createRoute({
    getParentRoute: () => rootRoute,
    path: "auth",
    component: AuthLayout,
});
