import React, { useState, useMemo } from "react";
import { Tabs, Space } from "antd";
import { SearchComponent } from "@/components/Search";
import CustomButton from "@/components/Button";
import { PlusOutlined, FileExcelOutlined, EyeOutlined, EditOutlined } from "@ant-design/icons";
import TableComponent from "@/components/Table";
import PaginationComponent from "@/components/Pagination";
import { colors } from "@/theme/color";
import { toast } from "react-hot-toast";
import { useNavigate } from "@tanstack/react-router";

import { type User } from "@/types/user.type";
import { addUserDetailRoute, editUserDetailRoute } from "@/routes/dashboard";

const UserManagementPage: React.FC = () => {
    const [searchValue, setSearchValue] = useState("");
    const [filterStatus, setFilterStatus] = useState<"all" | "locked" | "pending">("all");
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(10);
    const navigate = useNavigate();
    const startIndex = (currentPage - 1) * itemsPerPage;
    // 🔹 Data mẫu để test
    const data: User[] = [
        { id: 1, email: "user1@gmail.com", phone: "0901234567", fullName: "Nguyễn Văn A", birthYear: 1995, address: "Hà Nội", status: "active" },
        { id: 2, email: "user2@gmail.com", phone: "0907654321", fullName: "Trần Thị B", birthYear: 1992, address: "TP.HCM", status: "locked" },
        { id: 3, email: "user3@gmail.com", phone: "0909999999", fullName: "Lê Văn C", birthYear: 2000, address: "Đà Nẵng", status: "pending" },
    ];

    // 🔹 Lọc + tìm kiếm
    const filteredData = useMemo(() => {
        return data.filter((u) => {
            const matchStatus =
                filterStatus === "all"
                    ? true
                    : filterStatus === "locked"
                        ? u.status === "locked"
                        : u.status === "pending";
            const matchSearch =
                u.fullName.toLowerCase().includes(searchValue.toLowerCase()) ||
                u.email.toLowerCase().includes(searchValue.toLowerCase()) ||
                u.phone.includes(searchValue);
            return matchStatus && matchSearch;
        });
    }, [searchValue, filterStatus, data]);
    const paginatedData = filteredData.slice(startIndex, startIndex + itemsPerPage);

    // 🔹 Cấu hình cột
    const columns: { key: string; align?: "left" | "center" | "right"; label: string; render?: (item: User) => React.ReactNode }[] = [
        { key: "stt", label: "STT", render: (item: User) => filteredData.findIndex((u) => u.id === item.id) + 1 },
        { key: "email", label: "Email" },
        { key: "phone", label: "Số điện thoại" },
        { key: "fullName", label: "Họ và tên" },
        { key: "birthYear", label: "Năm sinh" },
        { key: "address", label: "Địa chỉ" },
        {
            key: "status",
            label: "Trạng thái",
            align: "center",
            render: (item: User) => renderStatus(item.status),
        },

        { key: "actions", label: "Hành động" },
    ];
    const renderStatus = (status: "active" | "locked" | "pending") => {
        return (
            <span
                className={`inline-block px-3 py-1 rounded-md text-sm font-medium
                    ${status === "active"
                        ? "bg-[#D1FADF] text-[#027A48]" // xanh lá
                        : status === "locked"
                            ? "bg-[#FEE4E2] text-[#B42318]" // đỏ
                            : "bg-[#FEF0C7] text-[#B54708]" // cam
                    }`}
            >
                {status === "active"
                    ? "Hoạt động"
                    : status === "locked"
                        ? "Bị khóa"
                        : "Yêu cầu mở ví trả sau"}
            </span>
        );
    };

    // 🔹 Hành động trong bảng
    const renderActions = (item: User) => (
        <Space size={12}>
            <EyeOutlined
                onClick={() => {
                    navigate({ to: editUserDetailRoute.to, params: { userId: item.id } });
                }}
                style={{
                    fontSize: 18,
                    color: colors.blue.b3,
                    cursor: "pointer",
                    transition: "color 0.25s",
                }}
                onMouseEnter={(e) => (e.currentTarget.style.color = colors.blue.b3)}
                onMouseLeave={(e) => (e.currentTarget.style.color = colors.blue.b2)}
            />

            <EditOutlined
                onClick={() => {
                    navigate({ to: editUserDetailRoute.to, params: { userId: item.id } });
                }}

                style={{
                    fontSize: 18,
                    color: colors.orange.o1,
                    cursor: "pointer",
                    transition: "color 0.25s",
                }}
                onMouseEnter={(e) => (e.currentTarget.style.color = colors.orange.o3)}
                onMouseLeave={(e) => (e.currentTarget.style.color = colors.orange.o2)}
            />
        </Space>
    );

    // 🔹 Các thao tác
    const handleExportExcel = () => toast.success("Đang xuất file Excel...");

    return (
        <div className="flex flex-col space-y-6 p-5">
            {/* HEADER TOOLBAR */}
            <div className="flex flex-wrap justify-start space-x-4">
                <h2 className="text-2xl text-black font-bold mb-4">Danh sách người dùng</h2>
                <SearchComponent
                    placeholder="Tìm kiếm theo tên, email, SĐT..."
                    value={searchValue}
                    onChange={(value) => setSearchValue(value)}
                    onSearch={(value) => setSearchValue(value)}
                />

            </div>
            <div className="flex flex-col space-y-4 bg-white p-4 rounded-lg shadow-md">
                <div className="flex flex-wrap items-center justify-between mb-4 gap-3">
                    <div className="flex items-center gap-3">
                        <Tabs
                            activeKey={filterStatus}
                            onChange={(key) => setFilterStatus(key as any)}
                            items={[
                                { key: "all", label: "Tất cả" },
                                { key: "locked", label: "Tài khoản bị khóa" },
                                { key: "pending", label: "Yêu cầu mở ví trả sau" },
                            ]}
                            tabBarStyle={{
                                borderBottom: `1px solid #e5e7eb`,
                                color: colors.gray.g1,
                            }}
                            className="custom-tabs"
                        />

                    </div>

                    <Space>
                        <CustomButton
                            color={colors.green.g1}
                            hoverColor={colors.green.g3}
                            icon={<FileExcelOutlined />}
                            label="Xuất Excel"
                            onClick={handleExportExcel}
                        />


                        <CustomButton
                            color={colors.blue.b2}
                            hoverColor={colors.blue.b3}
                            icon={<PlusOutlined />}
                            label="Tạo tài khoản"
                            onClick={() => navigate({ to: addUserDetailRoute.to })}
                        />
                    </Space>
                </div>

                {/* TABLE */}
                <TableComponent<User>
                    columns={columns}
                    dataSource={paginatedData}
                    renderActions={renderActions}
                />
                <PaginationComponent
                    currentPage={currentPage}
                    totalItems={filteredData.length}
                    itemsPerPage={itemsPerPage}
                    onPageChange={(page) => setCurrentPage(page)}
                    onItemsPerPageChange={(num) => setItemsPerPage(num)}
                />

            </div>
        </div>
    );
};

export default UserManagementPage;
