import { Form, Input } from "antd";
import { ArrowLeftOutlined } from "@ant-design/icons";
import { useNavigate } from "@tanstack/react-router";
import Section from "@/components/Section";
import CustomButton from "@/components/Button";
import { useCreateNotification } from "@/hooks/notification.hook";

const CreateNotificationPage: React.FC = () => {
  const navigate = useNavigate();
  const { mutate, isPending } = useCreateNotification();
  const [form] = Form.useForm();

  return (
    <div className="p-5 space-y-5">
      {/* HEADER */}
      <h2 className="text-2xl font-bold flex items-center">
        <ArrowLeftOutlined
          className="mr-4 cursor-pointer"
          onClick={() => navigate({ to: "/manage/notifications" })}
        />
        Thêm thông báo
      </h2>

      <div className="bg-white p-6 rounded-lg">
        <Section
          title="Thông tin thông báo"
          description="Tạo thông báo mới gửi tới người dùng"
        >
          <Form
            layout="vertical"
            form={form}
            onFinish={(values) => mutate(values)}
          >
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <Form.Item
                label="Tiêu đề"
                name="title"
                rules={[{ required: true, message: "Vui lòng nhập tiêu đề" }]}
              >
                <Input placeholder="Nhập tiêu đề thông báo" />
              </Form.Item>

              <Form.Item
                label="Nội dung"
                name="description"
                rules={[{ required: true, message: "Vui lòng nhập nội dung" }]}
                className="md:col-span-2"
              >
                <Input.TextArea
                  rows={5}
                  placeholder="Nhập nội dung thông báo"
                />
              </Form.Item>

              <div className="col-span-full flex justify-end">
                <CustomButton
                  label="Tạo thông báo"
                  loading={isPending}  
                  htmlType="submit"                
                />
              </div>
            </div>
          </Form>
        </Section>
      </div>
    </div>
  );
};

export default CreateNotificationPage;
