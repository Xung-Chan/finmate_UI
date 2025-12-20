import React, { useState } from "react";
import { Form, Input, Modal, Switch } from "antd";
import { PlusOutlined, EditOutlined } from "@ant-design/icons";
import { toast } from "react-hot-toast";

import Section from "@/components/Section";
import TableComponent from "@/components/Table";
import CustomButton from "@/components/Button";
import NumberInput from "@/components/NumberInput";
import { colors } from "@/theme/color";

type RoleType = "USER" | "STAFF" | "ADMIN";

type LimitConfig = {
  id: number;
  name?: string;
  role: RoleType;
  creditLimit: number;
  interestRate: number;
  status: "ACTIVE" | "INACTIVE";
  updatedAt: string;
};

const formatMoney = (v: number) => `${v.toLocaleString()} ₫`;

const CreditPolicyPage: React.FC = () => {
  const [form] = Form.useForm();

  const [editingItem, setEditingItem] = useState<LimitConfig | null>(null);
  const [open, setOpen] = useState(false);

  const [data, setData] = useState<LimitConfig[]>([
    {
      id: 1,
      name: "Gói tiêu chuẩn",
      role: "USER",
      creditLimit: 5000000,
      interestRate: 2.5,
      status: "ACTIVE",
      updatedAt: "01/10/2024",
    },
    {
      id: 2,
      name: "Gói nhân viên",
      role: "STAFF",
      creditLimit: 10000000,
      interestRate: 1.5,
      status: "ACTIVE",
      updatedAt: "05/10/2024",
    },
  ]);

  // ================= MODAL =================
  const openModal = (item?: LimitConfig) => {
    setEditingItem(item ?? null);
    setOpen(true);

    if (item) {
      form.setFieldsValue(item);
    } else {
      form.resetFields();
    }
  };

  const handleSubmit = async () => {
    const values = await form.validateFields();

    if (editingItem) {
      setData((prev) =>
        prev.map((i) =>
          i.id === editingItem.id
            ? {
                ...i,
                ...values,
                updatedAt: new Date().toLocaleDateString(),
              }
            : i
        )
      );
      toast.success("Cập nhật định mức thành công");
    } else {
      setData((prev) => [
        {
          id: Date.now(),
          status: "ACTIVE",
          updatedAt: new Date().toLocaleDateString(),
          ...values,
        },
        ...prev,
      ]);
      toast.success("Thêm định mức mới thành công");
    }

    setOpen(false);
  };

  // ================= TABLE =================
  const columns = [
    { key: "name", label: "Tên gói" },
    {
      key: "role",
      label: "Áp dụng cho",
      render: (i: LimitConfig) =>
        i.role === "USER"
          ? "Người dùng"
          : i.role === "STAFF"
          ? "Nhân viên"
          : "Admin",
    },
    {
      key: "creditLimit",
      label: "Hạn mức",
      render: (i: LimitConfig) => formatMoney(i.creditLimit),
    },
    {
      key: "interestRate",
      label: "Lãi suất",
      render: (i: LimitConfig) => `${i.interestRate}% / tháng`,
    },
    {
      key: "status",
      label: "Trạng thái",
      render: (i: LimitConfig) => (
        <Switch
          checked={i.status === "ACTIVE"}
          onChange={(checked) =>
            setData((prev) =>
              prev.map((x) =>
                x.id === i.id
                  ? { ...x, status: checked ? "ACTIVE" : "INACTIVE" }
                  : x
              )
            )
          }
        />
      ),
    },
    { key: "updatedAt", label: "Cập nhật lần cuối" },
  ];

  const renderActions = (item: LimitConfig) => (
    <EditOutlined
      className="cursor-pointer"
      style={{ color: colors.blue.b3 }}
      onClick={() => openModal(item)}
    />
  );

  return (
    <div className="p-5 space-y-6">
      <h2 className="text-2xl font-bold text-black">
        Quản lý hạn mức & lãi suất
      </h2>

      <Section
        title="Danh sách định mức"
        description="Thiết lập hạn mức ví trả sau và lãi suất áp dụng"
      >
        <div className="flex justify-end mb-3">
          <CustomButton
            label="Thêm định mức"
            icon={<PlusOutlined />}
            color={colors.blue.b2}
            onClick={() => openModal()}
          />
        </div>

        <TableComponent
          columns={columns}
          dataSource={data}
          renderActions={renderActions}
        />
      </Section>

      {/* ================= MODAL ================= */}
      <Modal
        open={open}
        title={editingItem ? "Chỉnh sửa định mức" : "Thêm định mức"}
        onCancel={() => setOpen(false)}
        onOk={handleSubmit}
        okText="Lưu"
        cancelText="Hủy"
      >
        <Form layout="vertical" form={form}>
          <Form.Item
            label="Tên gói"
            name="name"
            rules={[{ required: true }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            label="Áp dụng cho"
            name="role"
            rules={[{ required: true }]}
          >
            <Input placeholder="USER / STAFF / ADMIN" />
          </Form.Item>

          <Form.Item
            label="Hạn mức (VNĐ)"
            name="creditLimit"
            rules={[{ required: true }]}
          >
            <NumberInput />
          </Form.Item>

          <Form.Item
            label="Lãi suất (% / tháng)"
            name="interestRate"
            rules={[{ required: true }]}
          >
            <NumberInput />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default CreditPolicyPage;
