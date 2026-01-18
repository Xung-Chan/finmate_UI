import React, { useEffect, useState, useMemo } from "react";
import { Tabs, Space, Spin, Select } from "antd";
import { SearchComponent } from "@/components/Search";
import {

    EditOutlined,
} from "@ant-design/icons";
import TableComponent from "@/components/Table";
import type { TableColumn } from "@/components/Table";

import PaginationComponent from "@/components/Pagination";
import { colors } from "@/theme/color";
import { useNavigate } from "@tanstack/react-router";
import { type WalletResource } from "@/types/walltet.type";
import { useFilterWallets } from "@/hooks/wallet.hook";
import {
    walletDetailRoute,
} from "@/routes/dashboard";
import type { WalletStatus } from "@/enum/status";
const WalletManagementPage: React.FC = () => {
    const navigate = useNavigate();

    const [searchValue, setSearchValue] = useState("");
    const [filterStatus, setFilterStatus] = useState<WalletStatus | "">("");
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(10);
    const [debounced, setDebounced] = useState(searchValue);

    const [sortBy, setSortBy] = useState<string | undefined>();



    /* =======================
       FILTER PARAMS → BACKEND
       ======================= */
    useEffect(() => {
        const handler = setTimeout(() => {
            setDebounced(searchValue);
        }, 500);
        return () => {
            clearTimeout(handler);
        }
    }, [searchValue]);
    const filterParams = useMemo(
        () => ({
            keyword: debounced.trim() || "",
            status: filterStatus || undefined,
            sort_by: sortBy,
            size: itemsPerPage,
            page: currentPage - 1,
        }),
        [debounced, filterStatus, sortBy, itemsPerPage, currentPage]
    );


    /* =======================
       FETCH USERS
       ======================= */
    const { data, isLoading } = useFilterWallets(filterParams);

    const wallets: WalletResource[] = data?.contents ?? [];
    const totalItems = data?.totalElements ?? wallets.length;

    /* =======================
       PAGINATION (FE)
       ======================= */
    const startIndex = (currentPage - 1) * itemsPerPage;

    /* =======================
       RENDER STATUS
       ======================= */
    const renderStatus = (status?: WalletStatus | "") => {
        if (!status) return "-";
        const key = String(status).toUpperCase();


        const statusConfig: Record<string, { label: string; bg: string; color: string }> = {
            ACTIVE: {
                label: "Hoạt động",
                bg: "#D1FADF",
                color: "#027A48",
            },
            SUSPENDED: {
                label: "Bị khóa",
                bg: "#FEE4E2",
                color: "#B42318",
            },
            PENDING: {
                label: "Chưa kích hoạt",
                bg: "#FEF0C7",
                color: "#B54708",
            },
        } as const;

        const config = statusConfig[key] ?? {
            label: key,
            bg: "#F3F4F6",
            color: "#374151",
        };
        return (
            <span
                className="inline-block px-3 py-1 rounded-md text-sm font-medium"
                style={{
                    backgroundColor: config.bg,
                    color: config.color,
                }}
            >
                {config.label}
            </span>
        );
    };
    const renderVerifyStatus = (status: boolean | undefined) => {


        const statusConfig: Record<string, { label: string; bg: string; color: string }> = {
            VERIFIED: {
                label: "Đã xác thực",
                bg: "#D1FADF",
                color: "#027A48",
            },

            NOT_VERIFIED: {
                label: "Chưa xác thực",
                bg: "#FEF0C7",
                color: "#B54708",
            },


        } as const;

        const config = status ? statusConfig["VERIFIED"] : statusConfig["NOT_VERIFIED"] ?? {

        }
        return (
            <span
                className="inline-block px-3 py-1 rounded-md text-sm font-medium"
                style={{
                    backgroundColor: config.bg,
                    color: config.color,
                }}
            >
                {config.label}
            </span>
        );
    };

    /* =======================
       TABLE COLUMNS
       ======================= */
    const columns: TableColumn<WalletResource>[] = [
        {
            key: "stt",
            label: "STT",
            align: "center",
            render: (_item, index) => startIndex + index + 1,
        },
        {
            key: "username",
            label: "Chủ ví",
            render: (item) => item.username || "-",
        },
        {
            key: "mail",
            label: "Email",
            render: (item) => item.mail || "-",
        },
        {
            key: "walletNumber",
            label: "Số tài khoản",
            render: (item) => (
                <span className="font-mono">{item.walletNumber || "-"}</span>
            ),
        },
        {
            key: "merchantName",
            label: "Ngân hàng",
            render: (item) => item.merchantName || "-",
        },
        {
            key: "balance",
            label: "Số dư",
            align: "right",
            render: (item) =>
                item.balance != null
                    ? item.balance.toLocaleString("vi-VN") + " ₫"
                    : "-",
        },
        {
            key: "verified",
            label: "Xác thực",
            align: "center",
            render: (item) =>
                renderVerifyStatus(item.verified)
        },
        {
            key: "status",
            label: "Trạng thái",
            align: "center",
            render: (item) => renderStatus(item.status),
        },
        {
            key: "actions",
            label: "Hành động",
            align: "center",
        },
    ];


    /* =======================
       ACTIONS
       ======================= */
    const renderActions = (item: WalletResource) => (
        <Space size={12}>
            <EditOutlined
                title="Xem chi tiết ví"
                style={{ fontSize: 18, color: colors.orange.o1, cursor: "pointer" }}
                onClick={() =>
                    navigate({
                        to: walletDetailRoute.to,
                        params: { walletNumber: item.walletNumber },
                    })
                }
            />
        </Space>
    );

    return (
        <div className="flex flex-col space-y-6 p-5">
            {/* HEADER */}
            <div className="flex items-center gap-4 flex-wrap">
                <h2 className="text-2xl font-bold">Danh sách ví</h2>
                <SearchComponent
                    placeholder="Tìm kiếm theo tên, email, SĐT..."
                    value={searchValue}
                    onChange={setSearchValue}
                    onSearch={setSearchValue}
                />

            </div>

            <div className="bg-white p-4 rounded-lg shadow-md space-y-4">
                <Tabs
                    activeKey={String(filterStatus)}
                    onChange={(key) => {
                        setCurrentPage(1);
                        setFilterStatus(key as WalletStatus | "");
                    }}
                    items={[
                        { key: "", label: "Tất cả" },
                        { key: "ACTIVE", label: "Hoạt động" },
                        { key: "PENDING", label: "Chưa kích hoạt" },
                        { key: "SUSPENDED", label: "Tạm khóa" },
                    ]}
                />
                <div className="flex justify-end">
                    {/* SORT */}
                    <Select
                        allowClear
                        placeholder="Sắp xếp"
                        className="w-[200px]"
                        onChange={(value) => {
                            setCurrentPage(1);
                            setSortBy(value);
                        }}
                        options={[
                            { value: "word_asc", label: "Từ A-Z" },
                            { value: "word_des", label: "Từ Z-A" },

                        ]}
                    />
                </div>
                {/* TABLE */}
                {isLoading ? (
                    <div className="flex justify-center py-10">
                        <Spin />
                    </div>
                ) : (
                    <>
                        <TableComponent<WalletResource>
                            columns={columns}
                            dataSource={wallets}
                            renderActions={renderActions}
                        />

                        <PaginationComponent
                            currentPage={currentPage}
                            totalItems={totalItems}
                            itemsPerPage={itemsPerPage}
                            onPageChange={setCurrentPage}
                            onItemsPerPageChange={setItemsPerPage}
                        />
                    </>
                )}
            </div>
        </div>
    );
};

export default WalletManagementPage;
