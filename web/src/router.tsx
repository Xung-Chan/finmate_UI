import { createRouter } from "@tanstack/react-router";
import { authLayoutRoute } from "./routes/auth/layout";
import { dashboardLayoutRoute } from "./routes/dashboard/layout";
import { loginRoute } from "./routes/auth";
import { rootRoute } from "@/routes/root";
import {
    usermanagementRoute,
    userListIndexRoute,
    addUserDetailRoute,
    editUserDetailRoute,
    walletNormalManagementRoute,
    walletManagementRoute,
    walletManagementIndexRoute,
    walletDetailRoute,
    walletVerificationManagementRoute,
    paylaterAccountRoute,
    paylaterApplicationIndexRoute,
    paylaterApplicationRoute,
    paylaterAccountIndexRoute,
    paylaterDetailRoute,
    transactionManagementRoute,
    transactionManagementIndexRoute,
    aiTrainingDataRoute,
    aiTrainingDataIndexRoute,
    creditPolicyRoute,
    creditPolicyIndexRoute,
    transactionAnalyticsRoute,
    notificationManagementRoute,
    notificationListRoute,
    notificationListIndexRoute,
    createNotificationRoute,
    logMailManagementRoute,
} from "./routes/dashboard";
export const routeTree = rootRoute.addChildren([
    authLayoutRoute.addChildren([
        loginRoute,
        // thêm các route con ở đây
    ]),
    dashboardLayoutRoute.addChildren([
        transactionAnalyticsRoute,
        usermanagementRoute.addChildren([
            userListIndexRoute,
            addUserDetailRoute,
            editUserDetailRoute,
        ]),
        walletManagementRoute.addChildren([
            walletNormalManagementRoute.addChildren([
                walletManagementIndexRoute,
                walletDetailRoute,
            ]),
            walletVerificationManagementRoute,
            paylaterAccountRoute.addChildren([
                paylaterAccountIndexRoute,
                paylaterDetailRoute,
            ]),
            paylaterApplicationRoute.addChildren([
                paylaterApplicationIndexRoute,
            ]),
        ]),
        transactionManagementRoute.addChildren([
            transactionManagementIndexRoute,
        ]),
        aiTrainingDataRoute.addChildren([
            aiTrainingDataIndexRoute,
        ]),
        creditPolicyRoute.addChildren([
            creditPolicyIndexRoute,
        ]),
        notificationManagementRoute.addChildren([
            notificationListRoute.addChildren([
                notificationListIndexRoute,
                createNotificationRoute,
            ]),
            logMailManagementRoute,
        ]),
        // thêm các route con ở đây
    ]),
    // thêm các route con ở đây
]);

export const router = createRouter({ routeTree })