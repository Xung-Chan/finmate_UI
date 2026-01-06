import React, { useMemo, useState } from "react";
import { Spin, DatePicker, Space, Select } from "antd";
import { EditOutlined } from "@ant-design/icons";


import TableComponent from "@/components/Table";
import PaginationComponent from "@/components/Pagination";

import type { TableColumn } from "@/components/Table";
import type { PayLaterApplicationResource } from "@/types/walltet.type";
import { useFilterPayLaterApplications } from "@/hooks/wallet.hook";
import type { Dayjs } from "dayjs";
import dayjs from "dayjs";
import PayLaterApplicationDetailModal from "./PaylaterApplicationDetailModal";
import { colors } from "@/theme/color";
import type { PayLaterApplicationStatus } from "@/enum/status";
import { PayLaterApplicationType } from "@/enum/status";

const PayLaterApplicationPage: React.FC = () => {
    const getDefaultRange = (): [Dayjs, Dayjs] => {
        const today = dayjs();
        return [today.subtract(7, "day"), today];
    };

    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(10);
    const [dateRange, setDateRange] = useState<[Dayjs, Dayjs]>(
        getDefaultRange()
    );
    const [statusFilter, setStatusFilter] = useState<PayLaterApplicationStatus | undefined>(undefined);
    const [typeFilter, setTypeFilter] = useState<PayLaterApplicationType | undefined>(undefined);
    const [selectedApplication, setSelectedApplication] =
        useState<PayLaterApplicationResource | null>(null);
    const [openDetail, setOpenDetail] = useState(false);

    const filterParams = useMemo(
        () => ({
            fromDate: dateRange[0]?.format("YYYY-MM-DD"),
            toDate: dateRange[1]?.format("YYYY-MM-DD"),
            status: statusFilter || undefined,
            type: typeFilter || undefined,
            page: currentPage - 1,
            size: itemsPerPage,
        }),
        [dateRange, statusFilter, typeFilter, currentPage, itemsPerPage]

    );

    const { data, isLoading } = useFilterPayLaterApplications(filterParams);

    const applications: PayLaterApplicationResource[] =
        data?.contents ?? [];
    const totalItems = data?.totalElements ?? 0;
    const renderActions = (item: PayLaterApplicationResource) => (
        <Space size={12}>
            <EditOutlined
                title="Xem chi tiết"
                style={{ fontSize: 18, color: colors.orange.o1, cursor: "pointer" }}
                onClick={() => {
                    setSelectedApplication(item);
                    setOpenDetail(true);
                }}
            />

            <PayLaterApplicationDetailModal
                open={openDetail}
                application={selectedApplication}
                onClose={() => {
                    setOpenDetail(false);
                    setSelectedApplication(null);
                }}
            />
        </Space>
    );
    const columns: TableColumn<PayLaterApplicationResource>[] = [
        {
            key: "stt",
            label: "STT",
            align: "center",
            render: (_i, idx) =>
                (currentPage - 1) * itemsPerPage + idx + 1,
        },
        { key: "username", label: "Người dùng" },
        {
            key: "requestedCreditLimit",
            label: "Hạn mức yêu cầu",
            align: "right",
            render: (i) => {

                if (i.type === PayLaterApplicationType.SUSPEND_REQUEST) {
                    return "-";
                }
                return i.requestedCreditLimit?.toLocaleString("vi-VN") + " ₫"
            }
        },
        {
            key: "approvedLimit",
            label: "Hạn mức duyệt",
            align: "right",

        },
        {
            key: "type",
            label: "Loại yêu cầu",
            align: "center",
            render: (i) => {
                switch (i.type) {
                    case PayLaterApplicationType.ACTIVATION:
                        return "Kích hoạt ví trả sau";
                    case PayLaterApplicationType.LIMIT_ADJUSTMENT:
                        return "Điều chỉnh hạn mức tín dụng";
                    case PayLaterApplicationType.SUSPEND_REQUEST:
                        return "Yêu cầu tạm ngưng ví trả sau";
                    default:
                        return "-";
                }
            }

        },
        { key: "appliedAt", label: "Ngày đăng ký", render: (i) => i.appliedAt ? dayjs(i.appliedAt).format("DD/MM/YYYY") : "-" },
        { key: "status", label: "Trạng thái", render: (i) => renderStatus(i.status) },
        {
            key: "actions",
            label: "Hành động",
            align: "center",
        }

    ];
    const renderStatus = (status?: PayLaterApplicationStatus) => {
        if (!status) return "-";

        const map: Record<PayLaterApplicationStatus, { label: string; bg: string; color: string }> =
        {
            PENDING: {
                label: "Chờ duyệt",
                bg: "#FEF0C7",
                color: "#B54708",
            },
            APPROVED: {
                label: "Đã duyệt",
                bg: "#D1FADF",
                color: "#027A48",
            },
            REJECTED: {
                label: "Từ chối",
                bg: "#FEE4E2",
                color: "#B42318",
            },
            CANCELED: {
                label: "Hủy",
                bg: "#F3F4F6",
                color: "#374151",
            },
        };

        const config = map[status];

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

    return (
        <div className="flex flex-col space-y-6 p-5">
            <h2 className="text-2xl font-bold">
                Đơn đăng ký ví trả sau
            </h2>

            <div className="flex flex-col bg-white p-4 rounded-lg shadow-md space-y-4">
                <div className="flex justify-end gap-2 items-center">
                    <Select
                        allowClear
                        placeholder="Trạng thái"
                        style={{ width: 200 }}
                        value={statusFilter}
                        onChange={(v) => { setCurrentPage(1); setStatusFilter(v); }}
                        options={[
                            { value: 'PENDING', label: 'Chờ duyệt' },
                            { value: 'APPROVED', label: 'Đã duyệt' },
                            { value: 'REJECTED', label: 'Từ chối' },
                            { value: 'CANCELED', label: 'Hủy' },
                        ]}
                    />

                    <Select
                        allowClear
                        placeholder="Loại yêu cầu"
                        style={{ width: 260 }}
                        value={typeFilter}
                        onChange={(v) => { setCurrentPage(1); setTypeFilter(v); }}
                        options={[
                            { value: PayLaterApplicationType.ACTIVATION, label: 'Kích hoạt ví trả sau' },
                            { value: PayLaterApplicationType.LIMIT_ADJUSTMENT, label: 'Điều chỉnh hạn mức tín dụng' },
                            { value: PayLaterApplicationType.SUSPEND_REQUEST, label: 'Yêu cầu tạm ngưng ví trả sau' },
                        ]}
                    />
                    <DatePicker.RangePicker
                        value={dateRange}
                        allowClear={false}
                        onChange={(values) => {
                            if (!values) return;
                            setCurrentPage(1);
                            setDateRange(values as [Dayjs, Dayjs]);
                        }}
                    />
                </div>


                {isLoading ? (
                    <div className="flex justify-center py-10">
                        <Spin />
                    </div>
                ) : (
                    <>
                        <TableComponent
                            columns={columns}
                            dataSource={applications}
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

export default PayLaterApplicationPage;
