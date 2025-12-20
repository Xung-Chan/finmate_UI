import React, { useState, useMemo } from "react";
import { Tabs } from "antd";
import { EyeOutlined, FileExcelOutlined } from "@ant-design/icons";
import { useNavigate } from "@tanstack/react-router";
import { toast } from "react-hot-toast";

import { SearchComponent } from "@/components/Search";
import CustomButton from "@/components/Button";
import TableComponent from "@/components/Table";
import PaginationComponent from "@/components/Pagination";
import { colors } from "@/theme/color";
import { transactionDetailRoute } from "@/routes/dashboard";
import type { Transaction } from "@/types/transaction.type";

const TransactionManagementPage: React.FC = () => {
  const navigate = useNavigate();

  const [searchValue, setSearchValue] = useState("");
  const [filterStatus, setFilterStatus] = useState<
    "all" | "success" | "pending" | "failed"
  >("all");
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(10);

  // ================= MOCK DATA =================
  const data: Transaction[] = [
    {
      id: "1",
      userName: "Nguyễn Văn A",
      amount: 500000,
      type: "DEPOSIT",
      status: "success",
      createdAt: "2024-10-01 08:30",
    },
    {
      id: "2",
      userName: "Trần Thị B",
      amount: 200000,
      type: "PAYMENT",
      status: "pending",
      createdAt: "2024-10-02 10:15",
    },
    {
      id: "3",
      userName: "Lê Văn C",
      amount: 150000,
      type: "REFUND",
      status: "failed",
      createdAt: "2024-10-03 14:20",
    },
  ];

  // ================= FILTER + SEARCH =================
  const filteredData = useMemo(() => {
    return data.filter((t) => {
      const matchStatus =
        filterStatus === "all" ? true : t.status === filterStatus;

      const matchSearch =
        t.id.toLowerCase().includes(searchValue.toLowerCase());

      return matchStatus && matchSearch;
    });
  }, [searchValue, filterStatus, data]);

  const startIndex = (currentPage - 1) * itemsPerPage;
  const paginatedData = filteredData.slice(
    startIndex,
    startIndex + itemsPerPage
  );

  // ================= RENDER STATUS =================
  const renderStatus = (status: Transaction["status"]) => {
    return (
      <span
        className={`inline-block px-3 py-1 rounded-md text-sm font-medium
          ${
            status === "success"
              ? "bg-[#D1FADF] text-[#027A48]"
              : status === "failed"
              ? "bg-[#FEE4E2] text-[#B42318]"
              : "bg-[#FEF0C7] text-[#B54708]"
          }`}
      >
        {status === "success"
          ? "Thành công"
          : status === "failed"
          ? "Thất bại"
          : "Đang xử lý"}
      </span>
    );
  };

  // ================= TABLE COLUMNS =================
  const columns: { key: string; align?: "left" | "center" | "right"; label: string; render?: (item: Transaction) => React.ReactNode }[] = [
    { key: "stt", label: "STT", render: (item: Transaction) =>  filteredData.findIndex((t) => t.id === item.id) + 1 },
    { key: "id", label: "Mã giao dịch" },
    { key: "userName", label: "Khách hàng" },
    {
      key: "amount",
      label: "Số tiền",
      render: (item: Transaction) =>
        `${item.amount?.toLocaleString()} ₫`,
    },
    {
      key: "type",
      label: "Loại giao dịch",
      render: (item: Transaction) =>
        item.type === "DEPOSIT"
          ? "Nạp tiền"
          : item.type === "PAYMENT"
          ? "Thanh toán"
          : "Hoàn tiền",
    },
    {
      key: "status",
      label: "Trạng thái",
      align: "center",
      render: (item: Transaction) => renderStatus(item.status),
    },
    { key: "createdAt", label: "Thời gian" },
    { key: "actions", label: "Hành động" },
  ];

  // ================= ACTIONS =================
  const renderActions = (item: Transaction) => (
    <EyeOutlined
      onClick={() => {
        navigate({ to: transactionDetailRoute.to, params: { id: item.id } });
      }}
      style={{
        fontSize: 18,
        color: colors.blue.b3,
        cursor: "pointer",
      }}
    />
  );

  const handleExportExcel = () =>
    toast.success("Đang xuất danh sách giao dịch...");

  // ================= RENDER =================
  return (
    <div className="flex flex-col space-y-6 p-5">
      {/* HEADER */}
      <div className="flex flex-wrap justify-start space-x-4">
        <h2 className="text-2xl text-black font-bold mb-4">
          Quản lý giao dịch
        </h2>
        <SearchComponent
          placeholder="Tìm kiếm theo mã giao dịch..."
          value={searchValue}
          onChange={(v) => setSearchValue(v)}
          onSearch={(v) => setSearchValue(v)}
        />
      </div>

      <div className="flex flex-col space-y-4 bg-white p-4 rounded-lg shadow-md">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <Tabs
            activeKey={filterStatus}
            onChange={(key) => setFilterStatus(key as any)}
            items={[
              { key: "all", label: "Tất cả" },
              { key: "success", label: "Thành công" },
              { key: "pending", label: "Đang xử lý" },
              { key: "failed", label: "Thất bại" },
            ]}
            className="custom-tabs"
          />

          <CustomButton
            color={colors.green.g1}
            hoverColor={colors.green.g3}
            icon={<FileExcelOutlined />}
            label="Xuất Excel"
            onClick={handleExportExcel}
          />
        </div>

        {/* TABLE */}
        <TableComponent<Transaction>
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

export default TransactionManagementPage;
