import React, { useState, useMemo, useEffect } from "react";
import dayjs from 'dayjs';
import { Spin, Tabs, DatePicker, Select } from "antd";
import { EyeOutlined, FileExcelOutlined } from "@ant-design/icons";
import { toast } from "react-hot-toast";

import CustomButton from "@/components/Button";
import TableComponent, { type TableColumn } from "@/components/Table";
import PaginationComponent from "@/components/Pagination";
import { colors } from "@/theme/color";
import { ActionType, TransactionStatus } from "@/enum/status";
import TransactionDetailModal from "./TransactionDetailModal";
import type { TransactionResource } from "@/types/transaction.type";
import { useFilterTransactions } from "@/hooks/transaction.hook";
import { useFilterWallets } from "@/hooks/wallet.hook";
import { WalletStatus } from "@/enum/status";
const TransactionManagementPage: React.FC = () => {

  const [detailModalOpen, setDetailModalOpen] = useState(false);
  const [selectedTransaction, setSelectedTransaction] = useState<TransactionResource | null>(null);
  const { RangePicker } = DatePicker;
  const [filterStatus, setFilterStatus] = useState<TransactionStatus | "">("");
  const [sortBy, setSortBy] = useState<string>("");
  const todayStr = new Date().toISOString().slice(0, 10);
  const last30 = new Date();
  last30.setDate(last30.getDate() - 30);
  const last30Str = last30.toISOString().slice(0, 10);

  //initial state
  const [accountType, setAccountType] = useState<"WALLET" | "PAY_LATER">("WALLET");

  const [accountNumber, setAccountNumber] = useState<string | undefined>();
  const [actionType, setActionType] = useState<ActionType | undefined>();

  const [fromDate, setFromDate] = useState<string | undefined>(last30Str);
  const [toDate, setToDate] = useState<string | undefined>(todayStr);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(10);

  const walletFilterParams = useMemo(
    () => ({
      status: WalletStatus.ACTIVE,
    }),
    []
  );

  const { data: walletData, isLoading: isLoadingWallets } =
    useFilterWallets(walletFilterParams);

  const walletOptions =
    walletData?.contents?.map((w) => ({
      label: `${w.walletNumber} (${w.balance?.toLocaleString()} ₫)`,
      value: w.walletNumber,
    })) ?? [];

  // ================= FILTER + SEARCH =================
  const filterParams = useMemo(
    () => ({
      accountNumber,
      accountType,
      type: actionType,
      fromDate,
      toDate,
      sortBy: sortBy || "processed_at_desc",
      size: itemsPerPage,
      page: currentPage - 1,
    }),
    [
      accountNumber,
      accountType,
      actionType,
      fromDate,
      toDate,
      sortBy,
      currentPage,
      itemsPerPage,
    ]
  );

  ;

  const { data, isLoading } = useFilterTransactions(filterParams);

  const transactions: TransactionResource[] = data?.contents ?? [];
  const totalItems = data?.totalElements ?? transactions.length;
  //  Pagination
  const startIndex = (currentPage - 1) * itemsPerPage;
  // const paginatedData = transactions?.slice(
  //   startIndex,
  //   startIndex + itemsPerPage
  // );
  useEffect(() => {
    setCurrentPage(1);
  }, [
    accountNumber,
    accountType,
    actionType,
    fromDate,
    toDate,
    sortBy,
  ]);

  // ================= RENDER STATUS =================
  const renderStatus = (status?: TransactionStatus | "") => {
    if (!status) return "-";

    const statusConfig: Record<
      TransactionStatus,
      { label: string; bg: string; color: string }
    > = {
      COMPLETED: {
        label: "Hoàn thành",
        bg: "#D1FADF",      // green-100
        color: "#027A48",   // green-700
      },
      PROCESSING: {
        label: "Đang xử lý",
        bg: "#E0F2FE",      // blue-100
        color: "#0369A1",   // blue-700
      },
      PENDING: {
        label: "Chờ xử lý",
        bg: "#FEF3C7",      // yellow-100
        color: "#92400E",   // yellow-700
      },
      FAILED: {
        label: "Thất bại",
        bg: "#FEE4E2",      // red-100
        color: "#B42318",   // red-700
      },
      CANCELED: {
        label: "Đã hủy",
        bg: "#F3F4F6",      // gray-100
        color: "#374151",   // gray-700
      },
    };

    const config = statusConfig[status];

    return (
      <span
        className="inline-flex items-center px-3 py-1 rounded-md text-sm font-medium"
        style={{
          backgroundColor: config.bg,
          color: config.color,
        }}
      >
        {config.label}
      </span>
    );
  };

  // ================= TABLE COLUMNS =================
  const columns: TableColumn<TransactionResource>[] = [
    {
      key: "stt",
      label: "STT",
      align: "center",
      render: (_item, index) => startIndex + index + 1,
    },
    {
      key: "id",
      label: "Mã giao dịch",
      render: (item) => item.id.slice(0, 5).toLocaleUpperCase(),
    },
    {
      key: "sourceAccountNumber",
      label: "Ví nguồn",
      render: (item) => item.sourceAccountNumber || "-",
    },
    {
      key: "toWalletNumber",
      label: "Ví nhận",
      render: (item) => item.toWalletNumber || "-",
    },
    {
      key: "amount",
      label: "Số tiền",
      align: "right",
      render: (item) =>
        item.amount
          ? `${item.amount.toLocaleString()} ₫`
          : "-",
    },
    {
      key: "status",
      label: "Trạng thái",
      align: "center",
      render: (item) => renderStatus(item.status),
    },
    {
      key: "description",
      label: "Mô tả",
      render: (item) => item.description || "-",
    },
    {
      key: "processedAt",
      label: "Thời gian xử lý",
      render: (item) =>
        item.processedAt
          ? new Date(item.processedAt).toLocaleString("vi-VN")
          : "-",
    },
    {
      key: "actions",
      label: "Hành động",
      align: "center",
    },
  ];

  // ================= ACTIONS =================
  const renderActions = (item: TransactionResource) => (
    <EyeOutlined
      onClick={() => {
        setSelectedTransaction(item);
        setDetailModalOpen(true);
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
      <h2 className="text-2xl text-black font-bold mb-4">
        Quản lý giao dịch
      </h2>
      <div className="flex flex-col space-y-4 bg-white p-4 rounded-lg shadow-md">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <Tabs
            activeKey={filterStatus}
            onChange={(key) => setFilterStatus(key as TransactionStatus)}
            items={[
              { key: "", label: "Tất cả" },
              { key: TransactionStatus.COMPLETED, label: "Thành công" },
              { key: TransactionStatus.PROCESSING, label: "Đang xử lý" },
              { key: TransactionStatus.PENDING, label: "Chờ xử lý" },
              { key: TransactionStatus.FAILED, label: "Thất bại" },
              { key: TransactionStatus.CANCELED, label: "Đã hủy" },
            ]}
          />

          <div className="flex flex-wrap items-center gap-4">
            {/* SORT */}
            <Select
              value={sortBy || undefined}
              placeholder="Sắp xếp"
              style={{ width: 160 }}
              allowClear
              onChange={(value) => setSortBy(value)}
              options={[
                { value: "processed_at_desc", label: "Mới nhất" },

              ]}
            />

            {/* DATE RANGE */}
            <RangePicker
              format="YYYY-MM-DD"
              defaultValue={[dayjs(last30Str), dayjs(todayStr)]}
              onChange={(dates) => {
                if (!dates) {
                  setFromDate(undefined);
                  setToDate(undefined);
                  return;
                }

                setFromDate(dates[0]?.format("YYYY-MM-DD"));
                setToDate(dates[1]?.format("YYYY-MM-DD"));
              }}
            />
            {/* ACCOUNT TYPE */}
            <Select
              value={accountType}
              style={{ width: 160 }}
              onChange={(v) => {
                setAccountType(v);
                setAccountNumber(undefined); // reset khi đổi loại
              }}
              options={[
                { value: "WALLET", label: "Ví thường" },
                { value: "PAY_LATER", label: "Ví trả sau" },
              ]}
            />

            {/* ACCOUNT NUMBER */}
            <Select
              value={accountNumber}
              placeholder={
                accountType === "WALLET"
                  ? "Chọn ví"
                  : "Pay Later (chưa hỗ trợ)"
              }
              style={{ width: 160 }}
              allowClear
              loading={isLoadingWallets}
              disabled={accountType !== "WALLET"}
              options={walletOptions}
              onChange={(v) => setAccountNumber(v)}
            />
            <Select
              value={actionType}
              placeholder="Loại giao dịch"
              allowClear
              style={{ width: 160 }}
              onChange={(v) => setActionType(v)}
              options={[
                { value: "CASH_DEPOSIT", label: "Nạp tiền mặt" },
                { value: "CASH_WITHDRAW", label: "Rút tiền" },
                { value: "TRANSFER", label: "Chuyển tiền" },
                { value: "BILL_PAYMENT", label: "Thanh toán hóa đơn" },
                { value: "E_GATEWAY_DEPOSIT", label: "Nạp qua cổng" },
                { value: "PAY_LATER_REPAYMENT", label: "Trả Pay Later" },
                { value: "BILL_UTILITY_PAYMENT", label: "Thanh toán tiện ích" },
              ]}
            />

            {/* RESET */}
            <CustomButton
              label="Xóa lọc"
              color={colors.blue.b2}
              hoverColor={colors.blue.b3}
              onClick={() => {
                setFilterStatus("");
                setSortBy("");
                setFromDate(undefined);
                setToDate(undefined);
              }}
            />
          </div>

          <CustomButton
            color={colors.green.g1}
            hoverColor={colors.green.g3}
            icon={<FileExcelOutlined />}
            label="Xuất Excel"
            onClick={handleExportExcel}
          />
        </div>

        {/* TABLE */}
        {isLoading ? (
          <div className="flex justify-center py-10">
            <Spin />
          </div>
        ) : (
          <>
            <TableComponent<TransactionResource>
              columns={columns}
              dataSource={transactions}
              renderActions={renderActions}
            />

            <PaginationComponent
              currentPage={currentPage}
              totalItems={totalItems}
              itemsPerPage={itemsPerPage}
              onPageChange={setCurrentPage}
              onItemsPerPageChange={setItemsPerPage}
            />
            <TransactionDetailModal
              open={detailModalOpen}
              transaction={selectedTransaction}
              onClose={() => {
                setDetailModalOpen(false);
                setSelectedTransaction(null);
              }}
            />
          </>
        )}
      </div>
    </div>
  );
};

export default TransactionManagementPage;
