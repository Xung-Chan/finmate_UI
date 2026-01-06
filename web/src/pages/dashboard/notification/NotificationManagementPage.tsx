import React, { useMemo, useState } from "react";
import { Spin, DatePicker, Select } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { useNavigate } from "@tanstack/react-router";

import TableComponent from "@/components/Table";
import type { TableColumn } from "@/components/Table";
import PaginationComponent from "@/components/Pagination";
import CustomButton from "@/components/Button";

import { useFilterNotifications } from "@/hooks/notification.hook";
import type { NotificationResource } from "@/types/notification.type";
import dayjs, { Dayjs } from "dayjs";

const NotificationManagementPage: React.FC = () => {
    const navigate = useNavigate();

    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(10);
    const getDefaultRange = (): [Dayjs, Dayjs] => {
        const today = dayjs();
        return [today.subtract(7, "day"), today];
    };
    const [type, setType] = useState<"SYSTEM" | "PERSONAL">("SYSTEM");

    const [dateRange, setDateRange] = useState<[Dayjs, Dayjs]>(
        getDefaultRange()
    );

    /* =======================
       FILTER PARAMS
       ======================= */
    const filterParams = useMemo(
        () => ({
            fromDate: dateRange[0]?.format("YYYY-MM-DD"),
            toDate: dateRange[1]?.format("YYYY-MM-DD"),
            type: type,
        }),
        [dateRange, type]
    );

    /* =======================
       FETCH DATA
       ======================= */
    const { data, isLoading } = useFilterNotifications(filterParams);

    const notifications = data?.contents ?? [];
    const totalItems = data?.totalElements ?? notifications.length;

    /* =======================
       PAGINATION (FE)
       ======================= */
    const startIndex = (currentPage - 1) * itemsPerPage;
    const paginatedData = notifications.slice(
        startIndex,
        startIndex + itemsPerPage
    );

    /* =======================
       TABLE COLUMNS
       ======================= */
    const columns: TableColumn<NotificationResource>[] = [
        {
            key: "stt",
            label: "STT",
            align: "center",
            render: (_item, index) => startIndex + index + 1,
        },
        {
            key: "title",
            label: "Tiêu đề",
            render: (item) => item.title || "-",
        },
        {
            key: "content",
            label: "Nội dung",
            render: (item) =>
                item.content ? (
                    <span className="line-clamp-2">{item.content}</span>
                ) : (
                    "-"
                ),
        },
        {
            key: "createdAt",
            label: "Ngày tạo",
            align: "center",
            render: (item) =>
                item.createdAt
                    ? dayjs(item.createdAt).format("DD/MM/YYYY HH:mm")
                    : "-",
        },
        {
            key: "read",
            label: "Trạng thái",
            align: "center",
            render: (item) =>
                item.read ? (
                    <span className="text-green-600 font-medium">Đã đọc</span>
                ) : (
                    <span className="text-gray-400">Chưa đọc</span>
                ),
        },
    ];

    return (
        <div className="flex flex-col space-y-6 p-5">
            {/* HEADER */}
            <div className="flex items-center justify-between flex-wrap gap-4">
                <h2 className="text-2xl font-bold">Quản lý thông báo</h2>


            </div>

            {/* FILTER */}
            <div className="bg-white p-4 rounded-lg shadow-md space-y-4">
                <div className="flex flex-wrap items-center gap-4 justify-between">
                    <div className="flex gap-3 flex-wrap">
                        <DatePicker.RangePicker
                            value={dateRange}
                            onChange={(values) => {
                                if (!values) return;
                                setCurrentPage(1);
                                setDateRange(values as [Dayjs, Dayjs]);
                            }}
                        />

                        <Select
                            value={type}
                            placeholder="Loại thông báo"
                            className="w-[180px]"
                            allowClear
                            onChange={(value) => {
                                setCurrentPage(1);
                                setType(value);
                            }}
                            options={[
                                { value: "SYSTEM", label: "Hệ thống" },
                                { value: "PERSONAL", label: "Cá nhân" },
                            ]}
                        />
                    </div>

                    <CustomButton
                        label="Thêm thông báo"
                        icon={<PlusOutlined />}
                        onClick={() => navigate({ to: "/manage/notifications/create" })}
                    />
                </div>


                {/* TABLE */}
                {isLoading ? (
                    <div className="flex justify-center py-10">
                        <Spin />
                    </div>
                ) : (
                    <>
                        <TableComponent<NotificationResource>
                            columns={columns}
                            dataSource={paginatedData}
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

export default NotificationManagementPage;
