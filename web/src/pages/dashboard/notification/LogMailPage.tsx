import React, { useMemo, useState } from "react";
import { Spin, Input } from "antd";
import { SearchOutlined } from "@ant-design/icons";
import dayjs from "dayjs";

import TableComponent from "@/components/Table";
import type { TableColumn } from "@/components/Table";
import PaginationComponent from "@/components/Pagination";

import { useFilterLogMails } from "@/hooks/notification.hook";
import type { LogMailResource, FilterLogMails } from "@/types/notification.type";

const LogMailManagementPage: React.FC = () => {
    /* =======================
       STATE
       ======================= */
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(10);
    const [mailKeyword, setMailKeyword] = useState("");

    /* =======================
       FILTER PARAMS
       ======================= */
    const filterParams: FilterLogMails = useMemo(
        () => ({
            mailKeyword: mailKeyword || undefined,
            size: itemsPerPage,
            page: currentPage - 1, 

        }),
        [mailKeyword, currentPage, itemsPerPage]
    );

    /* =======================
       FETCH DATA
       ======================= */
    const { data, isLoading } = useFilterLogMails(filterParams);

    const logMails = data?.contents ?? [];
    const totalItems = data?.totalElements ?? logMails.length;
    /* =======================
       PAGINATION (FE)
       ======================= */
    const startIndex = (currentPage - 1) * itemsPerPage;
    // const paginatedData = logMails.slice(
    //     startIndex,
    //     startIndex + itemsPerPage
    // );

    /* =======================
       TABLE COLUMNS
       ======================= */
    const columns: TableColumn<LogMailResource>[] = [
        {
            key: "stt",
            label: "STT",
            align: "center",
            render: (_item, index) => startIndex + index + 1,
        },
        {
            key: "mail",
            label: "Email",
            render: (item) => item.mail || "-",
        },
        {
            key: "subject",
            label: "Tiêu đề",
            render: (item) => item.subject || "-",
        },
        {
            key: "template",
            label: "Template",
            render: (item) => item.template || "-",
        },
        {
            key: "sendAt",
            label: "Thời gian gửi",
            align: "center",
            render: (item) =>
                item.sendAt
                    ? dayjs(item.sendAt).format("DD/MM/YYYY HH:mm")
                    : "-",
        },
    ];

    return (
        <div className="flex flex-col space-y-6 p-5">
            {/* HEADER */}
            <div className="flex items-center justify-between flex-wrap gap-4">
                <h2 className="text-2xl font-bold">Log gửi email</h2>
            </div>

            {/* FILTER */}
            <div className="bg-white p-4 rounded-lg shadow-md space-y-4">
                <div className="flex flex-wrap items-center gap-4 justify-between">
                    <Input
                        placeholder="Tìm theo email"
                        prefix={<SearchOutlined />}
                        allowClear
                        className="w-[280px]"
                        value={mailKeyword}
                        onChange={(e) => {
                            setCurrentPage(1);
                            setMailKeyword(e.target.value);
                        }}
                    />
                </div>

                {/* TABLE */}
                {isLoading ? (
                    <div className="flex justify-center py-10">
                        <Spin />
                    </div>
                ) : (
                    <>
                        <TableComponent<LogMailResource>
                            columns={columns}
                            dataSource={logMails}
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

export default LogMailManagementPage;
