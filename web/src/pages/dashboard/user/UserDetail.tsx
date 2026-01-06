// pages/dashboard/user/UserDetail.tsx
import {
  Form,
  Input,
  DatePicker,
  Select,
} from "antd";
import { ArrowLeftOutlined } from "@ant-design/icons";
import { useNavigate } from "@tanstack/react-router";
import CustomButton from "@/components/Button";
import NumberInput from "@/components/NumberInput";
import Section from "@/components/Section";
import { colors } from "@/theme/color";
import dayjs from "dayjs";
import { userListRoute } from "@/routes/dashboard";
import { useCreateSingleUser } from "@/hooks/user.hook";

const UserDetailPage: React.FC = () => {
  const navigate = useNavigate();
  const { mutate: createUser, isPending: isPending } = useCreateSingleUser();


  const [form] = Form.useForm();


  // ======================
  // ACTIONS
  // ======================
  const handleSave = async () => {
    const values = await form.validateFields();

    const payload = {
      ...values,
      dateOfBirth: values.dateOfBirth
        ? dayjs(values.dateOfBirth).format("YYYY-MM-DD")
        : undefined,
      phoneNumber: values.phoneNumber ? String(values.phoneNumber) : undefined,
      cardId: values.cardId ? String(values.cardId) : undefined,
    };

    createUser(payload);
  };


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
        Tạo tài khoản mới
      </h2>

      <div className="bg-white p-6 rounded-lg space-y-2">
        {/* ================= INFO ================= */}
        <Section title="Thông tin cá nhân" description="Cập nhật thông tin cơ bản của người dùng">
          <Form layout="vertical" form={form}>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <Form.Item label="Họ tên" name="fullName" rules={[{ required: true }]}>
                <Input />
              </Form.Item>
              <Form.Item label="Số điện thoại" name="phoneNumber" rules={[{ required: true }]}>
                <NumberInput />
              </Form.Item>

              <Form.Item label="Email" name="mail" rules={[{ required: true }]}>
                <Input />
              </Form.Item>
              <Form.Item label="CMND/CCCD" name="cardId" rules={[{ required: true }]}>
                <NumberInput />
              </Form.Item>
              <Form.Item
                label="Giới tính"
                name="gender"
                rules={[{ required: true }]}
              >
                <Select
                  options={[
                    { value: "MALE", label: "Nam" },
                    { value: "FEMALE", label: "Nữ" },
                  ]}
                />
              </Form.Item>

              <Form.Item
                label="Vai trò"
                name="role"
              >
                <Select
                  options={[
                    { value: "USER", label: "Người dùng" },
                    { value: "STAFF", label: "Nhân viên" },
                  ]}
                />
              </Form.Item>
              <Form.Item
                label="Ngày sinh"
                name="dateOfBirth"
                rules={[{ required: true, message: "Vui lòng chọn ngày sinh" }]}
              >
                <DatePicker
                  style={{ width: "100%" }}
                  format="DD/MM/YYYY"
                />
              </Form.Item>

              <Form.Item label="Địa chỉ" name="address">
                <Input />
              </Form.Item>


            </div>
          </Form>
        </Section>

        {/* ================= SAVE ================= */}
        <div className="mt-2 flex justify-end">
          <CustomButton
            label="Tạo tài khoản"
            color={colors.blue.b2}
            onClick={handleSave}
            loading={isPending}
          />

        </div>
      </div>
    </div>
  );
};

export default UserDetailPage;
