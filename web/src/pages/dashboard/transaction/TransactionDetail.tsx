import React, { useMemo } from "react";
import { ArrowLeftOutlined } from "@ant-design/icons";
import { useNavigate, useParams } from "@tanstack/react-router";
import { transactionManagementRoute } from "@/routes/dashboard";
import Section from "@/components/Section";
import CustomButton from "@/components/Button";
import { colors } from "@/theme/color";

type TransactionStatus = "success" | "pending" | "failed";
type TransactionType = "DEPOSIT" | "PAYMENT" | "REFUND";

type TransactionDetail = {
  id: number;
  code: string;
  userName: string;
  email: string;
  phone: string;
  amount: number;
  type: TransactionType;
  status: TransactionStatus;
  walletType: "NORMAL" | "CREDIT";
  createdAt: string;
  note?: string;
};

const formatMoney = (v: number) => `${v.toLocaleString()} ₫`;

const renderStatus = (status: TransactionStatus) => {
  const map = {
    success: {
      label: "Thành công",
      className: "bg-[#D1FADF] text-[#027A48]",
    },
    pending: {
      label: "Đang xử lý",
      className: "bg-[#FEF0C7] text-[#B54708]",
    },
    failed: {
      label: "Thất bại",
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

const TransactionDetailPage: React.FC = () => {
  const navigate = useNavigate();
  const { transactionId } = useParams({ strict: false });

  // ================= MOCK DETAIL =================
  const transaction = useMemo<TransactionDetail>(
    () => ({
      id: Number(transactionId),
      code: "TXN-001",
      userName: "Nguyễn Văn A",
      email: "user1@gmail.com",
      phone: "0901234567",
      amount: 500000,
      type: "DEPOSIT",
      walletType: "NORMAL",
      status: "success",
      createdAt: "01/10/2024 08:30",
      note: "Nạp tiền qua chuyển khoản ngân hàng",
    }),
    [transactionId]
  );

  return (
    <div className="p-5 space-y-5">
      {/* HEADER */}
      <h2 className="text-2xl text-black font-bold flex items-center">
        <ArrowLeftOutlined
          className="mr-4 cursor-pointer"
          onClick={() => navigate({ to: transactionManagementRoute.to })}
        />
        Chi tiết giao dịch
      </h2>

      <div className="bg-white p-6 rounded-lg space-y-6">
        {/* ================= TRANSACTION INFO ================= */}
        <Section title="Thông tin giao dịch">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
            <Info label="Mã giao dịch" value={transaction.code} />
            <Info label="Thời gian" value={transaction.createdAt} />
            <Info
              label="Loại giao dịch"
              value={
                transaction.type === "DEPOSIT"
                  ? "Nạp tiền"
                  : transaction.type === "PAYMENT"
                  ? "Thanh toán"
                  : "Hoàn tiền"
              }
            />
            <Info
              label="Loại ví"
              value={
                transaction.walletType === "NORMAL"
                  ? "Ví thường"
                  : "Ví trả sau"
              }
            />
            <Info
              label="Số tiền"
              value={
                <span className="font-semibold text-green-600">
                  {formatMoney(transaction.amount)}
                </span>
              }
            />
            <Info
              label="Trạng thái"
              value={renderStatus(transaction.status)}
            />
          </div>
        </Section>

        {/* ================= USER INFO ================= */}
        <Section title="Thông tin người dùng">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
            <Info label="Họ tên" value={transaction.userName} />
            <Info label="Email" value={transaction.email} />
            <Info label="Số điện thoại" value={transaction.phone} />
          </div>
        </Section>

        {/* ================= NOTE ================= */}
        {transaction.note && (
          <Section title="Ghi chú">
            <p className="text-sm text-gray-700">{transaction.note}</p>
          </Section>
        )}

        {/* ================= ACTION ================= */}
        {transaction.status === "pending" && (
          <Section title="Thao tác">
            <div className="flex gap-3">
              <CustomButton
                label="Duyệt giao dịch"
                color={colors.green.g2}
                onClick={() => console.log("Approve")}
              />
              <CustomButton
                label="Từ chối"
                color={colors.red.r2}
                onClick={() => console.log("Reject")}
              />
            </div>
          </Section>
        )}
      </div>
    </div>
  );
};

export default TransactionDetailPage;

// ================= HELPER =================
const Info = ({
  label,
  value,
}: {
  label: string;
  value: React.ReactNode;
}) => (
  <div className="flex flex-col gap-1">
    <span className="text-gray-500">{label}</span>
    <span className="text-gray-900 font-medium">{value}</span>
  </div>
);
