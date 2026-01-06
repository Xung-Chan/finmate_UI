import { Modal, Form, InputNumber } from "antd";
import CustomButton from "@/components/Button";
import { useCreateCashTransaction } from "@/hooks/transaction.hook";
import { ActionType } from "@/enum/status";
import { colors } from "@/theme/color";

interface Props {
  open: boolean;
  onClose: () => void;
  actionType: typeof ActionType[keyof typeof ActionType];
  sourceWalletNumber: string;
}

const CashTransactionModal: React.FC<Props> = ({
  open,
  onClose,
  actionType,
  sourceWalletNumber,
}) => {
  const [form] = Form.useForm();
  const { mutate, isPending } = useCreateCashTransaction();

  const handleSubmit = async () => {
    const values = await form.validateFields();

    mutate(
      {
        actionType,
        amount: values.amount,
        sourceWalletNumber,
      },
      {
        onSuccess: () => {
          form.resetFields();
          onClose();
        },
      }
    );
  };

  return (
    <Modal
      open={open}
      onCancel={onClose}
      footer={null}
      title={actionType === ActionType.CASH_DEPOSIT ? "Nạp tiền" : "Rút tiền"}
    >
      <Form layout="vertical" form={form}>
        <Form.Item
          label="Số tiền"
          name="amount"
          rules={[
            { required: true, message: "Vui lòng nhập số tiền" },
            { type: "number", min: 1, message: "Số tiền không hợp lệ" },
          ]}
        >
          <InputNumber
            className="w-full"
            min={1}
            step={1000}
            formatter={(v) =>
              `${v}`.replace(/\B(?=(\d{3})+(?!\d))/g, ",")
            }
            style={{ width: "100%" }}

          />
        </Form.Item>

        <div className="flex justify-end gap-2">
          <CustomButton label="Hủy" onClick={onClose} />
          <CustomButton
            label="Xác nhận"
            color={colors.blue.b2}
            loading={isPending}
            onClick={handleSubmit}
          />
        </div>
      </Form>
    </Modal>
  );
};

export default CashTransactionModal;
