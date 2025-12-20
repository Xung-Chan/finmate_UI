import { createRoute } from "@tanstack/react-router";
import DashboardLayout from "@/layouts/DashboardLayout";
import { rootRoute } from "@/routes/root";

export const dashboardLayoutRoute = createRoute({
    getParentRoute: () => rootRoute,
    path: "manage",
    component: DashboardLayout,
});