import { createRouter } from "@tanstack/react-router";
import { authLayoutRoute } from "./routes/auth/layout";
import { dashboardLayoutRoute } from "./routes/dashboard/layout";
import { loginRoute } from "./routes/auth";
import { rootRoute } from "@/routes/root";
import {
    usermanagementRoute,
    userListIndexRoute,
    userListRoute,
    addUserDetailRoute,
    editUserDetailRoute,
    roleManagementRoute,
    roleManagementIndexRoute,
    transactionManagementRoute,
    transactionManagementIndexRoute,
    transactionDetailRoute,
    aiTrainingDataRoute,
    aiTrainingDataIndexRoute,
    creditPolicyRoute,
    creditPolicyIndexRoute,
} from "./routes/dashboard";
export const routeTree = rootRoute.addChildren([
    authLayoutRoute.addChildren([
        loginRoute,
        // thêm các route con ở đây
    ]),
    dashboardLayoutRoute.addChildren([
        usermanagementRoute.addChildren([
            userListRoute.addChildren([
                userListIndexRoute,
                addUserDetailRoute,
                editUserDetailRoute,
            ]),
            roleManagementRoute.addChildren([
                roleManagementIndexRoute,
            ]),
        ]),
        transactionManagementRoute.addChildren([
            transactionManagementIndexRoute,
            transactionDetailRoute,
        ]),
        aiTrainingDataRoute.addChildren([
            aiTrainingDataIndexRoute,
        ]),
        creditPolicyRoute.addChildren([
            creditPolicyIndexRoute,
        ]),
        // thêm các route con ở đây
    ]),
    // thêm các route con ở đây
]);

export const router = createRouter({ routeTree })