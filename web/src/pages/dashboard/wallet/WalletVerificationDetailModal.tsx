import { Modal, Form, Input, Row, Col, Tag, Button, Image } from "antd";
import type { WalletVerificationResource } from "@/types/walltet.type";
import type { VerificationStatus } from "@/enum/status";
import { useProcessWalletVerification } from "@/hooks/wallet.hook";
import dayjs from "dayjs";

interface Props {
  open: boolean;
  verified: WalletVerificationResource | null;
  loading?: boolean;
  onClose: () => void;
}

/* =======================
   STATUS CONFIG
   ======================= */
const STATUS_COLOR_MAP: Record<VerificationStatus, string> = {
  PENDING: "orange",
  APPROVED: "green",
  REJECTED: "red",
  CANCELED: "default",
};

const STATUS_LABEL_MAP: Record<VerificationStatus, string> = {
  PENDING: "Chờ duyệt",
  APPROVED: "Đã duyệt",
  REJECTED: "Từ chối",
  CANCELED: "Đã hủy",
};

const WalletVerificationDetailModal: React.FC<Props> = ({
  open,
  verified,
  loading = false,
  onClose,
}) => {
  if (!verified) return null;

  // normalized array of document URLs
  const docs: string[] = (() => {
    const d = verified.verifiedDocuments as string | string[] | undefined;
    if (!d) return [];
    try {
      if (typeof d === "string") {
        const parsed = JSON.parse(d);
        return Array.isArray(parsed) ? parsed : [String(parsed)];
      }
      if (Array.isArray(d)) return d;
      return [];
    } catch (error) {
      return [String(d)];
    }
  })();

  // eslint-disable-next-line react-hooks/rules-of-hooks
  const { mutate: processWalletVerification, isPending: isProcessing } = useProcessWalletVerification();

  return (
    <Modal
      open={open}
      title="Chi tiết đơn xác thực ví"
      onCancel={onClose}
      footer={[
        verified.status === "PENDING" && (
          <Button
            key="approve"
            type="primary"
            loading={isProcessing}
            onClick={() =>
              processWalletVerification({ requestId: verified.id, status: "APPROVED" })
            }
          >
            Duyệt
          </Button>
        ),
        verified.status === "PENDING" && (
          <Button
            key="reject"
            danger
            onClick={() =>
              processWalletVerification({ requestId: verified.id, status: "REJECTED" })
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
      width={800}
      destroyOnClose
    >
      <Form layout="vertical" disabled>
        <Row gutter={16}>
          <Col span={12}>
            <Form.Item label="Số ví">
              <Input value={verified.walletNumber || "-"} />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Trạng thái">
              <Tag
                color={STATUS_COLOR_MAP[verified.status]}
                style={{
                  fontSize: 15,
                  padding: "8px 18px",
                  borderRadius: 10,
                  fontWeight: 600,
                  display: "inline-flex",
                  alignItems: "center",
                }}
              >
                {STATUS_LABEL_MAP[verified.status]}
              </Tag>
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Tên hiển thị hóa đơn">
              <Input value={verified.invoiceDisplayName || "-"} />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Tên doanh nghiệp">
              <Input value={verified.businessName || "-"} />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Mã số thuế">
              <Input value={verified.businessCode || "-"} />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Địa chỉ kinh doanh">
              <Input value={verified.businessAddress || "-"} />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Người đại diện">
              <Input value={verified.representativeName || "-"} />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Loại giấy tờ">
              <Input value={verified.representativeIdType || "-"} />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Số giấy tờ">
              <Input value={verified.representativeIdNumber || "-"} />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Email liên hệ">
              <Input value={verified.contactEmail || "-"} />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Số điện thoại liên hệ">
              <Input value={verified.contactPhone || "-"} />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Ngày tạo đơn">
              <Input
                value={
                  verified.createdAt
                    ? dayjs(verified.createdAt).format("DD/MM/YYYY HH:mm")
                    : "-"
                }
              />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Ngày xử lý">
              <Input
                value={
                  verified.processedAt
                    ? dayjs(verified.processedAt).format("DD/MM/YYYY HH:mm")
                    : "-"
                }
              />
            </Form.Item>
          </Col>

          <Col span={12}>
            <Form.Item label="Người xử lý">
              <Input value={verified.processedBy || "-"} />
            </Form.Item>
          </Col>

          <Col span={24}>
            <Form.Item label="Tài liệu xác thực">
              {docs.length > 0 ? (
                <div style={{ display: "flex", gap: 12, flexWrap: "wrap" }}>
                  {docs.map((url, idx) => (
                    <Image
                      key={idx}
                      src={url}
                      width={140}
                      height={100}
                      style={{ objectFit: "cover", borderRadius: 6 }}
                    />
                  ))}
                </div>
              ) : (
                <Input value={"-"} />
              )}
            </Form.Item>
          </Col>


        </Row>
      </Form>
    </Modal>
  );
};

export default WalletVerificationDetailModal;