// pages/dashboard/user/UserDetail.tsx
import React, { useEffect, useMemo, useState } from "react";
import {
  Form,
  Input,
  App,
  DatePicker,
  Select,
} from "antd";
import { ArrowLeftOutlined } from "@ant-design/icons";
import { useMatchRoute, useNavigate } from "@tanstack/react-router";
import { toast } from "react-hot-toast";
import { formatDateTime } from "@/utils/date";
import CustomButton from "@/components/Button";
import TableComponent from "@/components/Table";
import Section from "@/components/Section";
import { colors } from "@/theme/color";
import { editUserDetailRoute, userListRoute } from "@/routes/dashboard";
import NumberInput from "@/components/NumberInput";
import dayjs from "dayjs";
type WalletType = "NORMAL" | "CREDIT";
type TransactionType = "DEPOSIT" | "PAYMENT" | "ADJUSTMENT";

type Tx = {
  id: number;
  amount: number; // luôn là số dương
  type: TransactionType;
  walletType: WalletType;
  createdAt: string; // ISO
  note?: string;
};

type UserResource = {
  fullName: string;
  email: string;
  phone: string;
  birthDate?: string;
  address?: string;
  status: "active" | "locked" | "pending";
  role: "customer" | "admin";
};

const formatMoney = (v: number) => `${v.toLocaleString()} ₫`;

