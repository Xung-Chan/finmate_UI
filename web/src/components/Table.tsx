import React from "react";
import { Table } from "antd";

interface TableComponentProps<T extends object> {
    columns: { key: string; label: string; render?: (item: T) => React.ReactNode }[];
    dataSource: T[];
    renderActions?: (item: T) => React.ReactNode;
    children?: (item: T, col: { key: string }) => React.ReactNode;
    summaryRow?: (data: T[]) => React.ReactNode;
}

const TableComponent = <T extends object>({
    columns,
    dataSource,
    renderActions,
    children,
    summaryRow,
}: TableComponentProps<T>) => {
    const centerCols = ["actions", "stt"];
    const antColumns = columns.map((col) => ({
        title: col.label,
        dataIndex: col.key,
        key: col.key,
        align: (col as any).align || (centerCols.includes(col.key) ? "center" : "left"),


        render: (_: any, record: T) =>
            col.key === "actions" && renderActions
                ? renderActions(record)
                : col.render
                    ? col.render(record)
                    : children?.(record, col) ?? String(record[col.key as keyof T] ?? ""),
    }));

    return (
        <Table
            bordered
            rowKey={(record) => (record as any).id?.toString() || JSON.stringify(record)}
            columns={antColumns}
            dataSource={dataSource}
            pagination={false}
            summary={summaryRow ? () => summaryRow(dataSource) : undefined}
            scroll={{ x: true }}
            className="custom-table"
        />
    );
};

export default TableComponent;
