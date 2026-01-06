import { createRoute } from "@tanstack/react-router";
import { dashboardLayoutRoute } from "./layout";
import { Outlet } from "@tanstack/react-router";
import UserManagementPage from "@/pages/dashboard/user/UserManagement";
import UserDetailPage from "@/pages/dashboard/user/UserDetail";
import WalletManagementPage from "@/pages/dashboard/wallet/WalletManagementPage";
import WalletDetailPage from "@/pages/dashboard/wallet/WalletDetail";
import WalletVerificationManagementPage from "@/pages/dashboard/wallet/WalletVerificationManagementPage";
import PayLaterAccountPage from "@/pages/dashboard/paylater/PaylaterAccountPage";
import PayLaterApplicationPage from "@/pages/dashboard/paylater/PaylaterApplicationPage";
import PaylaterDetailPage from "@/pages/dashboard/paylater/PaylaterDetail";
import TransactionManagementPage from "@/pages/dashboard/transaction/TransactionPage";
import TransactionAnalyticsPage from "@/pages/dashboard/transaction/TransactionAnalyticsPage";
import AiTrainingDataPage from "@/pages/dashboard/ai-content/AiTrainingDataPage";
import CreditPolicyPage from "@/pages/dashboard/credit/CreaditPolicyPage";
import NotificationManagementPage from "@/pages/dashboard/notification/NotificationManagementPage";
import CreateNotificationPage from "@/pages/dashboard/notification/CreateNotificationPage";
import LogMailManagementPage from "@/pages/dashboard/notification/LogMailPage";
export const usermanagementRoute = createRoute({
    getParentRoute: () => dashboardLayoutRoute,
    path: "users",
    component: () => <div><Outlet /></div>
});

export const userListRoute = createRoute({
    getParentRoute: () => usermanagementRoute,
    path: "list",
    component: () => <div><Outlet /></div>
});
export const userListIndexRoute = createRoute({
    getParentRoute: () => userListRoute,
    path: "/",
    component: UserManagementPage,
});

export const addUserDetailRoute = createRoute({
    getParentRoute: () => userListRoute,
    path: "detail",
    component: UserDetailPage,
});

export const editUserDetailRoute = createRoute({
    getParentRoute: () => userListRoute,
    path: "detail/$userId/edit",
    component: UserDetailPage,
});

export const walletManagementRoute = createRoute({
    getParentRoute: () => dashboardLayoutRoute,
    path: "wallets-management",
    component: () => <div><Outlet /></div>
});

export const walletNormalManagementRoute = createRoute({
    getParentRoute: () => walletManagementRoute,
    path: "wallets",
    component: () => <div><Outlet /></div>
});

export const walletManagementIndexRoute = createRoute({
    getParentRoute: () => walletNormalManagementRoute,
    path: "/",
    component: WalletManagementPage,
});

export const walletDetailRoute = createRoute({
    getParentRoute: () => walletNormalManagementRoute,
    path: "$walletNumber",
    component: WalletDetailPage,
});

export const walletVerificationManagementRoute = createRoute({
    getParentRoute: () => walletManagementRoute,
    path: "wallet-verifications",
    component: WalletVerificationManagementPage,
});

export const paylaterAccountRoute = createRoute({
    getParentRoute: () => walletManagementRoute,
    path: "paylaters",
    component: () => <div><Outlet /></div>
});
export const paylaterAccountIndexRoute = createRoute({
    getParentRoute: () => paylaterAccountRoute,
    path: "/",
    component: PayLaterAccountPage,
});

export const paylaterDetailRoute = createRoute({
    getParentRoute: () => paylaterAccountRoute,
    path: "$payLaterAccountNumber",
    component: PaylaterDetailPage,
});

export const paylaterApplicationRoute = createRoute({
    getParentRoute: () => walletManagementRoute,
    path: "paylater-applications",
    component: () => <div><Outlet /></div>
});
export const paylaterApplicationIndexRoute = createRoute({
    getParentRoute: () => paylaterApplicationRoute,
    path: "/",
    component: PayLaterApplicationPage,
});

export const transactionManagementRoute = createRoute({
    getParentRoute: () => dashboardLayoutRoute,
    path: "transactions",
    component: () => <div><Outlet /></div>,
});

export const transactionManagementIndexRoute = createRoute({
    getParentRoute: () => transactionManagementRoute,
    path: "/",
    component: TransactionManagementPage,
});

export const transactionAnalyticsRoute = createRoute({
    getParentRoute: () => dashboardLayoutRoute,
    path: "analytics",
    component: TransactionAnalyticsPage,
});


export const aiTrainingDataRoute = createRoute({
    getParentRoute: () => dashboardLayoutRoute,
    path: "ai-training-data",
    component: () => <AiTrainingDataPage />,
});

export const aiTrainingDataIndexRoute = createRoute({
    getParentRoute: () => aiTrainingDataRoute,
    path: "/",
    component: AiTrainingDataPage,
});

export const creditPolicyRoute = createRoute({
    getParentRoute: () => dashboardLayoutRoute,
    path: "credit-policy",
    component: () => <CreditPolicyPage />,
});

export const creditPolicyIndexRoute = createRoute({
    getParentRoute: () => creditPolicyRoute,
    path: "/",
    component: CreditPolicyPage,
});

export const notificationManagementRoute = createRoute({
    getParentRoute: () => dashboardLayoutRoute,
    path: "notifications",
    component: () => <div><Outlet /></div>,
});

export const notificationListRoute  = createRoute({
    getParentRoute: () => notificationManagementRoute,
    path: "list",
    component: () => <div><Outlet /></div>
});
export const notificationListIndexRoute = createRoute({
    getParentRoute: () => notificationListRoute,
    path: "/",
    component: NotificationManagementPage,
});

export const createNotificationRoute = createRoute({
    getParentRoute: () => notificationListRoute,
    path: "create",
    component: CreateNotificationPage,
});

export const logMailManagementRoute = createRoute({
    getParentRoute: () => notificationManagementRoute,
    path: "log-mails",
    component: LogMailManagementPage,
});