import React, { useState, useMemo } from "react";
import { Tabs, Select, App } from "antd";
import { SearchComponent } from "@/components/Search";
import { SettingOutlined } from "@ant-design/icons";
import TableComponent from "@/components/Table";
import PaginationComponent from "@/components/Pagination";
import { colors } from "@/theme/color";
import { toast } from "react-hot-toast";

type Role = "user" | "staff" | "admin";
type Status = "active" | "locked";

type RoleUser = {
    id: number;
    email: string;
    fullName: string;
    role: Role;
    status: Status;
};

const RoleManagementPage: React.FC = () => {
    const modal = App.useApp().modal;

    const [searchValue, setSearchValue] = useState("");
    const [filterRole, setFilterRole] = useState<"all" | Role>("all");
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(10);

    // ================= MOCK DATA =================
    const data: RoleUser[] = [
        { id: 1, email: "user@gmail.com", fullName: "Nguyễn Văn A", role: "user", status: "active" },
        { id: 2, email: "staff@gmail.com", fullName: "Trần Thị B", role: "staff", status: "active" },
        { id: 3, email: "admin@gmail.com", fullName: "Lê Văn C", role: "admin", status: "active" },
        { id: 4, email: "locked@gmail.com", fullName: "Phạm Văn D", role: "user", status: "locked" },
    ];

    // ================= FILTER =================
    const filteredData = useMemo(() => {
        return data.filter((u) => {
            const matchRole = filterRole === "all" ? true : u.role === filterRole;
            const matchSearch =
                u.fullName.toLowerCase().includes(searchValue.toLowerCase()) ||
                u.email.toLowerCase().includes(searchValue.toLowerCase());
            return matchRole && matchSearch;
        });
    }, [data, filterRole, searchValue]);

    const startIndex = (currentPage - 1) * itemsPerPage;
    const paginatedData = filteredData.slice(startIndex, startIndex + itemsPerPage);

    // ================= RENDER =================
    const renderRole = (role: Role) => {
        const map = {
            user: { label: "User", color: "bg-gray-100 text-gray-700" },
            staff: { label: "Staff", color: "bg-blue-100 text-blue-700" },
            admin: { label: "Admin", color: "bg-red-100 text-red-700" },
        };

        return (
            <span className={`px-3 py-1 rounded-md text-sm font-medium ${map[role].color}`}>
                {map[role].label}
            </span>
        );
    };

    const renderStatus = (status: Status) => (
        <span
            className={`px-3 py-1 rounded-md text-sm font-medium ${status === "active"
                    ? "bg-[#D1FADF] text-[#027A48]"
                    : "bg-[#FEE4E2] text-[#B42318]"
                }`}
        >
            {status === "active" ? "Hoạt động" : "Bị khóa"}
        </span>
    );

    // ================= ACTION =================
    const openAssignRoleModal = (user: RoleUser) => {
        let selectedRole: Role = user.role;

        modal.confirm({
            title: "Phân quyền người dùng",
            content: (
                <div className="space-y-2">
                    <div className="text-sm text-gray-600">
                        Người dùng: <b>{user.fullName}</b>
                    </div>
                    <Select
                        defaultValue={user.role}
                        style={{ width: "100%" }}
                        onChange={(v) => (selectedRole = v)}
                        options={[
                            { label: "User", value: "user" },
                            { label: "Staff", value: "staff" },
                            { label: "Admin", value: "admin" },
                        ]}
                    />
                </div>
            ),
            okText: "Lưu",
            cancelText: "Hủy",
            onOk: () => {
                toast.success(`Đã cập nhật quyền thành ${selectedRole}`);
                // TODO: gọi API update role
            },
        });
    };

    // ================= COLUMNS =================
    const columns = [
        { key: "email", label: "Email" },
        { key: "fullName", label: "Họ và tên" },
        { key: "role", label: "Quyền", render: (i: RoleUser) => renderRole(i.role) },
        { key: "status", label: "Trạng thái", render: (i: RoleUser) => renderStatus(i.status) },
        {
            key: "actions",
            label: "Hành động",
            align: "center" as const,
        },
    ];

    const renderActions = (item: RoleUser) => (
        <SettingOutlined
            style={{
                fontSize: 18,
                cursor: "pointer",
                color: colors.blue.b3,
            }}
            onClick={() => openAssignRoleModal(item)}
        />
    );

    // ================= UI =================
    return (
        <div className="flex flex-col space-y-6 p-5">
            <div className="flex flex flex-wrap justify-start space-x-4">
                <h2 className="text-2xl font-bold text-black">Phân quyền người dùng</h2>

                <div className="flex items-center gap-4">
                    <SearchComponent
                        placeholder="Tìm theo tên, email..."
                        value={searchValue}
                        onChange={setSearchValue}
                        onSearch={setSearchValue}
                    />
                </div>
            </div>

            <div className="bg-white p-4 rounded-lg shadow-md space-y-4">
                <Tabs
                    activeKey={filterRole}
                    onChange={(key) => setFilterRole(key as any)}
                    items={[
                        { key: "all", label: "Tất cả" },
                        { key: "user", label: "User" },
                        { key: "staff", label: "Staff" },
                        { key: "admin", label: "Admin" },
                    ]}
                    className="custom-tabs"
                />

                <TableComponent<RoleUser>
                    columns={columns}
                    dataSource={paginatedData}
                    renderActions={renderActions}
                />

                <PaginationComponent
                    currentPage={currentPage}
                    totalItems={filteredData.length}
                    itemsPerPage={itemsPerPage}
                    onPageChange={setCurrentPage}
                    onItemsPerPageChange={setItemsPerPage}
                />
            </div>
        </div>
    );
};

export default RoleManagementPage;
