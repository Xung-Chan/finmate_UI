import React, { useMemo, useState } from "react";
import {
    DollarCircleOutlined,
    SwapOutlined,
    CloseCircleOutlined,
    LineChartOutlined,
    PieChartOutlined,
    UserOutlined,
    BarChartOutlined,
} from "@ant-design/icons";

import { Row, Col, Card, DatePicker, Spin, Statistic, Table, Avatar } from "antd";
import type { ColumnsType } from "antd/es/table";
import dayjs from "dayjs";
import { useQuery } from "@tanstack/react-query";
import { transactionService } from "@/services/transaction.service";
import type {
    TransactionStatisticsResponse,
    TrendStatistics,
    DistributionStatistics,
    TopUsersStatistics,
} from "@/types/transaction.type";

import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    Tooltip,
    CartesianGrid,
    ResponsiveContainer,
    PieChart,
    Pie,
    Cell,
    BarChart,
    Bar,
} from "recharts";
import { colors } from "@/theme/color";


const defaultFrom = dayjs().subtract(29, "day").format("YYYY-MM-DD");
const defaultTo = dayjs().format("YYYY-MM-DD");

const TransactionAnalyticsPage: React.FC = () => {
    const [range, setRange] = useState<[any, any]>([dayjs(defaultFrom), dayjs(defaultTo)]);

    const fromDate = useMemo(() => range[0].format("YYYY-MM-DD"), [range]);
    const toDate = useMemo(() => range[1].format("YYYY-MM-DD"), [range]);

    const { data: stats, isLoading: loadingStats } = useQuery<TransactionStatisticsResponse>({
        queryKey: ["transactionStats", toDate],
        queryFn: () => transactionService.getTransactionStatistics(toDate),
        staleTime: 5 * 60 * 1000,
    });

    const { data: trendData, isLoading: loadingTrend } = useQuery<TrendStatistics[]>({
        queryKey: ["transactionTrend", fromDate, toDate],
        queryFn: () => transactionService.trendStatistics({ fromDate, toDate }),
        staleTime: 5 * 60 * 1000,
    });

    const { data: distributionData, isLoading: loadingDistribution } = useQuery<DistributionStatistics[]>({
        queryKey: ["transactionDistribution", fromDate, toDate],
        queryFn: () => transactionService.getDistributionStatistics({ fromDate, toDate }),
        staleTime: 5 * 60 * 1000,
    });

    const { data: topUsersData, isLoading: loadingTopUsers } = useQuery<TopUsersStatistics[]>({
        queryKey: ["transactionTopUsers", fromDate, toDate],
        queryFn: () => transactionService.getTopUsersStatistics({ fromDate, toDate, accountType: "WALLET" }),
        staleTime: 5 * 60 * 1000,
    });

    const loading = loadingStats || loadingTrend || loadingDistribution || loadingTopUsers;

    const lineChartData = trendData?.map((t) => ({
        date: t.date,
        transactions: t.totalTransactions ?? 0,
        value: t.totalValue ?? 0,
    })) ?? [];

    const pieData = distributionData?.map((d) => ({ name: d.label || d.expenseName || "Khác", value: d.totalValue ?? d.totalTransactions ?? 0 })) ?? [];
    const PIE_COLORS = [
        colors.blue.b5,
        colors.blue.b2,
        colors.green.g3,
        colors.orange.o1,
        colors.red.r2,
    ];

    const barData = distributionData?.map((d) => ({ name: d.label || d.expenseName || "Khác", transactions: d.totalTransactions ?? 0 })) ?? [];

    const topUsersColumns: ColumnsType<TopUsersStatistics> = [
        {
            title: "Avatar",
            dataIndex: "avatarUrl",
            key: "avatarUrl",
            width: 80,
            render: (v: string, record: TopUsersStatistics) => (
                <Avatar
                    src={v}
                    style={{
                        backgroundColor: colors.blue.b2,
                        verticalAlign: "middle",
                    }}
                >
                    {!v && (record.fullName || record.username)?.charAt(0)}
                </Avatar>
            ),
        },
        {
            title: "Username",
            dataIndex: "username",
            key: "username",
            render: (v: string) => v || "-",
        },
        {
            title: "Họ và tên",
            dataIndex: "fullName",
            key: "fullName",
            render: (v: string) => v || "-",
        },
        {
            title: "Số tài khoản",
            dataIndex: "accountNumber",
            key: "accountNumber",
            render: (v: string) => v || "-",
        },
        {
            title: "Số giao dịch",
            dataIndex: "transactionCount",
            key: "transactionCount",
            align: "right",
            render: (v: number) => v ?? 0,
        },
        {
            title: "Tổng giá trị",
            dataIndex: "totalValue",
            key: "totalValue",
            align: "right",
            render: (v: number) =>
                v ? (
                    <span style={{ fontWeight: 600, color: colors.blue.b5 }}>
                        {v.toLocaleString()} ₫
                    </span>
                ) : (
                    "-"
                ),
        },
    ];
    return (
        <div className="p-5">
            <h2 className="text-2xl font-bold mb-4">Báo cáo giao dịch</h2>

            <Card className="mb-4">
                <Row gutter={16} align="middle">
                    <Col>
                        <DatePicker.RangePicker value={range} onChange={(vals: any) => { if (vals) setRange(vals); }} />
                    </Col>
                </Row>
            </Card>

            {loading ? (
                <div className="flex justify-center py-20"><Spin /></div>
            ) : (
                <>
                    <Row gutter={16} className="my-4">
                        <Col span={6}>
                            <Card
                                style={{
                                    background: colors.blue.b5,
                                    color: colors.white.w1,
                                    borderRadius: 12,
                                }}
                            >
                                <Statistic
                                    title={<span style={{ color: colors.white.w1 }}>Tổng giao dịch</span>}
                                    value={stats?.totalTransactions ?? 0}
                                    prefix={<SwapOutlined />}
                                    valueStyle={{ color: colors.white.w1, fontWeight: 600 }}
                                />
                            </Card>
                        </Col>

                        <Col span={6}>
                            <Card
                                style={{
                                    background: colors.blue.b2,
                                    color: colors.white.w1,
                                    borderRadius: 12,
                                }}
                            >
                                <Statistic
                                    title={<span style={{ color: colors.white.w1 }}>Tổng giá trị</span>}
                                    value={stats?.totalValue ?? 0}
                                    suffix="₫"
                                    prefix={<DollarCircleOutlined />}
                                    valueStyle={{ color: colors.white.w1, fontWeight: 600 }}
                                />
                            </Card>
                        </Col>

                        <Col span={6}>
                            <Card
                                style={{
                                    background: colors.red.r1,
                                    color: colors.white.w1,
                                    borderRadius: 12,
                                }}
                            >
                                <Statistic
                                    title={<span style={{ color: colors.white.w1 }}>Tỷ lệ thất bại</span>}
                                    value={(stats?.failedRate ?? 0) * 100}
                                    precision={2}
                                    suffix="%"
                                    prefix={<CloseCircleOutlined />}
                                    valueStyle={{ color: colors.white.w1, fontWeight: 600 }}
                                />
                            </Card>
                        </Col>

                        <Col span={6}>
                            <Card
                                style={{
                                    background: colors.green.g1,
                                    color: colors.white.w1,
                                    borderRadius: 12,
                                }}
                            >
                                <Statistic
                                    title={<span style={{ color: colors.white.w1 }}>Kỳ trước (GD)</span>}
                                    value={stats?.previousPeriodTransactions ?? 0}
                                    prefix={<LineChartOutlined style={{ color: colors.white.w1 }} />}
                                    valueStyle={{ color: colors.white.w1, fontWeight: 600 }}
                                />
                            </Card>
                        </Col>
                    </Row>


                    <Row gutter={16} className="mb-4">
                        <Col span={16}>
                            <Card title={
                                <span className="flex items-center gap-2">
                                    <LineChartOutlined style={{ color: colors.blue.b5 }} />
                                    Xu hướng giao dịch
                                </span>
                            }>
                                <div style={{ width: "100%", height: 300 }}>
                                    <ResponsiveContainer>
                                        <LineChart data={lineChartData}>
                                            <CartesianGrid strokeDasharray="3 3" />
                                            <XAxis dataKey="date" />
                                            <YAxis />
                                            <Tooltip />
                                            <Line
                                                type="monotone"
                                                dataKey="transactions"
                                                stroke={colors.blue.b5}
                                                strokeWidth={3}
                                                name="Số giao dịch"
                                            />
                                            <Line
                                                type="monotone"
                                                dataKey="value"
                                                stroke={colors.green.g1}
                                                strokeWidth={3}
                                                name="Giá trị"
                                            />

                                        </LineChart>
                                    </ResponsiveContainer>
                                </div>
                            </Card>
                        </Col>

                        <Col span={8}>
                            <Card title={
                                <span className="flex items-center gap-2">
                                    <PieChartOutlined style={{ color: colors.blue.b5 }} />
                                    Phân bổ theo loại
                                </span>
                            }>
                                <div style={{ width: "100%", height: 300 }}>
                                    <ResponsiveContainer>
                                        <PieChart>
                                            <Pie data={pieData} dataKey="value" nameKey="name" outerRadius={100}>
                                                {pieData.map((_, index) => (
                                                    <Cell key={index} fill={PIE_COLORS[index % PIE_COLORS.length]} />
                                                ))}
                                            </Pie>
                                            <Tooltip />
                                        </PieChart>
                                    </ResponsiveContainer>
                                </div>
                            </Card>
                        </Col>
                    </Row>

                    <Row gutter={24} className="mb-4">
                        <Col span={24} className="mb-4">
                            <Card title={
                                <span className="flex items-center gap-2">
                                    <UserOutlined style={{ color: colors.blue.b5 }} />
                                    Top người dùng
                                </span>
                            }>
                                <div style={{ width: "100%", height: 300 }}>
                                    <Table
                                        dataSource={topUsersData ?? []}
                                        columns={topUsersColumns}
                                        rowKey={(r) => r.accountNumber || r.username || Math.random()}
                                        pagination={false}
                                        size="middle"
                                    />
                                </div>
                            </Card>
                        </Col>

                        <Col span={24}>
                            <Card title={
                                <span className="flex items-center gap-2">
                                    <BarChartOutlined style={{ color: colors.blue.b5 }} />
                                    Phân bổ theo số giao dịch
                                </span>
                            }>
                                <div style={{ width: "100%", height: 300 }}>
                                    <ResponsiveContainer>
                                        <BarChart data={barData}>
                                            <CartesianGrid strokeDasharray="3 3" />
                                            <XAxis dataKey="name" />
                                            <YAxis />
                                            <Tooltip />
                                            <Bar dataKey="transactions" fill={colors.blue.b3} radius={[6, 6, 0, 0]} name={`số giao dịch`} />
                                        </BarChart>
                                    </ResponsiveContainer>
                                </div>
                            </Card>
                        </Col>
                    </Row>
                </>
            )}
        </div>
    );
};

export default TransactionAnalyticsPage;
