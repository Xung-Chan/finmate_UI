import React from "react";
import { Table } from "antd";
import type { ColumnsType, ColumnType } from "antd/es/table";

export interface TableColumn<T> {
  key: Extract<keyof T, string> | "actions" | "stt";
  label: string;
  align?: "left" | "center" | "right";
  render?: (item: T, index: number) => React.ReactNode;
}

interface TableComponentProps<T extends object> {
  columns: TableColumn<T>[];
  dataSource: T[];
  renderActions?: (item: T) => React.ReactNode;
  summaryRow?: (data: T[]) => React.ReactNode;
}

const TableComponent = <T extends object>({
  columns,
  dataSource,
  renderActions,
  summaryRow,
}: TableComponentProps<T>) => {
  const antColumns: ColumnsType<T> = columns.map(
    (col): ColumnType<T> => ({
      title: col.label,
      dataIndex:
        col.key === "actions" || col.key === "stt"
          ? undefined
          : col.key,
      key: col.key,
      align: col.align ?? "left",
      render: (_: any, record: T, index: number) => {
        if (col.key === "actions" && renderActions) {
          return renderActions(record);
        }
        if (col.render) {
          return col.render(record, index);
        }
        if (col.key === "stt") {
          return index + 1;
        }
        return String(record[col.key as keyof T] ?? "");
      },
    })
  );

  return (
    <Table<T>
      bordered
      rowKey={(record) => (record as any).id?.toString()}
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
