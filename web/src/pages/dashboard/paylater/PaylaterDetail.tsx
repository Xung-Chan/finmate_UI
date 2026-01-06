import { Form, Input, Select, Spin } from "antd";
import { ArrowLeftOutlined } from "@ant-design/icons";
import { useNavigate, useParams } from "@tanstack/react-router";
import Section from "@/components/Section";
import { paylaterAccountRoute } from "@/routes/dashboard";
import { useGetPayLaterInfo } from "@/hooks/wallet.hook";
import type { WalletStatus } from "@/enum/status";
import { formatDate } from "@/utils/date";
import { useEffect } from "react";

const PaylaterDetailPage: React.FC = () => {
    const { payLaterAccountNumber } = useParams({ strict: false });
    const navigate = useNavigate();
    const { data: paylaterInfo, isPending } = useGetPayLaterInfo(payLaterAccountNumber as string);
    const [form] = Form.useForm();


    // ======================
    // FILL FORM WHEN DATA READY
    // ======================
    useEffect(() => {
        if (paylaterInfo) {
            const formatted = {
                ...paylaterInfo,
                nextDueDate: paylaterInfo.nextDueDate ? formatDate(paylaterInfo.nextDueDate) : undefined,
                nextBillingDate: paylaterInfo.nextBillingDate ? formatDate(paylaterInfo.nextBillingDate) : undefined,
                approvedAt: paylaterInfo.approvedAt ? formatDate(paylaterInfo.approvedAt) : undefined,
            };

            form.setFieldsValue(formatted);
        }
    }, [paylaterInfo, form]);

    // ======================
    // RENDER
    // ======================
    return (
        <div className="p-5 space-y-5">
            {/* HEADER */}
            <h2 className="text-2xl text-black font-bold flex items-center">
                <ArrowLeftOutlined
                    className="mr-4 cursor-pointer"
                    onClick={() => navigate({ to: paylaterAccountRoute.to })}
                />
                Chi tiết ví trả sau 
            </h2>

            <div className="bg-white p-6 rounded-lg space-y-2">
                <Section
                    title="Thông tin ví"
                    description="Thông tin chi tiết của ví người dùng"
                >
                    {isPending ? (
                        <div className="flex justify-center py-10">
                            <Spin />
                        </div>
                    ) : (
                        <Form layout="vertical" form={form}>
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <Form.Item label="Chủ ví (Username)" name="username">
                                    <Input disabled />
                                </Form.Item>

                                <Form.Item label="Số tài khoản ví" name="walletNumber">
                                    <Input disabled />
                                </Form.Item>

                                <Form.Item label="Số TK PayLater" name="payLaterAccountNumber">
                                    <Input disabled />
                                </Form.Item>

                                <Form.Item label="Trạng thái" name="status">
                                    <Select<WalletStatus>
                                        options={[
                                            { value: "ACTIVE", label: "Hoạt động" },
                                            { value: "PENDING", label: "Chưa kích hoạt" },
                                            { value: "SUSPENDED", label: "Bị khóa" },
                                        ]}
                                        disabled
                                    />
                                </Form.Item>

                                <Form.Item label="Hạn mức tín dụng (Credit Limit)" name="creditLimit">
                                    <Input
                                        disabled
                                        value={
                                            paylaterInfo?.creditLimit != null
                                                ? paylaterInfo.creditLimit.toLocaleString("vi-VN") + " ₫"
                                                : "-"
                                        }
                                    />
                                </Form.Item>

                                <Form.Item label="Số dư đã dùng (Used Credit)" name="usedCredit">
                                    <Input
                                        disabled
                                        value={
                                            paylaterInfo?.usedCredit != null
                                                ? paylaterInfo.usedCredit.toLocaleString("vi-VN") + " ₫"
                                                : "-"
                                        }
                                    />
                                </Form.Item>

                                <Form.Item label="Lãi suất" name="interestRate">
                                    <Input
                                        disabled
                                        value={
                                            paylaterInfo?.interestRate != null
                                                ? `${paylaterInfo.interestRate}%`
                                                : "-"
                                        }
                                    />
                                </Form.Item>

                                <Form.Item label="Ngày đến hạn tiếp theo" name="nextDueDate">
                                    <Input disabled />
                                </Form.Item>

                                <Form.Item label="Ngày lập hóa đơn tiếp theo" name="nextBillingDate">
                                    <Input disabled />
                                </Form.Item>

                                <Form.Item label="Được duyệt bởi" name="approvedBy">
                                    <Input disabled value={paylaterInfo?.approvedBy ?? "-"} />
                                </Form.Item>

                                <Form.Item label="Ngày duyệt" name="approvedAt">
                                    <Input disabled />
                                </Form.Item>

                                
                            </div>
                        </Form>
                    )}
                </Section>
            </div>
        </div>
    );
};

export default PaylaterDetailPage;
