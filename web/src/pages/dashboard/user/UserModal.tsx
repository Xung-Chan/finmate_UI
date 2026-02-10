import { Modal, Form, Input, Row, Col, Button, Tag } from "antd";
import type { UserResource } from "@/types/user.type";
import { UserStatus, USER_STATUS_LABEL } from "@/enum/status";

interface Props {
    open: boolean;
    user: UserResource | null;
    loading?: boolean;
    onClose: () => void;
    onToggleStatus: (user: UserResource) => void;
}

const UserDetailModal: React.FC<Props> = ({
    open,
    user,
    loading = false,
    onClose,
    onToggleStatus,
}) => {
    if (!user) return null;

    const isActive = user.status === UserStatus.ACTIVE;
    const isLocked = user.status === UserStatus.LOCKED;


    const statusColorMap: Record<string, string> = {
        [UserStatus.ACTIVE]: 'green',
        [UserStatus.LOCKED]: 'red',
    };
    const statusLabel = USER_STATUS_LABEL[user.status];

    return (
        <Modal
            open={open}
            title="Chi tiết người dùng"
            onCancel={onClose}
            footer={[
                <Button key="cancel" onClick={onClose} disabled={loading}>
                    Đóng
                </Button>,
                // Chỉ hiện nút khi tài khoản ACTIVE hoặc LOCKED
                (isActive || isLocked) && (
                    <Button
                        key="action"
                        danger={isActive}
                        type="primary"
                        loading={loading}
                        onClick={() => onToggleStatus(user)}
                    >
                        {isActive ? "Khóa tài khoản" : "Mở khóa tài khoản"}
                    </Button>
                ),
            ].filter(Boolean)}
            width={700}
        >
            <Form layout="vertical" disabled>
                <Row gutter={16}>
                    <Col span={12}>
                        <Form.Item label="Họ và tên">
                            <Input value={user.merchantName} />
                        </Form.Item>
                    </Col>

                    <Col span={12}>
                        <Form.Item label="Username">
                            <Input value={user.username} />
                        </Form.Item>
                    </Col>

                    <Col span={12}>
                        <Form.Item label="Email">
                            <Input value={user.mail} />
                        </Form.Item>
                    </Col>

                    <Col span={12}>
                        <Form.Item label="Số điện thoại">
                            <Input value={user.phoneNumber} />
                        </Form.Item>
                    </Col>

                    <Col span={12}>
                        <Form.Item label="Giới tính">
                            <Input value={user.gender} />
                        </Form.Item>
                    </Col>

                    <Col span={12}>
                        <Form.Item label="CMND/CCCD">
                            <Input value={user.cardId} />
                        </Form.Item>
                    </Col>

                    <Col span={24}>
                        <Form.Item label="Địa chỉ">
                            <Input.TextArea rows={2} value={user.address} />
                        </Form.Item>
                    </Col>

                    <Col span={12}>
                        <Form.Item label="Trạng thái">
                            <Tag color={statusColorMap[user.status] || 'default'}>
                                {statusLabel}
                            </Tag>
                        </Form.Item>
                    </Col>
                </Row>
            </Form>
        </Modal >
    );
};

export default UserDetailModal;
