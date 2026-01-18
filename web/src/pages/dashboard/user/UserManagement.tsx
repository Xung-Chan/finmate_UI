import React, { useEffect, useState, useMemo } from "react";
import { Tabs, Space, Spin } from "antd";
import { SearchComponent } from "@/components/Search";
import CustomButton from "@/components/Button";
import {
    PlusOutlined,
    FileExcelOutlined,
    EditOutlined,
} from "@ant-design/icons";
import TableComponent from "@/components/Table";
import type { TableColumn } from "@/components/Table";

import PaginationComponent from "@/components/Pagination";
import { colors } from "@/theme/color";
import { useNavigate } from "@tanstack/react-router";

import { type UserResource } from "@/types/user.type";
import { useFilterUsers, useBanUser, useUnbanUser } from "@/hooks/user.hook";
import {
    addUserDetailRoute,
} from "@/routes/dashboard";
import UserDetailModal from "./UserModal";
import type { UserStatus } from "@/enum/status";
const UserManagementPage: React.FC = () => {
    const navigate = useNavigate();

    const [searchValue, setSearchValue] = useState("");
    const [filterStatus, setFilterStatus] = useState<UserStatus | "">("");
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(10);
    const [debounced, setDebounced] = useState(searchValue);
    const fileInputRef = React.useRef<HTMLInputElement>(null);

    const [selectedUser, setSelectedUser] = useState<UserResource | null>(null);
    const [openDetail, setOpenDetail] = useState(false);
    const { mutate: banUser, isPending: isBanning } = useBanUser();
    const { mutate: unbanUser, isPending: isUnbanning } = useUnbanUser();
    const isActionLoading = isBanning || isUnbanning;


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
            size: itemsPerPage,
            page: currentPage - 1,
        }),
        [debounced, filterStatus, itemsPerPage, currentPage]
    );

    /* =======================
       FETCH USERS
       ======================= */
    const { data, isLoading } = useFilterUsers(filterParams);

    const users: UserResource[] = data?.contents ?? [];
    const totalItems = data?.totalElements ?? users.length;

    /* =======================
       PAGINATION (FE)
       ======================= */
    const startIndex = (currentPage - 1) * itemsPerPage;

    /* =======================
       RENDER STATUS
       ======================= */
    const renderStatus = (status?: UserStatus | "") => {
        if (!status) return "-";
        const key = String(status).toUpperCase();


        const statusConfig: Record<string, { label: string; bg: string; color: string }> = {
            ACTIVE: {
                label: "Hoạt động",
                bg: "#D1FADF",
                color: "#027A48",
            },
            LOCKED: {
                label: "Bị khóa",
                bg: "#FEE4E2",
                color: "#B42318",
            },
            UNACTIVE: {
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

    /* =======================
       TABLE COLUMNS
       ======================= */
    const columns: TableColumn<UserResource>[] = [
        {
            key: "stt",
            label: "STT",
            align: "center",
            render: (_item, index) => startIndex + index + 1,
        },
        {
            key: "fullName",
            label: "Họ và tên",
        },
        {
            key: "phoneNumber",
            label: "Số điện thoại",
        },
        {
            key: "mail",
            label: "Email",
        },
        {
            key: "status",
            label: "Trạng thái",
            align: "center",
            render: (item: UserResource) => renderStatus(item.status),
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
    const renderActions = (item: UserResource) => (
        <Space size={12}>
            <EditOutlined
                onClick={() => {
                    setSelectedUser(item);
                    setOpenDetail(true);
                }}
                style={{ fontSize: 18, color: colors.orange.o1, cursor: "pointer" }}
            />
            <UserDetailModal
                open={openDetail}
                user={selectedUser}
                onClose={() => {
                    if (isActionLoading) return;
                    setOpenDetail(false);
                    setSelectedUser(null);
                }}
                onToggleStatus={(user) => {
                    if (user.status === "ACTIVE") {
                        banUser({ usernames: [user.username || ""] }, {
                            onSuccess: () => {
                                setOpenDetail(false);
                                setSelectedUser(null);
                            }
                        });
                    } else {
                        unbanUser({ usernames: [user.username || ""] }, {
                            onSuccess: () => {
                                setOpenDetail(false);
                                setSelectedUser(null);
                            }
                        });
                    }
                    setOpenDetail(false);
                }}
            />

        </Space>
    );

    const handleImportExcel = () => {
        fileInputRef.current?.click();
    };
    /* =======================
       UI
       ======================= */
    return (
        <div className="flex flex-col space-y-6 p-5">
            {/* HEADER */}
            <div className="flex items-center gap-4">
                <h2 className="text-2xl font-bold">Danh sách người dùng</h2>
                <SearchComponent
                    placeholder="Tìm kiếm theo tên, email, SĐT..."
                    value={searchValue}
                    onChange={setSearchValue}
                    onSearch={setSearchValue}
                />
            </div>

            <div className="bg-white p-4 rounded-lg shadow-md space-y-4">
                {/* FILTER BAR */}
                <div className="flex justify-between items-center">
                    <Tabs
                        activeKey={String(filterStatus)}
                        onChange={(key) => {
                            setCurrentPage(1);
                            setFilterStatus(key as UserStatus | "");
                        }}
                        items={[
                            { key: "", label: "Tất cả" },
                            { key: "ACTIVE", label: "Hoạt động" },
                            { key: "LOCKED", label: "Bị khóa" },
                            { key: "UNACTIVE", label: "Chưa kích hoạt" },
                        ]}
                    />

                    <Space>
                        <CustomButton
                            icon={<FileExcelOutlined />}
                            label="nhập Excel"
                            onClick={handleImportExcel}
                        />
                        <CustomButton
                            icon={<PlusOutlined />}
                            label="Tạo tài khoản"
                            onClick={() => navigate({ to: addUserDetailRoute.to })}
                        />
                    </Space>
                </div>

                {/* TABLE */}
                {isLoading ? (
                    <div className="flex justify-center py-10">
                        <Spin />
                    </div>
                ) : (
                    <>
                        <TableComponent<UserResource>
                            columns={columns}
                            dataSource={users}
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

export default UserManagementPage;
