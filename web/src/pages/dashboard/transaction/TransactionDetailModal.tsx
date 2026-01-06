import React from "react";
import { Modal, Form, Row, Col, Input, Tag, Button } from "antd";
import dayjs from "dayjs";
import type { TransactionResource } from "@/types/transaction.type";
import type { TransactionStatus } from "@/enum/status";

interface Props {
    open: boolean;
    transaction: TransactionResource | null;
    onClose: () => void;
}

const STATUS_COLOR_MAP: Record<TransactionStatus, string> = {
    COMPLETED: "green",
    PROCESSING: "blue",
    PENDING: "orange",
    FAILED: "red",
    CANCELED: "default",
};

const STATUS_LABEL_MAP: Record<TransactionStatus, string> = {
    COMPLETED: "Hoàn thành",
    PROCESSING: "Đang xử lý",
    PENDING: "Chờ xử lý",
    FAILED: "Thất bại",
    CANCELED: "Đã hủy",
};

const TransactionDetailModal: React.FC<Props> = ({ open, transaction, onClose }) => {
    if (!transaction) return null;

    return (
        <Modal
            open={open}
            title="Chi tiết giao dịch"
            onCancel={onClose}
            footer={[
                <Button key="close" onClick={onClose}>
                    Đóng
                </Button>,
            ]}
            width={800}
            destroyOnClose
        >
            <Form layout="vertical" disabled>
                <Row gutter={16}>
                    <Col span={12}>
                        <Form.Item label="Mã giao dịch">
                            <Input value={transaction.id || "-"} />
                        </Form.Item>
                    </Col>

                    <Col span={12}>
                        <Form.Item label="Trạng thái">
                            {transaction.status ? (
                                <Tag color={STATUS_COLOR_MAP[transaction.status]} style={{ fontSize: 15, padding: "8px 18px", borderRadius: 10, fontWeight: 600 }}>
                                    {STATUS_LABEL_MAP[transaction.status]}
                                </Tag>
                            ) : (
                                <Input value="-" />
                            )}
                        </Form.Item>
                    </Col>

                    <Col span={12}>
                        <Form.Item label="Ví nguồn">
                            <Input value={transaction.sourceAccountNumber || "-"} />
                        </Form.Item>
                    </Col>

                    <Col span={12}>
                        <Form.Item label="Ví nhận">
                            <Input value={transaction.toWalletNumber || "-"} />
                        </Form.Item>
                    </Col>

                    <Col span={12}>
                        <Form.Item label="Số tiền">
                            <Input value={transaction.amount ? `${transaction.amount.toLocaleString()} ₫` : "-"} />
                        </Form.Item>
                    </Col>

                    <Col span={12}>
                        <Form.Item label="Số dư ví nguồn (sau)">
                            <Input value={transaction.sourceBalanceUpdated ? `${transaction.sourceBalanceUpdated.toString()}` : "-"} />
                        </Form.Item>
                    </Col>

                    <Col span={12}>
                        <Form.Item label="Số dư ví đích (sau)">
                            <Input value={transaction.destinationBalanceUpdated ? `${transaction.destinationBalanceUpdated}` : "-"} />
                        </Form.Item>
                    </Col>

                    <Col span={12}>
                        <Form.Item label="Mô tả">
                            <Input value={transaction.description || "-"} />
                        </Form.Item>
                    </Col>

                    <Col span={12}>
                        <Form.Item label="Thời gian xử lý">
                            <Input value={transaction.processedAt ? dayjs(transaction.processedAt).format("DD/MM/YYYY HH:mm") : "-"} />
                        </Form.Item>
                    </Col>

                    <Col span={24}>
                        <Form.Item label="Meta dữ liệu">
                            <Input.TextArea rows={4} value={transaction.metaData ? (typeof transaction.metaData === 'string' ? transaction.metaData : JSON.stringify(transaction.metaData, null, 2)) : "-"} />
                        </Form.Item>
                    </Col>
                </Row>
            </Form>
        </Modal>
    );
};

export default TransactionDetailModal;
