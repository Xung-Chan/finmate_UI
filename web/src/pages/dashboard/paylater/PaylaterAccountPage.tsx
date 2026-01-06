import React, { useEffect, useMemo, useState } from "react";
import { Tabs, Space, Spin, Select } from "antd";
import { EditOutlined } from "@ant-design/icons";
import { useNavigate } from "@tanstack/react-router";
import TableComponent from "@/components/Table";
import PaginationComponent from "@/components/Pagination";
import { SearchComponent } from "@/components/Search";
import { colors } from "@/theme/color";

import type { TableColumn } from "@/components/Table";
import type { PayLaterResource } from "@/types/walltet.type";
import type { WalletStatus } from "@/enum/status";
import { useFilterPayLaters } from "@/hooks/wallet.hook";
import { paylaterDetailRoute } from "@/routes/dashboard";

const PayLaterAccountPage: React.FC = () => {
  const navigate = useNavigate();
  const [searchValue, setSearchValue] = useState("");
  const [status, setStatus] = useState<WalletStatus | "ALL">("ALL");
  const [sortBy, setSortBy] = useState<string>("");
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [debounced, setDebounced] = useState(searchValue);

  /* ================= DEBOUNCE ================= */
  useEffect(() => {
    const t = setTimeout(() => setDebounced(searchValue), 500);
    return () => clearTimeout(t);
  }, [searchValue]);

  /* ================= FILTER PARAMS ================= */
  const filterParams = useMemo(
    () => ({
      keyword: debounced.trim() || "",
      status: status === "ALL" ? undefined : (status as WalletStatus),
      sort_by: sortBy || undefined,
      page: currentPage - 1,
      size: itemsPerPage,
    }),
    [debounced, status, sortBy, currentPage, itemsPerPage]
  );

  /* ================= API ================= */
  const { data, isLoading } = useFilterPayLaters(filterParams);

  const accounts: PayLaterResource[] = data?.contents ?? [];
  const totalItems = data?.totalElements ?? 0;

  /* ================= COLUMNS ================= */
  const columns: TableColumn<PayLaterResource>[] = [
    {
      key: "stt",
      label: "STT",
      align: "center",
      render: (_item, index) =>
        (currentPage - 1) * itemsPerPage + index + 1,
    },
    {
      key: "username",
      label: "Người dùng",
      render: (i) => i.username || "-",
    },
    {
      key: "payLaterAccountNumber",
      label: "Số TK PayLater",
      render: (i) => i.payLaterAccountNumber || "-",
    },
    {
      key: "creditLimit",
      label: "Hạn mức",
      align: "right",
      render: (i) =>
        i.creditLimit?.toLocaleString("vi-VN") + " ₫",
    },
    {
      key: "usedCredit",
      label: "Đã dùng",
      align: "right",
      render: (i) =>
        i.usedCredit?.toLocaleString("vi-VN") + " ₫",
    },
    {
      key: "status",
      label: "Trạng thái",
      align: "center",
      render: (i) => renderStatus(i.status),
    },
    {
      key: "actions",
      label: "Hành động",
      align: "center",
    }
  ];
  const renderActions = (item: PayLaterResource) => (
    <Space size={12}>
      <EditOutlined
        title="Xem chi tiết ví"
        style={{ fontSize: 18, color: colors.orange.o1, cursor: "pointer" }}
        onClick={() =>
          navigate({
            to: paylaterDetailRoute.to,
            params: { payLaterAccountNumber: item.payLaterAccountNumber },
          })
        }
      />
    </Space>
  );
  const renderStatus = (status?: WalletStatus) => {
    if (!status) return "-";

    const map: Record<WalletStatus, { label: string; bg: string; color: string }> =
    {
      PENDING: {
        label: "Chờ duyệt",
        bg: "#FEF0C7",
        color: "#B54708",
      },
      ACTIVE: {
        label: "Hoạt động",
        bg: "#D1FADF",
        color: "#027A48",
      },
      SUSPENDED: {
        label: "Tạm khóa",
        bg: "#FEE4E2",
        color: "#B42318",
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

      {/* SEARCH + SORT */}
      <div className="flex items-center gap-4 flex-wrap">
        <h2 className="text-2xl font-bold">Ví trả sau</h2>
        <SearchComponent
          placeholder="Tìm theo tên người dùng..."
          value={searchValue}
          onChange={setSearchValue}
          onSearch={setSearchValue}
        />
      </div>

      <div className="bg-white p-4 rounded-lg shadow-md space-y-4">
        {/* STATUS TABS */}
        <Tabs
          activeKey={status}
          onChange={(key) => {
            setCurrentPage(1);
            setStatus(key as WalletStatus);
          }}
          items={[
            { key: "ALL", label: "Tất cả" },
            { key: "PENDING", label: "Chờ duyệt" },
            { key: "ACTIVE", label: "Hoạt động" },
            { key: "SUSPENDED", label: "Tạm khóa" },
          ]}
        />
        <div className="flex justify-end">
          <Select
            allowClear
            placeholder="Sắp xếp"
            className="w-[200px]"
            onChange={setSortBy}
            options={[
              { value: "word_asc", label: "Tên A → Z" },
              { value: "word_desc", label: "Tên Z → A" },
            ]}
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
              dataSource={accounts}
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

export default PayLaterAccountPage;
