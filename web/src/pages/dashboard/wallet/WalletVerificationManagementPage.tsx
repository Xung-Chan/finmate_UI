import React, { useEffect, useState, useMemo } from "react";
import { Tabs, Space, Spin, Select } from "antd";
import { SearchComponent } from "@/components/Search";
import { EyeOutlined } from "@ant-design/icons";
import TableComponent from "@/components/Table";
import type { TableColumn } from "@/components/Table";
import PaginationComponent from "@/components/Pagination";
import { colors } from "@/theme/color";
import { useFilterWalletVerifications } from "@/hooks/wallet.hook"; // Hook bạn đã có

import WalletVerificationDetailModal from "./WalletVerificationDetailModal";
import type { VerificationStatus } from "@/enum/status"; // Giả sử bạn có enum này
import type { WalletVerificationResource } from "@/types/walltet.type";


const WalletVerificationManagementPage: React.FC = () => {

    const [searchValue, setSearchValue] = useState("");
    const [filterStatus, setFilterStatus] = useState<VerificationStatus | "">("");
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(10);
    const [debounced, setDebounced] = useState(searchValue);
    const [sortBy, setSortBy] = useState<string | undefined>();
    const [detailModalOpen, setDetailModalOpen] = useState(false);
    const [selectedVerified, setSelectedVerified] = useState<WalletVerificationResource | null>(null);

    const openDetailModal = (record: WalletVerificationResource) => {
        setSelectedVerified(record);
        setDetailModalOpen(true);
    };

    /* =======================
       DEBOUNCE SEARCH
       ======================= */
    useEffect(() => {
        const handler = setTimeout(() => {
            setDebounced(searchValue);
        }, 500);
        return () => clearTimeout(handler);
    }, [searchValue]);

    /* =======================
       FILTER PARAMS → BACKEND
       ======================= */
    const filterParams = useMemo(
        () => ({
            keyword: debounced.trim() || undefined,
            status: filterStatus || undefined,
            sort_by: sortBy,
            size: itemsPerPage,
            page: currentPage - 1,
        }),
        [debounced, filterStatus, sortBy, itemsPerPage, currentPage]
    );

    /* =======================
       FETCH DATA
       ======================= */
    const { data, isLoading } = useFilterWalletVerifications(filterParams);

    const verifications: WalletVerificationResource[] = data?.contents ?? [];
    const totalItems = data?.totalElements ?? verifications.length;

    /* =======================
       PAGINATION (FE)
       ======================= */
    const startIndex = (currentPage - 1) * itemsPerPage;

    /* =======================
       RENDER STATUS
       ======================= */
    const renderStatus = (status?: VerificationStatus | "") => {
        if (!status) return "-";

        const key = String(status).toUpperCase();
        const statusConfig: Record<
            string,
            { label: string; bg: string; color: string }
        > = {
            APPROVED: {
                label: "Đã duyệt",
                bg: "#D1FADF",
                color: "#027A48",
            },
            PENDING: {
                label: "Chờ duyệt",
                bg: "#FEF0C7",
                color: "#B54708",
            },
            REJECTED: {
                label: "Từ chối",
                bg: "#FEE4E2",
                color: "#B42318",
            },
        };

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

    /* =======================
       TABLE COLUMNS
       ======================= */
    const columns: TableColumn<WalletVerificationResource>[] = [
        {
            key: "stt",
            label: "STT",
            align: "center",
            render: (_item, index) => startIndex + index + 1,
        },
        {
            key: "walletNumber",
            label: "Số ví",
            render: (item) => (
                <span className="font-mono">{item.walletNumber || "-"}</span>
            ),
        },
        {
            key: "invoiceDisplayName",
            label: "Tên hiển thị hóa đơn",
            render: (item) => item.invoiceDisplayName || "-",
        },
        {
            key: "businessName",
            label: "Tên doanh nghiệp",
            render: (item) => item.businessName || "-",
        },
        {
            key: "businessCode",
            label: "Mã số thuế",
            render: (item) => item.businessCode || "-",
        },
        {
            key: "representativeName",
            label: "Người đại diện",
            render: (item) => item.representativeName || "-",
        },
        {
            key: "contactEmail",
            label: "Email liên hệ",
            render: (item) => item.contactEmail || "-",
        },
        {
            key: "contactPhone",
            label: "SĐT liên hệ",
            render: (item) => item.contactPhone || "-",
        },
        {
            key: "createdAt",
            label: "Ngày tạo đơn",
            render: (item) =>
                item.createdAt
                    ? new Date(item.createdAt).toLocaleDateString("vi-VN")
                    : "-",
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
    const renderActions = (item: WalletVerificationResource) => (
        <Space size={12}>
            <EyeOutlined
                title="Xem chi tiết đơn xác thực"
                style={{ fontSize: 18, color: colors.orange.o1, cursor: "pointer" }}
                onClick={() => openDetailModal(item)}
            />

            <WalletVerificationDetailModal
                open={detailModalOpen}
                verified={selectedVerified}
                onClose={() => {
                    setDetailModalOpen(false);
                    setSelectedVerified(null);
                }}
            />
        </Space>
    );

    return (
        <div className="flex flex-col space-y-6 p-5">
            {/* HEADER */}
            <div className="flex items-center gap-4 flex-wrap">
                <h2 className="text-2xl font-bold">Danh sách đơn ví xác thực</h2>
                <SearchComponent
                    placeholder="Tìm kiếm theo số ví, tên DN, email, SĐT..."
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
                        setFilterStatus(key as VerificationStatus | "");
                    }}
                    items={[
                        { key: "", label: "Tất cả" },
                        { key: "PENDING", label: "Chờ duyệt" },
                        { key: "APPROVED", label: "Đã duyệt" },
                        { key: "REJECTED", label: "Từ chối" },
                        { key: "CANCELED", label: "Đã hủy" },
                    ]}
                />

                <div className="flex justify-end">
                    <Select
                        allowClear
                        placeholder="Sắp xếp"
                        className="w-[200px]"
                        onChange={(value) => {
                            setCurrentPage(1);
                            setSortBy(value);
                        }}
                        options={[
                            { value: "date_asc", label: "Ngày cũ nhất trước" },
                            { value: "date_desc", label: "Ngày mới nhất trước" },
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
                        <TableComponent<WalletVerificationResource>
                            columns={columns}
                            dataSource={verifications}
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

export default WalletVerificationManagementPage;