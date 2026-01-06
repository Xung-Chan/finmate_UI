import { Modal, Form, Input, Row, Col, Tag, Button } from "antd";
import type { PayLaterApplicationResource } from "@/types/walltet.type";
import type { PayLaterApplicationType, PayLaterApplicationStatus } from "@/enum/status";
import { useProcessPaylaterApplication } from "@/hooks/wallet.hook";
import dayjs from "dayjs";

interface Props {
  open: boolean;
  application: PayLaterApplicationResource | null;
  loading?: boolean;
  onClose: () => void;
}

const STATUS_COLOR_MAP: Record<PayLaterApplicationStatus, string> = {
  PENDING: "orange",
  APPROVED: "green",
  REJECTED: "red",
  CANCELED: "gray",
};

const STATUS_LABEL_MAP: Record<PayLaterApplicationStatus, string> = {
  PENDING: "Chờ duyệt",
  APPROVED: "Đã duyệt",
  REJECTED: "Từ chối",
  CANCELED: "Hủy",
};


const PAYLATER_APPLICATION_TYPE_LABEL: Record<
  PayLaterApplicationType,
  string
> = {
  ACTIVATION: "Kích hoạt thẻ PayLater",
  LIMIT_ADJUSTMENT: "Điều chỉnh hạn mức tín dụng",
  SUSPEND_REQUEST: "Yêu cầu tạm ngưng thẻ PayLater",
};
const PAYLATER_APPLICATION_TYPE_COLOR: Record<
  PayLaterApplicationType,
  string
> = {
  ACTIVATION: "blue",
  LIMIT_ADJUSTMENT: "gold",
  SUSPEND_REQUEST: "volcano",
};

const PayLaterApplicationDetailModal: React.FC<Props> = ({
  open,
  application,
  loading = false,
  onClose,
}) => {
  if (!application) return null;

  const { mutate: processPaylaterApplication, isPending: isProcessing } = useProcessPaylaterApplication();
  return (
    <Modal
      open={open}
      title="Chi tiết đơn đăng ký PayLater"
      onCancel={onClose}
      footer={[
        application.status === "PENDING" && (
          <Button
            key="approve"
            type="primary"
            loading={isProcessing}
            onClick={() =>
              processPaylaterApplication({ id: application.id, action: "APPROVE" })
            }
          >
            Duyệt
          </Button>
        ),
        application.status === "PENDING" && (
          <Button
            key="reject"
            danger
            onClick={() =>
              processPaylaterApplication({ id: application.id, action: "REJECT" })
            }
            disabled={isProcessing}
          >
            Từ chối
          </Button>
        ),
        <Button key="close" onClick={onClose} disabled={loading || isProcessing}>
          Đóng
        </Button>,
      ]}
      width={720}
    >
      <Form layout="vertical" disabled>
        <Row gutter={16}>
          <Col span={12}>
            <Form.Item label="Người dùng">
              <Input value={application.username || "-"} />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Trạng thái">
              <Tag
                color={STATUS_COLOR_MAP[application.status]}
                style={{
                  fontSize: 15,
                  padding: "8px 18px",
                  borderRadius: 10,
                  fontWeight: 600,
                  display: "inline-flex",
                  alignItems: "center",
                }}
              >
                {STATUS_LABEL_MAP[application.status]}
              </Tag>

            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Hạn mức yêu cầu">
              <Input
                value={
                  application.requestedCreditLimit
                    ? application.requestedCreditLimit.toLocaleString("vi-VN") +
                    " ₫"
                    : "-"
                }
              />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Hạn mức duyệt">
              <Input
                value={
                  application.approvedLimit
                    ? application.approvedLimit.toLocaleString("vi-VN") + " ₫"
                    : "-"
                }
              />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Ngày đăng ký">
              <Input value={application.appliedAt ? dayjs(application.appliedAt).format("DD/MM/YYYY") : "-"} />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Người duyệt">
              <Input value={application.approvedBy || "-"} />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Loại yêu cầu PayLater">
              {application.type ? (
                <Tag
                  color={PAYLATER_APPLICATION_TYPE_COLOR[application.type]}
                  style={{
                    fontSize: 15,
                    padding: "8px 18px",
                    borderRadius: 10,
                    fontWeight: 600,
                  }}
                >
                  {PAYLATER_APPLICATION_TYPE_LABEL[application.type]}
                </Tag>


              ) : (
                "-"
              )}
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Ngày xử lý">
              <Input
                value={
                  application.processedAt
                    ? dayjs(application.processedAt).format("DD/MM/YYYY HH:mm")
                    : "-"
                }
              />
            </Form.Item>
          </Col>

          <Col span={24}>
            <Form.Item label="Lý do đăng ký">
              <Input.TextArea
                rows={3}
                value={application.reason || "-"}
              />
            </Form.Item>
          </Col>

          {application.rejectionReason && (
            <Col span={24}>
              <Form.Item label="Lý do từ chối">
                <Input.TextArea
                  rows={3}
                  value={application.rejectionReason}
                />
              </Form.Item>
            </Col>
          )}
        </Row>
      </Form>
    </Modal>
  );
};

export default PayLaterApplicationDetailModal;
