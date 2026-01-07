const baseURL = import.meta.env.VITE_API_BASE_URL;
// const buildQuery = (endpoint: string, params: Record<string, any>) => {
//     const query = new URLSearchParams();

//     Object.entries(params).forEach(([key, value]) => {
//         if (value !== undefined && value !== null && value !== "") {
//             query.append(key, String(value));
//         }
//     });

//     return `${baseURL}${endpoint}?${query.toString()}`;
// };

const API_ROUTES = {
    auth: {
        login: `${baseURL}/auth/login`, //done 
        refresh: `${baseURL}/auth/refresh`,
    },
    notify: {
        //both

        //admin
        filterLogMails: `${baseURL}/api/manage/logs/mails/filter`,
        createNotification: `${baseURL}/api/manage/notifications/`,
        filterNotifications: `${baseURL}/api/manage/notifications/filter`,

        //staff
    },
    transaction: {
        //both
        createCash: `${baseURL}/api/manage/transactions/cash`, //done
        filterTransactions: `${baseURL}/api/manage/transactions/filter`, //done
        statsTransactions: (params: { date: string }) => `${baseURL}/api/manage/transactions/statistics/stats?${new URLSearchParams(params).toString()}`,
        trendsForManage: `${baseURL}/api/manage/transactions/statistics/trends`,
        distributionForManage: `${baseURL}/api/manage/transactions/statistics/distribution`,
        topUsersForManage: `${baseURL}/api/manage/transactions/statistics/users/top`,

    },
    // expensetype: {
    //     //both

    //     //admin
    //     createExpenseType: `${baseURL}/api/manage/expense-types/`,
    //     updateExpenseType: `${baseURL}/api/manage/expense-types/`,
    //     getAllExpenseType: `${baseURL}/api/manage/expense-types/`,
    // },
    wallet: {
        //both
        getWalletInfo: (walletNumber: string) => `${baseURL}/api/manage/wallets/${walletNumber}`,// done
        filterAccounts: `${baseURL}/api/manage/wallets/filter`,//done
        updateStatus: `${baseURL}/api/manage/wallets/status`,
        filterWalletVertifications: `${baseURL}/api/manage/wallet-verifications/filter`, //done
        processWalletVerification: `${baseURL}/api/manage/wallet-verifications/process`,
        processPaylaterApplication: `${baseURL}/api/manage/pay-later/applications/process`,
        filterPaylaterApplications: `${baseURL}/api/manage/pay-later/applications/filter`,//done
        filterAccountsPaylater: `${baseURL}/api/manage/pay-later/accounts/filter`,//done
        getPayLaterInfo: (payLaterAccountNumber: string) => `${baseURL}/api/manage/pay-later/accounts/${payLaterAccountNumber}`, //done
        filterBillingCycle: `${baseURL}/api/manage/pay-later/billing-cycles/filter`,
        //admin

    },
    user: {
        // both
        createSingleUser: `${baseURL}/api/users/management/`, //done
        getStatsUsers: `${baseURL}/api/users/statistics/summary`,

        //admin
        createBatchUsers: `${baseURL}/api/users/management/create-batch-users`,
        unbanUser: `${baseURL}/api/users/management/unlock-users`,//done
        banUser: `${baseURL}/api/users/management/ban-users`,//done
        filterUsers: `${baseURL}/api/users/management/filter`,//done
        //staff
    }
}
export default API_ROUTES;
