import React, { useMemo, useState } from "react";
import { Upload, App, Space } from "antd";
import { InboxOutlined, EyeOutlined, DeleteOutlined } from "@ant-design/icons";
import { toast } from "react-hot-toast";

import Section from "@/components/Section";
import TableComponent from "@/components/Table";
import PaginationComponent from "@/components/Pagination";
import { colors } from "@/theme/color";

type TrainingStatus = "PROCESSING" | "TRAINED" | "FAILED";

type TrainingFile = {
    id: number;
    fileName: string;
    size: number;
    uploadedBy: string;
    createdAt: string;
    status: TrainingStatus;
};

const formatSize = (bytes: number) =>
    bytes > 1024 * 1024
        ? `${(bytes / 1024 / 1024).toFixed(2)} MB`
        : `${(bytes / 1024).toFixed(2)} KB`;

const renderStatus = (status: TrainingStatus) => {
    const map = {
        PROCESSING: {
            label: "Đang xử lý",
            className: "bg-[#FEF0C7] text-[#B54708]",
        },
        TRAINED: {
            label: "Đã huấn luyện",
            className: "bg-[#D1FADF] text-[#027A48]",
        },
        FAILED: {
            label: "Lỗi",
            className: "bg-[#FEE4E2] text-[#B42318]",
        },
    };

    return (
        <span
            className={`inline-block px-3 py-1 rounded-md text-sm font-medium ${map[status].className}`}
        >
            {map[status].label}
        </span>
    );
};

const AiTrainingDataPage: React.FC = () => {
    const modal = App.useApp().modal;

    // ================= MOCK DATA =================
    const [data, setData] = useState<TrainingFile[]>([
        {
            id: 1,
            fileName: "training_users.csv",
            size: 345678,
            uploadedBy: "Admin",
            createdAt: "01/10/2024 09:00",
            status: "TRAINED",
        },
        {
            id: 2,
            fileName: "transaction_history.csv",
            size: 1256789,
            uploadedBy: "Staff A",
            createdAt: "05/10/2024 14:20",
            status: "PROCESSING",
        },
    ]);

    // ================= UPLOAD =================
    const handleUpload = (file: File) => {
        if (!file.name.endsWith(".csv")) {
            toast.error("Chỉ cho phép upload file .csv");
            return Upload.LIST_IGNORE;
        }

        toast.success("Upload file thành công, đang xử lý huấn luyện");

        setData((prev) => [
            {
                id: Date.now(),
                fileName: file.name,
                size: file.size,
                uploadedBy: "Admin",
                createdAt: new Date().toLocaleString(),
                status: "PROCESSING",
            },
            ...prev,
        ]);

        return false; // chặn auto upload
    };

    // ================= DELETE =================
    const handleDelete = (id: number) => {
        modal.confirm({
            title: "Xóa dữ liệu huấn luyện",
            content: "Bạn có chắc chắn muốn xóa file này?",
            okText: "Xóa",
            okButtonProps: { danger: true },
            cancelText: "Hủy",
            onOk: () => {
                setData((prev) => prev.filter((i) => i.id !== id));
                toast.success("Đã xóa file");
            },
        });
    };

    // ================= TABLE =================
    const columns = [
        { key: "fileName", label: "Tên file" },
        {
            key: "size",
            label: "Dung lượng",
            render: (i: TrainingFile) => formatSize(i.size),
        },
        { key: "uploadedBy", label: "Người tải lên" },
        { key: "createdAt", label: "Ngày tải lên" },
        {
            key: "status",
            label: "Trạng thái",
            render: (i: TrainingFile) => renderStatus(i.status),
        },
    ];

    const renderActions = (item: TrainingFile) => (
        <Space size={12}>
            <EyeOutlined
                className="cursor-pointer"
                style={{ color: colors.blue.b3 }}
                onClick={() => toast("Xem chi tiết file")}
            />
            <DeleteOutlined
                className="cursor-pointer"
                style={{ color: colors.red.r2 }}
                onClick={() => handleDelete(item.id)}
            />
        </Space>
    );

    return (
        <div className="p-5 space-y-6">
            <h2 className="text-2xl font-bold text-black">
                Quản lý dữ liệu huấn luyện AI
            </h2>

            {/* ================= UPLOAD ================= */}
            <div className="flex flex-col space-y-4 bg-white p-4 rounded-lg shadow-md">
                <Section
                    title="Tải lên dữ liệu huấn luyện"
                    description="Chấp nhận file .csv để cập nhật dữ liệu cho hệ thống AI gợi ý"
                >
                    <Upload.Dragger
                        multiple={false}
                        beforeUpload={handleUpload}
                        accept=".csv"
                        showUploadList={false}
                    >
                        <p className="ant-upload-drag-icon">
                            <InboxOutlined />
                        </p>
                        <p className="text-sm font-medium">
                            Kéo & thả file CSV vào đây hoặc click để chọn file
                        </p>
                        <p className="text-xs text-gray-500">
                            Chỉ hỗ trợ định dạng .csv
                        </p>
                    </Upload.Dragger>
                </Section>

                {/* ================= LIST ================= */}
                <Section title="Danh sách dữ liệu huấn luyện">
                    <TableComponent
                        columns={columns}
                        dataSource={data}
                        renderActions={renderActions}
                    />

                    <PaginationComponent
                        currentPage={1}
                        totalItems={data.length}
                        itemsPerPage={10}
                        onPageChange={() => { }}
                        onItemsPerPageChange={() => { }}
                    />
                </Section>

            </div>
        </div>
    );
};

export default AiTrainingDataPage;