const UserDetailPage: React.FC = () => {
  const navigate = useNavigate();
  const matchRoute = useMatchRoute();

  const isEditMode = !!matchRoute({ to: editUserDetailRoute.to });
  const isAddMode = !isEditMode;

  const modal = App.useApp().modal;
  const [form] = Form.useForm();

  // ======================
  // MOCK USER (EDIT)
  // ======================
  const user: UserResource | null = useMemo(
    () =>
      isEditMode
        ? {
          fullName: "Nguyễn Văn A",
          email: "user1@gmail.com",
          phone: "0901234567",
          birthDate: "1995-05-15T00:00:00.000Z",
          address: "Hà Nội",
          status: "pending",
          role: "customer",
        }
        : null,
    [isEditMode]
  );

  const canUseCreditWallet = user?.status === "active";
  const isPendingCreditRequest = user?.status === "pending";

  // ======================
  // WALLET STATE
  // ======================
  const [wallet, setWallet] = useState<{ normal: number; credit: number }>({
    normal: 1500000,
    credit: 300000, // ví trả sau
  });

  // ======================
  // TRANSACTION STATE
  // ======================
  const [transactions, setTransactions] = useState<Tx[]>([
    {
      id: 1,
      amount: 500000,
      type: "DEPOSIT",
      walletType: "NORMAL",
      createdAt: "2024-10-01T08:00:00.000Z",
    },
    {
      id: 2,
      amount: 200000,
      type: "PAYMENT",
      walletType: "NORMAL",
      createdAt: "2024-10-10T09:00:00.000Z",
    },
  ]);

  // ======================
  // PREFILL FORM (EDIT)
  // ======================
  useEffect(() => {
    if (isEditMode && user) {
      form.setFieldsValue({
        email: user.email,
        phone: user.phone,
        fullName: user.fullName,
        birthDate: user.birthDate ? dayjs(user.birthDate) : undefined,
        address: user.address,
      });
    } else {
      form.resetFields();
    }
  }, [isEditMode, user, form]);

  // ======================
  // DEPOSIT MODAL STATE
  // ======================
  const [depositType, setDepositType] = useState<WalletType>("NORMAL");
  const [depositAmount, setDepositAmount] = useState<number>(0);

  const openDepositModal = (type: WalletType) => {
    setDepositType(type);
    setDepositAmount(0);

    modal.confirm({
      title: "Nạp tiền vào ví thường",
      content: (
        <div className="space-y-2">
          <div className="text-sm text-gray-500">
            Nhập số tiền cần nạp ({">"}= 0)
          </div>
          <NumberInput
            style={{ width: "100%" }}
            min={0}
            value={depositAmount}
            onChange={(v: any) => setDepositAmount(v ?? 0)}
          />
        </div>
      ),
      okText: "Xác nhận",
      cancelText: "Hủy",
      onOk: () => handleConfirmDeposit(),
    });
  };

  const handleConfirmDeposit = () => {
    if (depositAmount <= 0) {
      toast.error("Số tiền nạp phải lớn hơn 0");
      return;
    }

    // 1) Update wallet
    setWallet((prev) => {
      if (depositType === "NORMAL") {
        return { ...prev, normal: prev.normal + depositAmount };
      }
      return { ...prev, credit: prev.credit + depositAmount };
    });

    // 2) Push transaction (DEPOSIT)
    setTransactions((prev) => [
      {
        id: Date.now(),
        amount: depositAmount,
        type: "DEPOSIT",
        walletType: depositType,
        createdAt: new Date().toISOString(),
      },
      ...prev,
    ]);

    toast.success("Nạp tiền thành công");
  };

  // ======================
  // FILTER STATE
  // ======================
  const [filterRange, setFilterRange] = useState<any>(null); // dayjs range (AntD)
  const [filterType, setFilterType] = useState<TransactionType | undefined>();
  const [filterWallet, setFilterWallet] = useState<WalletType | undefined>();

  const filteredTransactions = useMemo(() => {
    return transactions.filter((tx) => {
      // filter type
      if (filterType && tx.type !== filterType) return false;

      // filter wallet
      if (filterWallet && tx.walletType !== filterWallet) return false;

      // filter time range
      if (filterRange?.length === 2 && filterRange[0] && filterRange[1]) {
        const from = filterRange[0].startOf("day").valueOf();
        const to = filterRange[1].endOf("day").valueOf();
        const t = new Date(tx.createdAt).getTime();
        if (t < from || t > to) return false;
      }

      return true;
    });
  }, [transactions, filterRange, filterType, filterWallet]);

  // ======================
  // ACTIONS
  // ======================
  const handleSave = async () => {
    await form.validateFields();
    toast.success(isAddMode ? "Tạo tài khoản thành công" : "Cập nhật thành công");
    navigate({ to: userListRoute.to });
  };

  const handleLock = () => toast.success("Đã khóa tài khoản");
  const handleUnlock = () => toast.success("Đã mở khóa tài khoản");

  // ======================
  // TABLE COLUMNS
  // ======================
  const columns = [
    { key: "createdAt", label: "Thời gian", render: (i: Tx) => formatDateTime(i.createdAt) },
    {
      key: "type",
      label: "Loại giao dịch",
      render: (i: Tx) =>
        i.type === "DEPOSIT" ? "Nạp tiền" : i.type === "PAYMENT" ? "Thanh toán" : "Điều chỉnh",
    },
    {
      key: "walletType",
      label: "Loại ví",
      render: (i: Tx) => (i.walletType === "NORMAL" ? "Ví thường" : "Ví trả sau"),
    },
    { key: "amount", label: "Số tiền", render: (i: Tx) => formatMoney(i.amount) },
  ];

  // ======================
  // RENDER
  // ======================
  return (
    <div className="p-5 space-y-5">
      {/* HEADER */}
      <h2 className="text-2xl text-black font-bold flex items-center">
        <ArrowLeftOutlined
          className="mr-4 cursor-pointer"
          onClick={() => navigate({ to: userListRoute.to })}
        />
        {isAddMode ? "Tạo tài khoản mới" : "Chỉnh sửa người dùng"}
      </h2>

      <div className="bg-white p-6 rounded-lg space-y-2">
        {/* ================= INFO ================= */}
        <Section title="Thông tin cá nhân" description="Cập nhật thông tin cơ bản của người dùng">
          <Form layout="vertical" form={form}>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <Form.Item label="Email" name="email" rules={[{ required: true }]}>
                <Input />
              </Form.Item>

              <Form.Item label="Số điện thoại" name="phone" rules={[{ required: true }]}>
                <Input />
              </Form.Item>

              <Form.Item label="Họ tên" name="fullName" rules={[{ required: true }]}>
                <Input />
              </Form.Item>

              <Form.Item
                label="Ngày sinh"
                name="birthDate"
                rules={[{ required: true, message: "Vui lòng chọn ngày sinh" }]}
              >
                <DatePicker
                  style={{ width: "100%" }}
                  format="DD/MM/YYYY"
                  placeholder="Chọn ngày sinh"
                />
              </Form.Item>

              <Form.Item label="Địa chỉ" name="address">
                <Input />
              </Form.Item>

              <Form.Item
                label="Tiền nạp ban đầu"
                name="initialBalance"
                extra={isEditMode ? "Chỉ áp dụng khi tạo mới" : undefined}
              >
                <NumberInput style={{ width: "100%" }} />
              </Form.Item>
            </div>
          </Form>
        </Section>

        {/* ================= WALLET ================= */}
        {isEditMode && (
          <Section title="Ví & số dư" description="Quản lý số dư và ví của khách hàng">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">

              {/* ===== Ví thường (LUÔN CÓ) ===== */}
              <div className="p-4 rounded-lg bg-gray-50 flex justify-between items-center">
                <div>
                  <p className="text-sm text-gray-500">Ví thường</p>
                  <p className="text-xl font-bold text-green-600">
                    {formatMoney(wallet.normal)}
                  </p>
                </div>

                <CustomButton
                  label="Nạp tiền"
                  color={colors.blue.b3}
                  onClick={() => openDepositModal("NORMAL")}
                />
              </div>

              {/* ===== Ví trả sau (CHỈ KHI ACTIVE) ===== */}
              {canUseCreditWallet && (
                <div className="p-4 rounded-lg bg-gray-50 flex justify-between items-center">
                  <div>
                    <p className="text-sm text-gray-500">Ví trả sau</p>
                    <p className="text-xl font-bold text-green-600">
                      {formatMoney(wallet.credit)}
                    </p>
                  </div>
                </div>
              )}

              {/* ===== Yêu cầu mở ví trả sau ===== */}
              {isPendingCreditRequest && (
                <div className="p-4 rounded-lg bg-yellow-50 border border-yellow-200 flex justify-between items-center">
                  <div>
                    <p className="text-sm text-yellow-700 font-medium">
                      Yêu cầu mở ví trả sau
                    </p>
                    <p className="text-xs text-yellow-600">
                      Tài khoản đang chờ duyệt để kích hoạt ví trả sau
                    </p>
                  </div>

                  <CustomButton
                    label="Duyệt yêu cầu"
                    color={colors.green.g2}
                    onClick={() => {
                      toast.success("Đã duyệt mở ví trả sau");
                      // TODO: gọi API duyệt → update status = active
                    }}
                  />
                </div>
              )}

            </div>
          </Section>
        )}


        {/* ================= ACCOUNT STATUS ================= */}
        {isEditMode && (
          <Section title="Trạng thái tài khoản" description="Khóa/mở tài khoản khi cần thiết">
            <div className="flex gap-3">
              {user?.status === "active" ? (
                <CustomButton
                  label="Khóa tài khoản"
                  color={colors.red.r2}
                  onClick={handleLock}
                />
              ) : (
                <CustomButton
                  label="Mở tài khoản"
                  color={colors.blue.b2}
                  onClick={handleUnlock}
                />
              )}
            </div>
          </Section>
        )}

        {/* ================= TRANSACTION ================= */}
        {isEditMode && (
          <Section title="Lịch sử giao dịch" description="Lọc theo thời gian, loại giao dịch và loại ví">
            <div className="flex flex-wrap gap-3 mb-4">
              <DatePicker.RangePicker onChange={(v) => setFilterRange(v)} />

              <Select
                allowClear
                placeholder="Loại giao dịch"
                style={{ minWidth: 180 }}
                onChange={(v) => setFilterType(v)}
                options={[
                  { label: "Nạp tiền", value: "DEPOSIT" },
                  { label: "Thanh toán", value: "PAYMENT" },
                  { label: "Điều chỉnh", value: "ADJUSTMENT" },
                ]}
              />

              <Select
                allowClear
                placeholder="Loại ví"
                style={{ minWidth: 180 }}
                onChange={(v) => setFilterWallet(v)}
                options={[
                  { label: "Ví thường", value: "NORMAL" },
                  { label: "Ví trả sau", value: "CREDIT" },
                ]}
              />

              <CustomButton
                label="Xóa lọc"
                color={colors.blue.b4}
                onClick={() => {
                  setFilterRange(null);
                  setFilterType(undefined);
                  setFilterWallet(undefined);
                }}
              />
            </div>

            <TableComponent columns={columns} dataSource={filteredTransactions} />
          </Section>
        )}

        {/* ================= SAVE ================= */}
        <div className="mt-2 flex justify-end">
          <CustomButton
            label={isAddMode ? "Tạo tài khoản" : "Lưu thay đổi"}
            color={colors.blue.b2}
            onClick={handleSave}
          />
        </div>
      </div>
    </div>
  );
};

export default UserDetailPage;
